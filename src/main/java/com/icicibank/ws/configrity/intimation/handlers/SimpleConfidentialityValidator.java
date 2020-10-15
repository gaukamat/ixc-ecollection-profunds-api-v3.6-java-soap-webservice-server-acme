package com.icicibank.ws.configrity.intimation.handlers;

import java.security.Key;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icicibank.ws.configrity.model.wrap.IntimationRequestType;
import com.icicibank.ws.configrity.model.wrap.IntimationResponseType;

import model.mapstruct.IntimationRequestUnwrapperMapper;
import model.mapstruct.IntimationResponseWrapperMapper;

public class SimpleConfidentialityValidator extends BasePolicyInterceptor 
	implements SOAPHandler<SOAPMessageContext> {
	
	public SimpleConfidentialityValidator() {
		super();
	}
	
	private static interface SimpleConfidentialityValidatorConstants {
		interface ConfigurationParameters {
				String ENCRYPTION_ENCODING 								= "encryption-encoding";
				String ENCRYPTION_KEY_TYPE								= "encryption-keytype";
				String ENCRYPTION_CIPHER 									= "encryption-cipher";
				String ENCRYPTION_PROVIDER_KEY_ID 				= "encryption-providerkey-alias";
				String ENCRYPTION_PROVIDER_KEY_LENGTH 	= "encryption-providerkey-length";
				String ENCRYPTION_CONSUMER_KEY_ID 			= "encryption-consumerkey-alias";
				String ENCRYPTION_CONSUMER_KEY_LENGTH 	= "encryption-consumerkey-length";
				String ENCRYPTION_SELECT_FIELDS 						= "encryption-select-fields";
		}
		
		interface FieldNames {
			String CIPHER_IV = "Cipher-IV";
		}
		
		interface Type {
			String SELECT_FIELDS_ALL 				= 	"*";
			String ENCODING_BASE64				= "base64";
			String ENCODING_HEX						= "hex";
		}
		
		interface XPaths {
			String IV_FIELD		= "acmex:Cipher-IV";
			String IV_ENCODING_FIELD		= "acmex:Cipher-IV[@encoding]";
		}
	}
	
	private Logger log = LoggerFactory.getLogger(SimpleConfidentialityValidator.class.getSimpleName());
    
 	public boolean handleRequest(SOAPMessageContext messageContext) {
		info("Message confidentiality checks will be done for this request. "
			+ "Message fields will be decrypted and set back on the message and "
			+ "the message forwarded to the implementation for processing.");

		// Use the default policy configuration and if symmetric key is being used, the IV present in the request.
		ConversionContext context 		= buildConversionContext(messageContext);

		messageContext.put("Conversion-Context", context);
		log.info("Stored the Conversion Context in the SOAP Message Context for use during the response leg.");
		
		IntimationRequestType locked	= (IntimationRequestType) toEntity(messageContext, IntimationRequestType.class);
	// IntimationRequestType unlocked = IntimationRequestUnwrapperMapper.INSTANCE.unwrap(wrapped);
		
		log.info("Locked Confidential: " + toMessageString(locked, new QName(BasePolicyInterceptor.Namespaces.WRAP, "intimation_request", 
																																		BasePolicyInterceptor.NamespacePrefixes.WRAP)));

		IntimationRequestUnwrapperMapper uwm = IntimationRequestUnwrapperMapper.INSTANCE;
		uwm.setContext(context);
		
		IntimationRequestType unlocked = uwm.unwrap(locked);
		
		log.info("Unlocked Confidential: " + toMessageString(unlocked, new QName(BasePolicyInterceptor.Namespaces.WRAP, "intimation_request", 
																																				BasePolicyInterceptor.NamespacePrefixes.WRAP)));
 
		setNewBusinessPayload(messageContext, 
				new JAXBElement(new QName(BasePolicyInterceptor.Namespaces.WRAP, "intimation_request", 
																		   BasePolicyInterceptor.NamespacePrefixes.WRAP), unlocked.getClass(), unlocked));

		log.info("Message fields decrypted and set in the request. Forwarding it now.");
		return true;
	}

	public boolean handleResponse(SOAPMessageContext messageContext) {
		info("Message confidentiality will be enforced for this response. "
				+ "Message fields will be encrypted and set in the response before forwarding it.");

		// Use the default policy configuration and if symmetric key is being used, the IV present in the request.
	// ConversionContext context 		= buildConversionContext(messageContext);
		ConversionContext context 		= (ConversionContext)messageContext.get("Conversion-Context");
		
		if(context == null) {
			throw new WebServiceException("Confidentiality context not found.");
		}
		
		IntimationResponseType unlocked	= (IntimationResponseType) toEntity(messageContext, IntimationResponseType.class);
	// IntimationResponseType locked = IntimationResponseWrapperMapper.INSTANCE.wrap(unlocked);
		
		log.info("Unlocked Confidential: " + toMessageString(unlocked, new QName(BasePolicyInterceptor.Namespaces.WRAP, "intimation_response", 
																																				BasePolicyInterceptor.NamespacePrefixes.WRAP)));

		IntimationResponseWrapperMapper lwm = IntimationResponseWrapperMapper.INSTANCE;
		lwm.setContext(context);
		
		IntimationResponseType locked = lwm.wrap(unlocked);
	 	log.info("Locked Confidential: " + toMessageString(locked, new QName(BasePolicyInterceptor.Namespaces.WRAP, "intimation_response", 
	 																																	BasePolicyInterceptor.NamespacePrefixes.WRAP)));
	
		setNewBusinessPayload(messageContext, 
				new JAXBElement(new QName(BasePolicyInterceptor.Namespaces.WRAP, "intimation_response", 
						BasePolicyInterceptor.NamespacePrefixes.WRAP), unlocked.getClass(), locked));

	 	switch(context.getIvLocation()) {
	 		case BasePolicyInterceptor.FieldValue.Location.HTTP_HEADER:
	 				setHttpHeader(messageContext, SimpleConfidentialityValidatorConstants.FieldNames.CIPHER_IV, context.getIv());
	 				break;  
	 		case BasePolicyInterceptor.FieldValue.Location.SOAP_HEADER: 
	 			setSOAPHeaderField(messageContext, 	BasePolicyInterceptor.NamespacePrefixes.EXT, 
	 																					BasePolicyInterceptor.Namespaces.EXT, SimpleConfidentialityValidatorConstants.FieldNames.CIPHER_IV, context.getIv());
	 			break;  
	 		case BasePolicyInterceptor.FieldValue.Location.MESSAGE_BODY: 
	 			setSOAPBodyField(messageContext, 	BasePolicyInterceptor.NamespacePrefixes.EXT, 
							BasePolicyInterceptor.Namespaces.EXT, SimpleConfidentialityValidatorConstants.FieldNames.CIPHER_IV, context.getIv());
	 			break;  
	 	}
 	
		log.info("Message fields encrypted and set in the response. Forwarding it now.");
		return true;
	}

	@Override
	protected void initPolicy() {
		setPolicyName("SimpleConfidentialityValidator");
		String policyDefaultsFileName = "policy-defaults/simple-confidentiality-validator-defaults.properties";
		policyDefaults = loadPolicyConfiguration(policyDefaultsFileName);

		if(policyDefaults.isEmpty()) {
			warn("Configurable defaults were not found within the properties file \"" + policyDefaultsFileName + "\". "
					+ "Check if the properties file is on the classpath (within classes of the .war). "
					+ "Validate the relative path, the filename and the read permissions.");

			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_ENCODING, "base64");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_KEY_TYPE, "asymmetric");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_CIPHER, "RSA/ECB/PKCS1Padding");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_PROVIDER_KEY_ID, "acme-rsa-1024");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_PROVIDER_KEY_LENGTH, "1024");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_CONSUMER_KEY_ID, "ixc-rsa-1024");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_CONSUMER_KEY_LENGTH, "1024");
			policyDefaults.setProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_SELECT_FIELDS, "*");

			warn("Defaulting to hard-fixed defaults. [" + policyDefaults + "]");
		}
	}

	private ConversionContext buildConversionContext(SOAPMessageContext messageContext) {
		ConversionContext context 		= null;
		
		String keyType							 	= policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_KEY_TYPE);
		String cipherSpec[] 						= policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_CIPHER).split("/");
		String encoding								= policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_ENCODING);
		
		String cipherAlgo							= cipherSpec[0];
		String cipherAlgoMode				= cipherSpec[1];
		String cipherAlgoPadding			= cipherSpec[2];
		
		log.info(keyType + " key type will be used for request decryption and response encryption.");
		
		if(keyType.equals("symmetric")) {
			log.info("IV will be looked up in the order of HTTP Header, SOAP Header, SOAP Body.");

			String ivEncInHTTPHeader			= getSingleHttpRequestHeader(messageContext, SimpleConfidentialityValidatorConstants.FieldNames.CIPHER_IV);
			String ivEncInSOAPHeader		= getSingleSOAPHeaderFieldValue(messageContext, SimpleConfidentialityValidatorConstants.XPaths.IV_FIELD);
			String ivEncInSOAPBody			= getSingleSOAPBodyFieldValue(messageContext, SimpleConfidentialityValidatorConstants.XPaths.IV_FIELD);
			
			log.info("IV: HTTP Header: " + ivEncInHTTPHeader + "; SOAP Header: " + ivEncInSOAPHeader + "; SOAP Body: " + ivEncInSOAPBody);
			
			String finalIV = null;
			String finalIVLocation = null;
			if (ivEncInHTTPHeader != null) {
				finalIV = ivEncInHTTPHeader;
				log.info("IV within HTTP Header chosen for data decryption / encryption.");
				finalIVLocation = BasePolicyInterceptor.FieldValue.Location.HTTP_HEADER;
			}
			else if (ivEncInSOAPHeader != null) {
				finalIV = ivEncInSOAPHeader;
				log.info("IV within SOAP Header chosen for data decryption / encryption.");
				finalIVLocation = BasePolicyInterceptor.FieldValue.Location.SOAP_HEADER;
			}
			else if (ivEncInSOAPBody != null) {
				finalIV = ivEncInSOAPBody;
				log.info("IV within SOAP Body chosen for data decryption / encryption.");
				finalIVLocation = BasePolicyInterceptor.FieldValue.Location.MESSAGE_BODY;
			}
			
			log.info("IV \"" + finalIV + "\" will be used for data decryption / encryption.");
			
			String keyId = policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_PROVIDER_KEY_ID);
			Key secretKey = findPrivateOrSecretKey(keyId);
			
			context 	= new ConversionContext(secretKey, secretKey, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, encoding, finalIV);
			context.setIvLocation(finalIVLocation);
			
			log.info(">>>>>>>>>> >>>>>>>>>> [SYMMETRIC] Request decryption & response encryption using the symmetric key bound to  \"" + keyId + "\" in the keystore.");
		}
		else { // asymmetric
			String privateKeyId 	= policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_PROVIDER_KEY_ID);
			String publicKeyId 	= policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_CONSUMER_KEY_ID);

			Key receiversPrivateKey				= findPrivateOrSecretKey(privateKeyId);
			Key sendersPublicKey				= findPublicKey(publicKeyId); 				

			context 	= new ConversionContext(sendersPublicKey, receiversPrivateKey, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, encoding, null);
			
			log.info(">>>>>>>>>> >>>>>>>>>> [ASYMMETRIC] Request decryption using private key \"" + privateKeyId + "\" (server end) and "
																									  + "response encryption using public key \"" +publicKeyId + "\" (client / consumer end).");
		}
		
		String fields		= policyDefaults.getProperty(SimpleConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_SELECT_FIELDS);
		context.setFields(fields);
		
		log.info("Conversion Context: " + context);
		return context;
	}

}