
package com.icici.xpress_connect.ws.ecollection.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

// https://javaee.github.io/metro-jax-ws/doc/user-guide/ch03.htm
public class IntimationPortMessageHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Logger log = Logger.getLogger(IntimationPortMessageHandler.class.getName());
    private static final String MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_ALGO = "ACME-Message-Checksum-Algo";
    private static final String MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_MESSAGE = "ACME-Message-Checksum";
    private static final String MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_FIELDS = "ACME-Message-Field-Checksum";

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		log.info("Message intercepted.");
		
		
		Boolean outBoundProperty = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		Map<String, List<String>> headers = null;
		log.info("Outbound Message ? " + outBoundProperty);
		try {
			if(outBoundProperty) {
				log.info("[IBM]: Outbound message: ");
		         headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_RESPONSE_HEADERS);
			}
			else {
				log.info("[IBM]: Inbound message: ");
		         headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
			}
	       
	         log.info("[IBM]: HTTP Headers : " + headers);
	         ByteArrayOutputStream baos = new ByteArrayOutputStream();

	         SOAPMessage smo = context.getMessage();
	         smo.writeTo(baos);
			
			String message= baos.toString();
			
			log.info("[IBM]: \n" + message);
			log.info("\n[IBM]: \n");

			if(outBoundProperty) {
				log.info("[IBM]: Outbound message: ");
		         headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_RESPONSE_HEADERS);

		         if(headers == null) {
	         		log.info("Unable to obtain HTTP response headers. Creating one.");
		        	 headers = new HashMap<String, List<String>>();
		        	 context.put(MessageContext.HTTP_RESPONSE_HEADERS, headers);
		         }
		         
		         byte[] data = message.getBytes();
		         
		         headers.put(MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_ALGO, Arrays.asList("SHA-512"));
		         headers.put(MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_MESSAGE, Arrays.asList(hash("SHA-512", data)));
		         
			}
			else {
				log.info("[IBM]: Inbound message: ");
		         headers = (Map<String, List<String>>) context.get(MessageContext.HTTP_REQUEST_HEADERS);
			
		         	if(headers == null) {
		         		log.info("Unable to obtain HTTP request headers. Checksum cannot be computed.");
		         	}
		         	else {

						String checksumAlgo = null;
						String messageChecksum = null;
						String contentType = null;
						
						if(headers.get(MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_ALGO) != null) {
							checksumAlgo = headers.get(MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_ALGO).get(0);
						}
						if(headers.get(MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_MESSAGE) != null) {
							messageChecksum = headers.get(MESSAGE_INTEGRITY_FIELDNAME_CHECKSUM_MESSAGE).get(0);
						}
						if(headers.get("Content-Type") != null) {
							contentType = headers.get("content-type").get(0);
						}
						
						if(checksumAlgo == null || messageChecksum == null) {
			         		log.info("Checksum algo and checksum value not found. Checksum cannot be computed.");
			         		return true;
						}
						
						SOAPBody soapBody = smo.getSOAPBody();
						Node soapBodyDoc = soapBody.getFirstChild();
						
						StringWriter sw = new StringWriter();

						TransformerFactory tf = TransformerFactory.newInstance();
						Transformer transformer = tf.newTransformer();
						transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						transformer.setOutputProperty(OutputKeys.METHOD, "xml");
						transformer.transform(new DOMSource(soapBodyDoc), new StreamResult(sw));
						
						if(sw.getBuffer() == null || sw.getBuffer().length() == 0) {
							log.info("No checksum validation.");
						}
						else {
							String chkMessage = sw.getBuffer().toString();

							log.info("\nMessage payload for checksum calculation : \n[" + chkMessage + "]\n\n");
							
							log.info("*** Computing checksum using " + checksumAlgo + " and comparing.");
							log.info("*** \t Received: " + messageChecksum);
							
							String messageChecksumComputed = hash(checksumAlgo, chkMessage.getBytes());
							 
							log.info("*** \t Computed: " + messageChecksumComputed);
							
							if(messageChecksum != null) {
								log.info("*** \t Checksums match ? " + messageChecksum.equals(messageChecksumComputed));
							}
						}
		         	}
			}
		} 
		catch (SOAPException | IOException | TransformerException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	private String hash(String algo, byte[] data) {
		String hashValue = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algo, new BouncyCastleProvider());
			byte[] hash = md.digest(data);
			hashValue = Hex.encodeHexString(hash);
		} 
		catch (NoSuchAlgorithmException nsae) {
			log.severe("Failed to hash data. No such algorithm. [Root Cause: " + nsae + "]");
		}
		return hashValue;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		log.info("[IBM]: Message context closed.");
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
