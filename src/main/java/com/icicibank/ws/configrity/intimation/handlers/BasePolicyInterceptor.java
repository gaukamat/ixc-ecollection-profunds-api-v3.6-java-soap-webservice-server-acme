package com.icicibank.ws.configrity.intimation.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.w3c.dom.Node;

import com.icicibank.ws.configrity.model.ObjectFactory;

public abstract class BasePolicyInterceptor implements SOAPHandler<SOAPMessageContext> {

	static {
		org.apache.xml.security.Init.init();
	}
	
	protected Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());

	private static final String 			POLICY_DIRECTION_REQUEST_ONLY 					= "request-only";
	private static final String 			POLICY_DIRECTION_RESPONSE_ONLY 				= "response-only";
	private static final String 			POLICY_DIRECTION_REQUEST_RESPONSE 			= "request-response";

	protected static interface FieldValue {
		public static interface Location {
			String 	HTTP_HEADER 			= "http-header";
			String 	SOAP_HEADER 			= "soap-header";
			String 	MESSAGE_BODY 			= "message-body";
		}
	}
	
	protected static interface Namespaces {
		String WS 									= "http://www.icicibank.com/ws/configrity/intimation";
		String API 									= "http://www.icicibank.com/api/acme";
		String WRAP 							= "http://www.icicibank.com/api/acme-wrap";
		String COMPACT 						= "http://www.icicibank.com/api/acme/configrity/compact";
		String EXT									= "http://www.icicibank.com/api/acme-ext";
	}

	protected static interface NamespacePrefixes {
		String WS 									= "acmews";
		String API 									= "acme";
		String WRAP 							= "acmew";
		String COMPACT 						= "acmec";
		String EXT									= "acmex";
	}

	protected Boolean policyInitIndicator = Boolean.FALSE;
	
	private List<String> 	enforceablePolicyList 			= null;
	private String 				policyIndicatorDirection		= null;
	
	protected String policyName 		= null;
	protected Marker marker 				= null;
	
	protected Properties policyDefaults = null;
	
	protected static JAXBContext	 _XmlBindingContext				= null;
    protected static KeyStore 		_WebServicesKeyStore				= null;

    protected static final String CONFIGRITY_KEYSTORE						= "acme-ks.pkcs12";
    protected static final String CONFIGRITY_KEYSTORE_LOCATION	= "./keystores";
    protected static final String CONFIGRITY_KEYSTORE_TYPE			= "PKCS12";
    protected static final String CONFIGRITY_KEYSTORE_PWD			= "changeit";
    protected static final String CONFIGRITY_KEY_PWD						= "changeit";

    @SuppressWarnings("unchecked")
	public BasePolicyInterceptor() {
		super();
		
		// Decides which all policies to apply and whether to apply them one-way or both-ways.
		Properties policyConfiguration = loadPolicyConfiguration("policy-config.properties");
		String policies = policyConfiguration.getProperty("enforced-policy-list");

		if(!(policies == null)) {
			String[] policyList = policies.split("\\|");
			
			if (policyList == null || policyList.length == 0) {
				enforceablePolicyList = Collections.EMPTY_LIST;
				warn("Policy configuration file found and loaded but no policies were found in there.");
			} 
			else {
				enforceablePolicyList = Arrays.asList(policyList);
				warn("Policy configuration file found and loaded. " + enforceablePolicyList + " policies will be used if the request does not carry these details.");
			}
		}
		policyIndicatorDirection = policyConfiguration.getProperty("policy-enforcement-indicator", POLICY_DIRECTION_REQUEST_RESPONSE);
		
		initPolicy();
		marker = MarkerFactory.getIMarkerFactory().getMarker(getPolicyName());
		
		setupSecurityKeystores();
	}
	
	public String getPolicyName() {
		return policyName;
	}
	
	protected void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	protected void info(String message) {
		log.info(marker, message);
	}

	protected void warn(String message) {
		log.warn(marker, message);
	}

	protected void severe(String message) {
		log.error(marker, message);
	}

	protected boolean isForwardPolicyEnforcementRequired()  {
		// info("Policies-in-force: " + enforceablePolicyList);
		if(enforceablePolicyList == null || 
			enforceablePolicyList.isEmpty()) {
			return false;
		}
		
		if(!enforceablePolicyList.contains(getPolicyName())) {
			return false;
		}
		if(policyIndicatorDirection.equals(POLICY_DIRECTION_REQUEST_RESPONSE)) {
			return true;
		}
		else if(policyIndicatorDirection.equals(POLICY_DIRECTION_REQUEST_ONLY)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected boolean isReturnPolicyEnforcementRequired() {
		// info("Policies-in-force: " + enforceablePolicyList);
		if(enforceablePolicyList == null || 
			enforceablePolicyList.isEmpty()) {
			return false;
		}
		
		if(!enforceablePolicyList.contains(getPolicyName())) {
			return false;
		}

		if(policyIndicatorDirection.equals(POLICY_DIRECTION_REQUEST_RESPONSE)) {
			return true;
		}
		else if(policyIndicatorDirection.equals(POLICY_DIRECTION_RESPONSE_ONLY)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected abstract void initPolicy();
 	protected abstract boolean handleRequest(SOAPMessageContext messageContext);
 	protected abstract boolean handleResponse(SOAPMessageContext messageContext);
 	
 	@PostConstruct
 	public void setupHandler() {
 	}
 	
 	@PreDestroy
 	public void teardownHandler() {
 	}
 	
 	
 	public boolean handleMessage(SOAPMessageContext messageContext) {
		Boolean isOutbound = (Boolean)messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(isOutbound) {
			// MessageAuditor will be enforced by default.
			if(isReturnPolicyEnforcementRequired() || "MessageAuditor".equals(getPolicyName())) {
				log.warn("[RETURN]  " + getPolicyName() + " is in force and will be processed.");
				return handleResponse(messageContext);
			}
			else {
				log.warn("[RETURN]  " + getPolicyName() + " is in not force and will not be processed.");
				return true;
			}
		}
		else {
			// MessageAuditor will be enforced by default.
			if(isForwardPolicyEnforcementRequired() || "MessageAuditor".equals(getPolicyName())) {
				log.warn("[FORWARD]  " + getPolicyName() + " is in force and will be processed.");
				return handleRequest(messageContext);
			}
			else {
				log.warn("[FORWARD]  " + getPolicyName() + " is in not force and will not be processed.");
				return true;
			}
		}
	}

	public boolean handleFault(SOAPMessageContext messageContext) {
		return true;
	}

	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() {
		return Collections.EMPTY_SET;
	}
	
	protected Properties loadPolicyConfiguration(String policyConfigurationFileName) {
		Properties policyConfiguration = new Properties();
		try {
			log.info("Loading \"" + policyConfigurationFileName + "\" policy configuration properties file.");
			policyConfiguration.load(this.getClass().getClassLoader().getResourceAsStream(policyConfigurationFileName));
		}
		catch (IOException e) {
			warn("Unable to load policy configuration \"" + policyConfigurationFileName + "\" . "
					+ "Service may not operate as desired (implementations may choose to fallback on defaults).");
		}
		
		log.info("Configuration Properties: " + policyConfiguration);
		return policyConfiguration;
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> getHttpRequestHeader(SOAPMessageContext messageContext, String headerName) {
		Map<String, List<String>> requestHeaders = (Map<String, List<String>>) 
					messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
		
		for(String headerKey: requestHeaders.keySet()) {
			if(headerKey.equalsIgnoreCase(headerName)) {
				return requestHeaders.get(headerName);
			}
		}
		return null;
	}

	protected String getSingleHttpRequestHeader(SOAPMessageContext messageContext, String headerName) {
		List<String> headerValues = getHttpRequestHeader(messageContext, headerName);
		
		if(headerValues == null || headerValues.isEmpty()) {
			return null;
		}
		return headerValues.get(0);
	}
	
	protected String getMessageEncoding(SOAPMessage msg) throws SOAPException {
		String encoding = "utf-8";
		if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null) {
			encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
		}
		return encoding;
	}

	protected String toMessageString(Object object, QName elementQName) {
		StringWriter writer = new StringWriter();
		try {
			JAXBElement eObject = new JAXBElement(elementQName, object.getClass(), object);
			getXmlBindingContext().createMarshaller().marshal(eObject, writer);
			return writer.getBuffer().toString();
		} 
		catch (JAXBException e) {
			log.warn("Failed to stringify the object. Check if the object has JAXB annotations and the its package is registered with the JAXB Context.", e);
			return "-- ERROR --";
		}
	}

	protected String toMessageString(Object object) {
		// May not be required if runtime polymorphism handles this overloaded method.
		if(object instanceof SOAPMessageContext) {
			return toMessageString(((SOAPMessageContext)object));
		}
		if(object instanceof Node) {
			return toMessageString(((Node)object));
		}
		StringWriter writer = new StringWriter();
		try {
			log.info("Stringify object: " + object);
			getXmlBindingContext().createMarshaller().marshal(object, writer);
			return writer.getBuffer().toString();
		} 
		catch (JAXBException e) {
			log.warn("Failed to stringify the object. Check if the object has JAXB annotations and the its package is registered with the JAXB Context.", e);
			return "-- ERROR --";
		}
	}
	
	protected String toMessageString(SOAPMessageContext messageContext) {
		log.info("Stringify object from within: " + messageContext);
		SOAPMessage msg = messageContext.getMessage(); 
		String message = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			msg.writeTo(baos);
			message = baos.toString(getMessageEncoding(messageContext.getMessage()));
		} 
		catch (SOAPException | IOException ex) {            
			log.error("Unable to stringify the SOAP message.", ex);
		} 
		return message;
	}
	
	protected String toMessageString(Node node) {
		log.info("Stringify node object: " + node);
		StringWriter writer = new StringWriter();
		try {
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        
	        // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        
	        transformer.transform(new DOMSource(node), new StreamResult(writer));
	    } 
		catch (TransformerException ex) {            
			log.error("Unable to stringify the node..", ex);
			writer.append("-- ERROR --");
		} 
		return writer.toString();
	}

	protected String toMessageBodyString(SOAPMessageContext messageContext) {
		String messageString = null;
		try {
			Node node = messageContext.getMessage().getSOAPBody().getFirstChild(); 
			messageString = toMessageString(node);
	    } 
		catch (SOAPException  ex) {            
			log.error("Unable to stringify the SOAP business message.", ex);
			messageString = "-- ERROR --";
		} 
		return messageString;
	}


	protected void setupXmlBindingContext() {
 		if(_XmlBindingContext == null) {
 			try {
				_XmlBindingContext = 
				 		JAXBContext.newInstance("com.icicibank.api.acme_ext:"
				 				+ "com.icicibank.ws.configrity.model:"
				 				+ "com.icicibank.ws.configrity.model.wrap:"
				 				+ "com.icicibank.ws.configrity.model.secure.compact");
				
				log.info("Setup the Xml Binding Context needed to replace pre-processed (decrypted / encrypted) message body in the inbound / outbound message.");
			} 
 			catch (JAXBException e) {
 				throw new WebServiceException("Xml Binding Context is needed to replace pre-processed (decrypted / encrypted) message body in the inbound / outbound message.", e);
			}
		}
	}

	protected JAXBContext getXmlBindingContext() {
		if(_XmlBindingContext == null) {
			setupXmlBindingContext();
		}
		return _XmlBindingContext;
	}

 	protected Object toEntity(SOAPMessageContext messageContext, Class clazz) {
 		try {
			log.info("Unmarshalling business payload from within the SOAP Message Context to type \"" + clazz.getName() + "\".");
			Object o =  toEntity(messageContext.getMessage().getSOAPBody().getFirstChild(), clazz);
			log.info("Unmarshalled business payload from within the SOAP Message Context.");
			return o;
		} 
 		catch (SOAPException e) {
 			throw new WebServiceException("Failed to unmarshall node " + messageContext + " to type \"" + clazz.getName() + "\". "
 					+ "Could not get to the message payload (first child) within SOAP Body.", e);
		}
	}
 	
 	protected Object toEntity(Node node, Class clazz) {
 		try {
			log.info("Unmarshalling Node: " + node + " to type \"" + clazz.getName() + "\".");
			Object entity = getXmlBindingContext().createUnmarshaller().unmarshal(node);
			if(entity instanceof JAXBElement) {
				entity = ((JAXBElement)entity).getValue();
			}
			log.info("Unmarshalled Entity: " + entity + " [Type: " + entity.getClass().getName() +"]");
			return entity;
		} 
 		catch (JAXBException e) {
 			throw new WebServiceException("Failed to unmarshall node " + node + " to type \"" + clazz.getName() + "\".", e);
		}
	}

	// SOAPMessage (an XML Document) --> SOAPPart --> SOAPEnvelope --> SOAPHeader, SOAPBody
	// SOAPHeader --> Header
	// SOAPBody --> XML Content, SOAPFault
	protected String getSingleSOAPHeaderFieldValue(SOAPMessageContext messageContext, String xpath) {
		Node source = null;
		try {
			// SOAPHeader soapHeader 
			source = messageContext.getMessage().getSOAPPart().getEnvelope().getHeader();
			return getSingleFieldValue(source, xpath);
		} 
		catch (SOAPException e) {
			log.error("Unable to access the SOAP header.", e);
		}
		return null;
	}
	
	protected String getSingleSOAPBodyFieldValue(SOAPMessageContext messageContext, String xpath) {
		Node source = null;
		try {
			// SOAPBody soapBody
			source = messageContext.getMessage().getSOAPPart().getEnvelope().getBody().getFirstChild();
			return getSingleFieldValue(source, xpath);
		} 
		catch (SOAPException e) {
			log.error("Unable to access the SOAP body.", e);
		}
		return null;
	}

	protected String getSingleFieldValue(Node source, String xpath) {
    	Node field = null;
		try {
			// SOAPHeader soapHeader 
			XPathFactory xpf = XPathFactory.newInstance();
	    	XPath xp = xpf.newXPath();
	    	xp.setNamespaceContext(new NamespaceContext() {
				@Override
				public Iterator getPrefixes(String namespaceURI) {
					return null;
				}
				
				@Override
				public String getPrefix(String namespaceURI) {
					return null;
				}
				
				@Override
				public String getNamespaceURI(String prefix) {
					String nsURI = null;
					switch(prefix) {
						case "api" 			: 	nsURI = Namespaces.API; 
														break;
						case "acme" 		: 	nsURI = Namespaces.API; 
														break;
						case "wrap" 		: 	nsURI = Namespaces.WRAP; 
														break;
						case "acmew" 	: 	nsURI = Namespaces.WRAP; 
														break;
						case "cv3" 		: 	nsURI = Namespaces.COMPACT; 
														break;
						case "ext" 			: 	nsURI = Namespaces.EXT; 
														break;
						case "acmex" 	: 	nsURI = Namespaces.EXT; 
														break;
					}
					
					log.info("Namespace \"" + nsURI + "\" bound to prefix \"" + prefix + "\".");
					return nsURI;
				}
			});
	    	
	    	XPathExpression xpe = xp.compile(xpath);
	    	log.info("XPath \"" + xpath +  "\" expresssion compiled. Context: " + source + " [Node Name: {" + source.getNamespaceURI()+ "}:" + source.getLocalName()+ "]");
	    	field = (Node)xpe.evaluate(source, XPathConstants.NODE);
	    	log.info("Found " + field + " at XPath \"" + xpath +  "\".");
		} 
		catch (XPathExpressionException | ClassCastException e) {
			log.error("Unable to pull the node from the source using the xpath expression \"" + xpath +"\".", e);
		}

		if(field == null) {
			return null;
		}
		return field.getTextContent();
	}
	
//	protected void setHttpHeader(SOAPMessageContext messageContext, String fieldName, String fieldValue) {
//		boolean response = ((Boolean) messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
//		log.info("Binding value \"" + fieldValue + "\" to field \"" + fieldName +"\" in HTTP Header.");
//
//		Map<String, List<String>> headers = null;
//		if (response) {
//			// this is a JAX-WS-provided map of HTTP headers
//			headers = (Map<String, List<String>>) messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
//			log.info("[Outbound] HTTP Headers : " + headers);
//		} 
//		else {
//			headers = (Map<String, List<String>>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
//			log.info("[Inbound] HTTP Headers : " + headers);
//		}
//
//		if (null == headers) {
//			// create a new map of HTTP headers if there isn't already one
//			headers = new HashMap<String, List<String>>();
//			// HTTP Headers will always be present for the request.
//			messageContext.put(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY, headers);
//		}
//
//		// add your desired header
//		headers.put(fieldName, Collections.singletonList(fieldValue));
//		log.info("Value \"" + fieldValue + "\" bound to field \"" + fieldName +"\" in HTTP Header.");
//	}

	protected void setHttpHeader(SOAPMessageContext messageContext, String fieldName, String fieldValue) {
		boolean response = ((Boolean) messageContext.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
		log.info("Binding value \"" + fieldValue + "\" to field \"" + fieldName +"\" in HTTP Header.");

		Map<String, List<String>> headers = null;
		if (response) {
			// this is underlying http response object
	        HttpServletResponse httpServletResponse = (HttpServletResponse) messageContext.get(MessageContext.SERVLET_RESPONSE);
	        log.info("Http Servlet Response: " + httpServletResponse);
	          
			// add your desired header
	        httpServletResponse.addHeader(fieldName, fieldValue);
			log.info("Value \"" + fieldValue + "\" bound to field \"" + fieldName +"\" in HTTP Header.");
		} 
	}

	protected void setSOAPHeaderField(SOAPMessageContext messageContext, 
			String prefix, String fieldNameSpace, String fieldName, String fieldValue) {
		log.info("Binding value \"" + fieldValue + "\" to field \"" + fieldName +"\" in SOAP Header.");
		try {
			SOAPHeader soapHeader = messageContext.getMessage().getSOAPHeader();
			if(soapHeader == null) {
				SOAPEnvelope soapEnvelope = messageContext.getMessage().getSOAPPart().getEnvelope();
				soapHeader = soapEnvelope.addHeader();
			}
			
			SOAPFactory soapFactory = SOAPFactory.newInstance();
			
			SOAPElement fieldElement = soapFactory.createElement(fieldName, prefix, fieldNameSpace);
			fieldElement.addTextNode(fieldValue);
			
			soapHeader.addChildElement(fieldElement);
			
			log.info("Bound SOAP Element:  " + fieldElement + " to SOAP Header: " + soapHeader);
			log.info("Value \"" + fieldValue + "\" bound to field \"" + fieldName +"\" in SOAP Header.");
		} 
		catch (SOAPException e) {
			String message = "Failed to add a SOAP header for field " + (fieldNameSpace == null ? "" : ("{" + fieldNameSpace +"}:")) +  fieldName + ".";
			log.error(message, e);
			throw new WebServiceException(message, e);
		}
	}

	protected void setSOAPBodyField(SOAPMessageContext messageContext, 
			String prefix, String fieldNameSpace, String fieldName, String fieldValue) {
		log.info("Binding value \"" + fieldValue + "\" to field \"" + fieldName +"\" in SOAP Body.");
		try {
			SOAPBody soapBody = messageContext.getMessage().getSOAPBody();
			SOAPFactory soapFactory = SOAPFactory.newInstance();
			
			SOAPElement fieldElement = soapFactory.createElement(fieldName, prefix, fieldNameSpace);
			fieldElement.addTextNode(fieldValue);

			Node adoptedFieldElement = soapBody.getFirstChild().getOwnerDocument().adoptNode(fieldElement);
			soapBody.getFirstChild().appendChild(adoptedFieldElement);

			log.info("Bound SOAP Element:  " + fieldElement + " to SOAP Body: " + soapBody);
			log.info("Value \"" + fieldValue + "\" bound to field \"" + fieldName +"\" in SOAP Body.");
		} 
		catch (SOAPException e) {
			String message = "Failed to add a SOAP body field " + (fieldNameSpace == null ? "" : ("{" + fieldNameSpace +"}:")) +  fieldName + ".";
			log.error(message, e);
			throw new WebServiceException(message, e);
		}
	}

 	protected void setNewBusinessPayload(SOAPMessageContext messageContext, Object newBusinessPayload) {
 		if(newBusinessPayload instanceof Node) {
 			// May not be required if runtime polymorphism handles this overloaded method.
 			setNewBusinessPayload(messageContext, ((Node)newBusinessPayload));
 			return;
 		}
 		// newBusinessPayload should be JAXB annotated object for this to work.
		SOAPBody soapBody = null;
		try {
			soapBody = messageContext.getMessage().getSOAPBody();
			Node oldBusinessPayload = soapBody.getFirstChild();
			
			log.info("Old Business Payload: [" + toMessageString(oldBusinessPayload)+ "]");
			log.info("New Business Payload: [" + toMessageString(newBusinessPayload)+ "]");

			soapBody.removeChild(oldBusinessPayload);

			getXmlBindingContext().createMarshaller().marshal(newBusinessPayload, soapBody);
			
			log.info("New business payload " + newBusinessPayload + " injected in SOAP Body.");
		} 
		catch (SOAPException | JAXBException e) {
			throw new WebServiceException("Failed to inject (replace) the new business payload in the SOAP body.", e);
		}
	
 	}
 	
 	protected void setNewBusinessPayload(SOAPMessageContext messageContext, Node newBusinessPayload) {
		log.info("Injecting business payload " + newBusinessPayload + " in SOAP Body.");

		SOAPBody soapBody = null;
		try {
			soapBody = messageContext.getMessage().getSOAPBody();
			Node oldBusinessPayload = soapBody.getFirstChild();
			
			log.info("Old Business Payload: [" + toMessageString(oldBusinessPayload)+ "]");
			log.info("New Business Payload: [" + toMessageString(newBusinessPayload)+ "]");
			
			Node adoptedNewBusinessPayload = soapBody.getFirstChild().getOwnerDocument().adoptNode(newBusinessPayload);
			soapBody.replaceChild(adoptedNewBusinessPayload, oldBusinessPayload);

			log.info("New business payload " + newBusinessPayload + " injected in SOAP Body.");
		} 
		catch (SOAPException e) {
			throw new WebServiceException("Failed to inject (replace) the new business payload in the SOAP body.", e);
		}
	}

    private void setupSecurityKeystores() {
    	if(_WebServicesKeyStore == null) {
    		info("Web Services security keystore setup in progress.");

    		InputStream keyStoreStream = this.getClass().getClassLoader().getResourceAsStream(CONFIGRITY_KEYSTORE);
    		info("Loading keystore from " + keyStoreStream + " stream.");
    		
    		try {
    			// [08-Aug-2020] With BouncyCastleProvider, we somehow cannot access the secret keys in the keystore. .
				// _WebServicesKeyStore = KeyStore.getInstance(CONFIGRITY_KEYSTORE_TYPE, new BouncyCastleProvider());	
				
    			_WebServicesKeyStore = KeyStore.getInstance(CONFIGRITY_KEYSTORE_TYPE);
    			_WebServicesKeyStore.load(keyStoreStream, CONFIGRITY_KEYSTORE_PWD.toCharArray());

				Enumeration<String> keyAliasEnum = _WebServicesKeyStore.aliases();
				int count = 0;
				
				
				while(keyAliasEnum.hasMoreElements()) {
					StringBuffer logBuffer = new StringBuffer();
					logBuffer.append(++count + "> ");

					String alias = keyAliasEnum.nextElement();
					
					logBuffer.append(alias);
					
				// Boolean isPublicCertKeyEntry			 	= _WebServicesKeyStore.isCertificateEntry(alias);
					Boolean isPrivateOrSecretKeyEntry 	= _WebServicesKeyStore.isKeyEntry(alias);
					
					logBuffer.append((isPrivateOrSecretKeyEntry ? " Private / Secret Key " : " Public Key Cert " ));
					
					if(isPrivateOrSecretKeyEntry) {
						try {
							Key key = _WebServicesKeyStore.getKey(alias, CONFIGRITY_KEYSTORE_PWD.toCharArray());
							
							logBuffer.append("[Algo: " + key.getAlgorithm());
							logBuffer.append(", Format: " + key.getFormat() + "]");
						} 
						catch (UnrecoverableKeyException e) {
							logBuffer.append("[No info: " + e.getLocalizedMessage() + "]");
						}
					}
					else {
						Certificate cert = _WebServicesKeyStore.getCertificate(alias);
						
						logBuffer.append(" [Type: " + cert.getType());
						logBuffer.append(", Algo: " + cert.getPublicKey().getAlgorithm());
						logBuffer.append(", Format: " + cert.getPublicKey().getFormat() + "]");
					}
					
					log.info(logBuffer.toString());	
				}
				
	    		info("Web Services security keystore setup.");
			} 
    		catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
    			severe("Unable to load the configrity keystore. Confidentiality validation will not work. [Root Cause: " + e + "]");
    			return;
			}
    	}
    }
    
    protected KeyStore getWebServicesKeyStore() {
    	return _WebServicesKeyStore;
    }
    
    // Private / Secret key.
	protected Key findPrivateOrSecretKey(String keyId) {
		try {
			Key key = getWebServicesKeyStore().getKey(keyId, CONFIGRITY_KEY_PWD.toCharArray());
			if(key == null) {
				throw new WebServiceException("No private / secret key found within the web services keystore bound to \"" + keyId + "\".");
			}
			log.info("Private / Secret key found for \"" + keyId + "\" key id.");
			return key;
		} 
		catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			throw new WebServiceException("Unable to access the private / secret key bound to \"" + keyId + "\". "
					+ "Key may/may not be present within the web services keystore or may be of a different type (Public Key Cert instead of a Secret / Private Key).");
		}
	}

	protected Key findPublicKey(String keyId)  {
		try {
			Key key = getWebServicesKeyStore().getCertificate(keyId).getPublicKey();
			if(key == null) {
				throw new WebServiceException("No public key certificate found within the web services keystore bound to \"" + keyId + "\".");
			}
			log.info("Public key found for \"" + keyId + "\" key id.");
			return key;
		} 
		catch (KeyStoreException e) {
			throw new WebServiceException("Unable to access the public key cert bound to \"" + keyId + "\". "
					+ "Key may/may not be present within the web services keystore or may be of a different type (Secret / Private Key instead of a Public Key Cert).");
		}
	}
}
