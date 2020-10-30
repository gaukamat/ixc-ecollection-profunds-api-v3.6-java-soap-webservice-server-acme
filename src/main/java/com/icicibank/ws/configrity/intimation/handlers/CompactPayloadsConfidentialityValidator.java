package com.icicibank.ws.configrity.intimation.handlers;

import java.io.StringReader;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icicibank.ws.configrity.intimation.util.MessageSecurityHelper;
import com.icicibank.ws.configrity.model.secure.compact.EncryptedType;
import com.icicibank.ws.configrity.model.secure.compact.ObjectFactory;
import com.icicibank.ws.configrity.model.secure.compact.SecureMessageType;
import com.icicibank.ws.configrity.model.wrap.IntimationRequestType;

// FIELD ENCRYPTION / DECRYPTION WILL USE RSA ASYMMETRIC KEYS ONLY. SENDER WILL ENCRYPT 
// USING RECEIVERS PUBLIC KEY. RECEIVER WILL DECRYPT USING THEIR PRIVATE KEY AND VICE-VERSA.

// PAYLOAD ENCRYPTION / DECRYPTION WILL USE AES / DES / 3DES / BLOWFISH SYMMETRIC KEYS 
// THESE ARE CALLED SHARED SECRET. SHARED SECRET IF AUTO-GENERATED PER REQUEST WILL
// BE SENT IN THE REQUEST AFTER IT IS ITSELF ENCRYPTED USING THE RECEIVERS PUBLIC KEY.
// LIKEWISE APPLIES TO THE RESPONSE.

	public class CompactPayloadsConfidentialityValidator extends BasePolicyInterceptor 
	implements SOAPHandler<SOAPMessageContext> {
	
	private static final String ENCODED_IV_KEY = "Payload-IV-Encoded";
	private static final String MESSAGE_SECURITY_CONTEXT_KEY = "Message-Confidentiality-Properties";

	public CompactPayloadsConfidentialityValidator() {
		super();
	}
	
	private static interface PayloadConfidentialityValidatorConstants {
		interface ConfigurationParameters {
			String SECURE_ENVELOPE_VERSION						= "secure-envelope-version";		 				// compact-v3
			String SECURE_ENVELOPE_REQUEST_NAME			= "secure-envelope-request-name";			// intimation_request
			String SECURE_ENVELOPE_RESPONSE_NAME		= "secure-envelope-response-name";			// intimation_response
			String PAYLOAD_ENCRYPTION_CIPHER 					= "payload-encryption-cipher";		 				// DESede/CBC/PKCS5Padding
			String PAYLOAD_ENCRYPTION_KEY_ALIAS 			= "payload-encryption-key-alias"; 				// acme-3des-168
			String SECRETKEY_ENCRYPTION_CIPHER				= "secretkey-encryption-cipher"; 					// RSA/ECB/PKCS1Padding
			String SECRETKEY_DECRYPTION_KEY_ALIAS 		= "secretkey-decryption-key-alias"; 			// acme-rsa-1024
			String SECRETKEY_ENCRYPTION_KEY_ALIAS 		= "secretkey-encryption-key-alias"; 			// ixc-rsa-1024
			String ENCRYPTION_ENCODING 								= "encryption-encoding"; 								// base64
			String ENCRYPTION_KEY_TYPE								= "encryption-key-type";
		}
		
		interface Type {
		}

		interface HttpHeaders {
		}
		
		interface XPaths {
		}
	}
	
	private Logger log = LoggerFactory.getLogger(CompactPayloadsConfidentialityValidator.class.getSimpleName());
    
 	public boolean handleRequest(SOAPMessageContext messageContext) {
		info("Payload confidentiality checks will be done for this request. "
				+ "Message will be decrypted (\"encrypted_payload\") and set back on the message (\"clear_payload\") and "
				+ "the message forwarded to the implementation for processing.");
		String messageVersion = policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECURE_ENVELOPE_VERSION);

		Properties requestContextProperties  =  new Properties();
		
		if(!messageVersion.equals("compact-v3")) {
			throw new WebServiceException("Unsupported message version. Check the Policy Configuration.");
		}
		
		SecureMessageType intimationRequest = (SecureMessageType)toEntity(messageContext, SecureMessageType.class);

		EncryptedType encryptedBusinessPayload = intimationRequest.getEncryptedPayload();
		String encryptedRequest = encryptedBusinessPayload.getValue();

		log.info("Encrypted Payload: [" + encryptedRequest + "]");

		String finalPCipher = encryptedBusinessPayload.getCipher();

		if(finalPCipher == null) {
			// no value set in the request, fallback on policy defaults.
			finalPCipher = policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_CIPHER);
		}
		// Build the confidentiality context.
		requestContextProperties.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_CIPHER, finalPCipher);
		
		String finalEncoding = encryptedBusinessPayload.getEncoding();
		if(finalEncoding == null) {
			// no value set in the request, fallback on policy defaults.
			finalEncoding =  policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_ENCODING);
		}
		// Build the confidentiality context.
		requestContextProperties.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_ENCODING, finalEncoding);
		
		String encodedIV = encryptedBusinessPayload.getIv();
		if(encodedIV == null) {
			throw new WebServiceException("IV not found. IV is used with the symmteric key to encrypt/decrypt the business message payload.");
		}
		// Build the confidentiality context.
		requestContextProperties.setProperty(ENCODED_IV_KEY, encodedIV);
	
		String payloadEncryptionKeyId = encryptedBusinessPayload.getKeyId();
		if(payloadEncryptionKeyId == null) {
			// no value set in the request, fallback on policy defaults.
			payloadEncryptionKeyId = policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_KEY_ALIAS);
		}
		// Build the confidentiality context.
		requestContextProperties.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_KEY_ALIAS, payloadEncryptionKeyId);
		
		// Ignoring the IV and Encoding attributes for the Encrypted Key for the following reasons.
		// 		Secret Key used to encrypt the payload will always be encrypted with a pre-shared asymmetric key and no IV is needed for asymmetric keys.
		// 		Encoding is already taken care of when handling the payload. A single encoding is assumed throughout.
		
		// Key used to encrypt the secret key. Required only if the secret key was not pre-shared.
		EncryptedType encryptedKey = intimationRequest.getEncryptedKey();
		String[] pCipherSpec = finalPCipher.split("/");

		if(encryptedKey == null || encryptedKey.getValue() == null) {
			log.info("Confidentiality-Context: " + requestContextProperties);

			// Pre-shared between the provider and the consumer, kept in the keystore.  ("static")
			log.warn("Secret token (symmetric) not found in the request. Pre-shared symmetric key \"" + payloadEncryptionKeyId + "\" is being used for encryption. ");
			try {
				Key payloadSymmKey = getWebServicesKeyStore().getKey(payloadEncryptionKeyId, CONFIGRITY_KEY_PWD.toCharArray());
				String clearPayload = MessageSecurityHelper.decryptWithKey(payloadSymmKey, 
							encryptedRequest, finalEncoding, 
							pCipherSpec[0],pCipherSpec[1],pCipherSpec[2],
							MessageSecurityHelper.encode(MessageSecurityHelper.decode(encodedIV, finalEncoding), "hex"));
				intimationRequest.setClearPayload(clearPayload);
				
				log.info("Payload decrypted using a pre-shared symmetric key.");
			} 
			catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
				throw new WebServiceException("Unable decrypt the business message payload.", e);
			}
		}
		else {
			// Not pre-shared. New key for every request. ("runtime")
			log.warn("Secret token (symmetric) found in the request. Pre-shared (configured) symmetric key \"" + payloadEncryptionKeyId + "\" will not be used even if provided. ");
			payloadEncryptionKeyId = null;
			requestContextProperties.remove(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_KEY_ALIAS);

			String finalKCipher = encryptedKey.getCipher();
			if(finalKCipher == null) {
				// no value set in the request, fallback on policy defaults.
				finalKCipher = policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_CIPHER);
			}
			// Build the confidentiality context.
			requestContextProperties.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_CIPHER, finalKCipher);
			
			String[] kCipherSpec = finalKCipher.split("/");
			String finalDKeyId = encryptedKey.getKeyId();
			String finalEKeyId = (finalDKeyId != null ? finalDKeyId.replace("acme", "ixc"): null);
			
			if(finalDKeyId == null) {
				// no values set in the request, fallback on policy defaults.
				finalDKeyId = policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_DECRYPTION_KEY_ALIAS);
				finalEKeyId = policyDefaults.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_KEY_ALIAS);
			}
			
			// Build the confidentiality context.
			requestContextProperties.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_DECRYPTION_KEY_ALIAS, finalDKeyId);
			requestContextProperties.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_KEY_ALIAS, finalEKeyId);

			log.info("Confidentiality-Context: " + requestContextProperties);

			try {
				Key privateAsymmKey = getWebServicesKeyStore().getKey(finalDKeyId, CONFIGRITY_KEY_PWD.toCharArray());
				log.info("Private key bound to \"" + finalDKeyId + "\" Public-Private Key Pair will be used to decrypt the symmetric secret key.");
				
				byte[] symmetricSecretKey = MessageSecurityHelper.decryptRaw(privateAsymmKey, encryptedKey.getValue(), 
																	 finalEncoding, kCipherSpec[0], kCipherSpec[1], kCipherSpec[2], null);

				Key payloadSymmKey = new SecretKeySpec(symmetricSecretKey, 0, symmetricSecretKey.length, pCipherSpec[0]);

				String clearPayload = MessageSecurityHelper.decryptWithKey(payloadSymmKey, 
														encryptedRequest, finalEncoding, 
														pCipherSpec[0],pCipherSpec[1],pCipherSpec[2],
														MessageSecurityHelper.encode(MessageSecurityHelper.decode(encodedIV, finalEncoding), "hex"));
					
				intimationRequest.setClearPayload(clearPayload);
			} 
			catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
				throw new WebServiceException("Unable decrypt the secret key and hence the business message payload.", e);
			}
		}
		
		// We have a clear payload with us.
		log.info("Clear Payload: [" +  intimationRequest.getClearPayload() + "]");
		log.info("Converting clear payload into IntimationRequest wrapper type.");
		try {
			JAXBElement<IntimationRequestType> clearIntimationRequest = 
					getXmlBindingContext().createUnmarshaller().unmarshal(
								new StreamSource(new StringReader(intimationRequest.getClearPayload())), IntimationRequestType.class);
			
			setNewBusinessPayload(messageContext, clearIntimationRequest);
			log.info("Clear payload converted into IntimationRequest wrapper type and set as the new business payload.");
		} 
		catch (JAXBException e) {
			throw new WebServiceException("Unable to process the clear business message payload.", e);
		}
		
		messageContext.put(MESSAGE_SECURITY_CONTEXT_KEY, requestContextProperties);
		return true;
	}

 	public boolean handleResponse(SOAPMessageContext messageContext) {
		info("Payload confidentiality enforced for this response. "
				+ "Clear message (\"clear_payload\")  will be encrypted and set in the response (\"encrypted_response\") before forwarding.");

		Properties requestContextProperties = (Properties)messageContext.get(MESSAGE_SECURITY_CONTEXT_KEY);
		if(requestContextProperties == null) {
			throw new WebServiceException("Unable to determine the message security context to be applied to the response message.");
		}
		
		String clearPayload = toMessageBodyString(messageContext);
		log.info("Clear Payload: [" + clearPayload + "]");
		
		ObjectFactory of = new ObjectFactory();

		SecureMessageType intimationResponse = of.createSecureMessageType();
		intimationResponse.setClearPayload(clearPayload);

		EncryptedType encryptedBusinessPayload = of.createEncryptedType();
		intimationResponse.setEncryptedPayload(encryptedBusinessPayload);

		String  finalEncoding	= requestContextProperties.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_ENCODING);

		String  finalPCipher	= requestContextProperties.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_CIPHER);
		String [] finalPCipherSpec = finalPCipher.split("/");
		
		String  finalKCipher 	= requestContextProperties.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_CIPHER);
		String [] finalKCipherSpec = finalKCipher.split("/");

		// Public key of the API consumer.
		Key finalKEKey;
		try {
			finalKEKey = getWebServicesKeyStore().getCertificate(
														requestContextProperties.getProperty(PayloadConfidentialityValidatorConstants.
																										ConfigurationParameters.SECRETKEY_ENCRYPTION_KEY_ALIAS)).getPublicKey();
		} 
		catch (KeyStoreException kse) {
			throw new WebServiceException("Unable to encrypt the response.  Public key of the API consumer not found.", kse);
		}

		log.info("Public key of the API consumer will be used to encrypt the symmetric key if its not a pre-shared one. "
				+ "If the symmetric key is pre-shared, it wont be passed in the message and hence no question of encrypting it."); 
		
		String finalPEKeyId = requestContextProperties.getProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_KEY_ALIAS);
		Key finalPEKey 		= null;

		// Recycling IV: You can create a new one if you wish.
		String  encodedIV 	= requestContextProperties.getProperty(ENCODED_IV_KEY);
		
		if(finalPEKeyId == null) { // Not pre-shared. New key for every request. ("runtime")
			log.info("Not pre-shared. New symmetric key is created for every request. (\"runtime\") and "
					+ "sent to the API consumer in the message payload, encrypted (using API consumer public key) & encoded. ");
			
			Long skl = MessageSecurityHelper.getKeySize(finalKCipherSpec[0]);
			finalPEKey = MessageSecurityHelper.genKey(finalKCipherSpec[0], skl);

			info("[RUNTIME SECRET KEY]: " + skl +" bits runtime symmetric key for " + finalKCipherSpec[0] + " cipher created.");
			
			String encryptedKey = MessageSecurityHelper.encryptEncodedMessageWithKey(finalKEKey, 			// public key of the API consumer
													MessageSecurityHelper.encode(finalPEKey.getEncoded(), finalEncoding),		// secret key is the very thing we are encrypting
													finalKCipherSpec[0], finalKCipherSpec[1], finalKCipherSpec[2], finalEncoding, null, null);
			
			
			info("Encrypted & encoded symmetric key to be used for payload encryption: [" + encryptedKey + "]");
			
			EncryptedType encryptedSymmKey = of.createEncryptedType();
			encryptedSymmKey.setValue(encryptedKey);

			encryptedSymmKey.setCipher(finalKCipher);			// asymmetric key cipher
			encryptedSymmKey.setEncoding(finalEncoding);		// single encoding across the message
			encryptedSymmKey.setIv(null);		// the symmetric key is encrypted using public asymm key and does not need an IV. 

			intimationResponse.setEncryptedKey(encryptedSymmKey);
		}
		else {
			// Pre-shared between the provider and the consumer, kept in the keystore.  ("static")
			log.info("Pre-shared symmetric key will be used to encrypt the payload. Since its pre-shared, it will NOT be sent to the API consumer. ");
			
			try {
				finalPEKey = getWebServicesKeyStore().getKey(finalPEKeyId, CONFIGRITY_KEY_PWD.toCharArray());
				
				if(finalPEKey == null) {
					throw new UnrecoverableKeyException("No key found bound to " + finalPEKeyId + ".");
				}
				
				info("Symmetric key bound to  " + finalPEKeyId + " found and will be used to encrypt the response.");
			} 
			catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
				severe("Unable to encrypt the response. [Root Cause: " + e + "]");
				throw new WebServiceException("Unable to encrypt the response. Pre-shared symmetric key not found.",  e);
			}

			encryptedBusinessPayload.setKeyId(finalPEKeyId);
		}
	
		log.info("Encrypting and encoding business payload.");
		
		String securePayload = MessageSecurityHelper.encryptWithKey(finalPEKey, clearPayload, 
					finalPCipherSpec[0], finalPCipherSpec[1], finalPCipherSpec[2], finalEncoding, null, 
					MessageSecurityHelper.encode(MessageSecurityHelper.decode(encodedIV, finalEncoding), "hex"));
		
		info("Response encrypted & encoded.");

		encryptedBusinessPayload.setValue(securePayload);
		encryptedBusinessPayload.setCipher(finalPCipher);	// symmetric key cipher
		encryptedBusinessPayload.setEncoding(finalEncoding);
		encryptedBusinessPayload.setIv(encodedIV);		// IV used to encrypt the business message payload.
	
		intimationResponse.setIv(encodedIV);		// IV used to encrypt the business message payload.
		
		// We have a clear payload with us.
		log.info("Secure Payload: [" +  intimationResponse.getClearPayload() + "]");

		JAXBElement<SecureMessageType> compactV3IntimationResponse = new JAXBElement<>(
				new QName(BasePolicyInterceptor.Namespaces.COMPACT, "intimation_response", 
										BasePolicyInterceptor.NamespacePrefixes.COMPACT), 
				SecureMessageType.class, intimationResponse);
		
		setNewBusinessPayload(messageContext, compactV3IntimationResponse);
		log.info("Clear payload converted into IntimationRequest wrapper type and set as the new business payload.");
		
		return true;
	}

	@Override
	protected void initPolicy() {
		setPolicyName("PayloadConfidentialityValidator");
		String policyDefaultsFileName = "policy-defaults/payload-confidentiality-validator-defaults.properties";
		policyDefaults = loadPolicyConfiguration(policyDefaultsFileName);

		if(policyDefaults.isEmpty()) {
			warn("Configurable defaults were not found within the properties file \"" + policyDefaultsFileName + "\". "
					+ "Check if the properties file is on the classpath (within classes of the .war). "
					+ "Validate the relative path, the filename and the read permissions.");

			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECURE_ENVELOPE_VERSION, 					"compact-v3");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECURE_ENVELOPE_REQUEST_NAME, 		"intimation_request");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECURE_ENVELOPE_RESPONSE_NAME, 	"intimation_response");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_CIPHER, 				"DESede/CBC/PKCS5Padding");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.PAYLOAD_ENCRYPTION_KEY_ALIAS, 		"acme-3des-168");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_CIPHER, 			"RSA/ECB/PKCS1Padding");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_DECRYPTION_KEY_ALIAS, 	"acme-rsa-1024");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.SECRETKEY_ENCRYPTION_KEY_ALIAS, 	"ixc-rsa-1024");
			policyDefaults.setProperty(PayloadConfidentialityValidatorConstants.ConfigurationParameters.ENCRYPTION_ENCODING, 							"base64");
			
			warn("Defaulting to hard-fixed defaults. [" + policyDefaults + "]");
		}
	}

}