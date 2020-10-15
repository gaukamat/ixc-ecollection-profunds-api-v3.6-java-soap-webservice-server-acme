package com.icicibank.ws.configrity.intimation.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.parser.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icicibank.ws.configrity.intimation.util.MessageSecurityHelper;
import com.icicibank.ws.configrity.intimation.util.StringUtil;

public class IntegrityValidator extends BasePolicyInterceptor 
	implements SOAPHandler<SOAPMessageContext> {
	
	private static interface IntegrityValidatorConstants {
		interface ConfigurationParameters {
			String HASHING_ALGO 														= "hashing-algo";
			String HASHING_STRATEGY 												= "hashing-strategy";
			String HASHING_STRATEGY_FIELD_SEPERATOR 		= "hashing-strategy-param-field-seperator";
			String HASHING_STRATEGY_FIELD_NAME_CASE 		= "hashing-strategy-param-field-name-case";
			String HASH_LOCATION 													= "hash-location";
			String REMOVE_XML_DECLARATION_INDICATOR 		= "remove-xml-declaration";
			String LENIENT_CHECKSUM_INDICATOR 						= "lenient-checksum";
			String HASHING_ALGO_FIELD_NAME 							= "hashing-algo-field-name";
			String HASH_FIELD_NAME 												= "hash-field-name";
			String HASHING_STRATEGY_FIELD_NAME_PREFIX 	= "hashing-strategy-param-field-name-prefix";
			String XML_C14N_ALGO 													= "cannonical-xml-algo";
		}
		
		interface Type {
			String HASHING_STRATEGY_MESSAGE_PAYLOAD 					= "message-payload";
			String HASHING_STRATEGY_FIELD_CONCATENATION 			= "message-field-concatenation";
			String HASHING_STRATEGY_FIELD_NAME_PREFIX_DROP 	= "drop";
			String HASHING_STRATEGY_FIELD_NAME_PREFIX_KEEP 	= "keep";
		}
		
		interface IntegrityFields {
			String HASHING_ALGO_FIELD_NAME 	= "Message-Checksum-Algo";
			String HASH_FIELD_NAME 						= "Message-Checksum";
		}

		interface XPaths {
			String HASHING_ALGO_FIELD_NAME 	= "acmex:Message-Checksum-Algo";
			String HASH_FIELD_NAME 						= "acmex:Message-Checksum";
		}
	}
	
	private Logger log = LoggerFactory.getLogger(IntegrityValidator.class.getSimpleName());
    
 	public boolean handleRequest(SOAPMessageContext messageContext) {
		info("Message integrity checks will be done for this request. Checksum will be calculated and verified with the one in the message.");

		String hashingStrategy 	= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY);
		String hashLocation 		= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASH_LOCATION);
		String hashingAlgo 			= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_ALGO);

		log.info("Request checksum will be calculated over " + hashingStrategy + " using "  + hashingAlgo + 
				" and will be set in the " + hashLocation + " for " + IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME + " field.");
		
		String actualChecksum 		= null;
		String receivedChecksum 	= null;
		String checksumString 		= null;
		
		if(IntegrityValidatorConstants.Type.HASHING_STRATEGY_MESSAGE_PAYLOAD.equals(hashingStrategy)) {
			checksumString = findPayloadChecksumString(messageContext);
		}
		else if(IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_CONCATENATION.equals(hashingStrategy)) {
			checksumString = findFieldChecksumString(messageContext);
		}
		else {
			throw new WebServiceException("Message integrity checks failed. Hashing can either be \"" + 
					IntegrityValidatorConstants.Type.HASHING_STRATEGY_MESSAGE_PAYLOAD + "\" or \"" + 
					IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_CONCATENATION + 
					"\". Check policy configuration.");
		}
		
		log.info("Checksum will be calculated over [" + checksumString + "]");
		
		actualChecksum = MessageSecurityHelper.hash(hashingAlgo, checksumString.getBytes());
		
		if(BasePolicyInterceptor.FieldValue.Location.HTTP_HEADER.equals(hashLocation)) {
			receivedChecksum = getSingleHttpRequestHeader(messageContext, IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME);
		}
		else if(BasePolicyInterceptor.FieldValue.Location.SOAP_HEADER.equals(hashLocation)) {
			receivedChecksum = getSingleSOAPHeaderFieldValue(messageContext, IntegrityValidatorConstants.XPaths.HASH_FIELD_NAME);
		}
		else if(BasePolicyInterceptor.FieldValue.Location.MESSAGE_BODY.equals(hashLocation)) {
			receivedChecksum = getSingleSOAPBodyFieldValue(messageContext, IntegrityValidatorConstants.XPaths.HASH_FIELD_NAME);
		}

		Boolean checksumMatch = Boolean.FALSE;
		
		if(receivedChecksum != null && actualChecksum != null && 
			actualChecksum.equals(receivedChecksum)) {
			checksumMatch = Boolean.TRUE;
		}
		
		log.info("Comparing actual checksum \"" +actualChecksum + "\" with received checksum \"" + receivedChecksum + "\".  Matches ? " + checksumMatch);
		
		Boolean showLeniency = Boolean.parseBoolean(policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.LENIENT_CHECKSUM_INDICATOR));
		
		if(showLeniency) {
			log.info("In leniency mode.  Checksum mismatch will be pardoned.");
		}
		
		if(checksumMatch || showLeniency) {
			return true;
		}
		else {
			throw new WebServiceException("Message integrity compromised."
					+ " Actual checksum \"" + actualChecksum + "\" does not match with the received checksum \"" + receivedChecksum + "\".");
		}
	}

 	private String findFieldChecksumString(SOAPMessageContext messageContext) {
		log.warn("Checksum will be computed over concatenated message field value pairs.");

		String delimiter 							= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY_FIELD_SEPERATOR);
		String keyCase 								= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY_FIELD_NAME_CASE);
		String checksumFieldName 		= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASH_FIELD_NAME);
		Boolean dropNSPrefix 				= (policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.
					HASHING_STRATEGY_FIELD_NAME_PREFIX).
				equals(IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_NAME_PREFIX_DROP) ? Boolean.TRUE : Boolean.FALSE);
		
		String  message = toMessageBodyString(messageContext);
		log.info("Checksum string will be created using the business message: [" + message + "]");

		String checksumString = StringUtil.concatenateXmlFieldValuePairs(message, ":", delimiter, checksumFieldName, keyCase, dropNSPrefix);
		
		log.info("[CHECKSUM-OVER-CONCAT-MESSAGE-FIELD-VALUE-PAIRS]: Checksum should be computed over : [" + checksumString + "]");
		
		return checksumString;
	}

	private String findPayloadChecksumString(SOAPMessageContext messageContext) {
		log.warn("Checksum will be computed over the business message. Checksum field will be removed if it is present within the business message.");

		boolean removeXMLDeclarationIndicator 		= Boolean.parseBoolean(policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.
																							REMOVE_XML_DECLARATION_INDICATOR, "true"));

		String  message = toMessageBodyString(messageContext);
		log.info("Checksum string will be created using the business message: [" + message + "]");

	// String checksumFieldName 		= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASH_FIELD_NAME);
		String checksumFieldName 		= IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME;
		
		String regexPatternStr 				= "\\<(([A-Za-z0-9\\-_]+:){0,1}" + checksumFieldName + ")([^\\<\\>]*)\\>([^\\<\\>]*)\\</\\1\\>";
		log.info("Reg expression \"" + regexPatternStr + "\" will be used to remove the checksum field if present within the business payload.");
		message = StringUtil.replaceAll(message, regexPatternStr, "");

		if(removeXMLDeclarationIndicator) {
			regexPatternStr = "\\<\\?xml.*\\?\\>";
			log.info("Reg expression \"" + regexPatternStr + "\" will be used to remove the XML Declaration if present within the business payload.");
			message = StringUtil.replaceAll(message, regexPatternStr, "");
		}

		log.info("Pre-processed XML:  [" + message + "]");
	
		String canonicalAlgo = policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.XML_C14N_ALGO);
		// Option 1:
		// message = MessageSecurityHelper.canonicalizeXml(message, canonicalAlgo, null, null);
		
		// Option 2:
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Canonicalizer c14n = Canonicalizer.getInstance(canonicalAlgo);
			c14n.canonicalize(message.getBytes(), baos, false);
			message = baos.toString();
		} 
		catch (InvalidCanonicalizerException | XMLParserException | CanonicalizationException | IOException e) {
			throw new WebServiceException("Unable to calculate the checksum on the message. Canonicalization failed.", e);
		}
		
		log.info("[CHECKSUM-OVER-PAYLOAD]: Canonical XML  : [" + message + "]");
		return message;
	}

	public boolean handleResponse(SOAPMessageContext messageContext) {
		info("Message integrity enforced for this response. Checksum will be calculated and set in the response.");
		
		String hashingStrategy 	= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY);
		String hashLocation 		= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASH_LOCATION);
		String hashingAlgo 			= policyDefaults.getProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_ALGO);

		log.info("Response checksum will be calculated over " + hashingStrategy + " using "  + hashingAlgo + 
				" and will be set in the " + hashLocation + " for " + IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME + " field.");

		String actualChecksum 		= null;
		String checksumString 		= null;
		
		if(IntegrityValidatorConstants.Type.HASHING_STRATEGY_MESSAGE_PAYLOAD.equals(hashingStrategy)) {
			checksumString = findPayloadChecksumString(messageContext);
		}
		else if(IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_CONCATENATION.equals(hashingStrategy)) {
			checksumString = findFieldChecksumString(messageContext);
		}
		else {
			throw new WebServiceException("Message integrity checks failed. Hashing can either be \"" + 
					IntegrityValidatorConstants.Type.HASHING_STRATEGY_MESSAGE_PAYLOAD + "\" or \"" + 
					IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_CONCATENATION + 
					"\". Check policy configuration.");
		}
		
		log.info("Checksum will be calculated over [" + checksumString + "]");
		
		actualChecksum = MessageSecurityHelper.hash(hashingAlgo, checksumString.getBytes());
		log.info("Actual checksum is " + actualChecksum);
		
		if(BasePolicyInterceptor.FieldValue.Location.HTTP_HEADER.equals(hashLocation)) {
			setHttpHeader(messageContext, IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME, actualChecksum);
		}
		else if(BasePolicyInterceptor.FieldValue.Location.SOAP_HEADER.equals(hashLocation)) {
			setSOAPHeaderField(messageContext, "mz", BasePolicyInterceptor.Namespaces.EXT, IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME, actualChecksum);
		}
		else if(BasePolicyInterceptor.FieldValue.Location.MESSAGE_BODY.equals(hashLocation)) {
			setSOAPBodyField(messageContext, "mz", BasePolicyInterceptor.Namespaces.EXT, IntegrityValidatorConstants.IntegrityFields.HASH_FIELD_NAME, actualChecksum);
		}

		return true;
	}

	@Override
	protected void initPolicy() {
		setPolicyName("IntegrityValidator");
		String policyDefaultsFileName = "policy-defaults/integrity-validator-defaults.properties";
		policyDefaults = loadPolicyConfiguration(policyDefaultsFileName);

		if(policyDefaults.isEmpty()) {
			warn("Configurable defaults were not found within the properties file \"" + policyDefaultsFileName + "\". "
					+ "Check if the properties file is on the classpath (within classes of the .war). "
					+ "Validate the relative path, the filename and the read permissions.");
			
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_ALGO, "MD5");
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY, 
															 IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_CONCATENATION);
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY_FIELD_NAME_CASE, "upper");
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY_FIELD_SEPERATOR, "{}");
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASH_LOCATION, 
																	BasePolicyInterceptor.FieldValue.Location.SOAP_HEADER);
			
			// policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.REMOVE_XML_DECLARATION_INDICATOR, "true");
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.LENIENT_CHECKSUM_INDICATOR, "false");
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_ALGO_FIELD_NAME, "Message-Checksum-Algo");
			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASH_FIELD_NAME, 	"Message-Checksum");

			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.HASHING_STRATEGY_FIELD_NAME_PREFIX, 	
					IntegrityValidatorConstants.Type.HASHING_STRATEGY_FIELD_NAME_PREFIX_DROP);

			policyDefaults.setProperty(IntegrityValidatorConstants.ConfigurationParameters.XML_C14N_ALGO, "http://www.w3.org/2001/10/xml-exc-c14n#");

			warn("Defaulting to hard-fixed defaults. [" + policyDefaults + "]");
		}
	}

}