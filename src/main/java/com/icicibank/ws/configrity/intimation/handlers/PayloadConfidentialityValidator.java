package com.icicibank.ws.configrity.intimation.handlers;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	public class PayloadConfidentialityValidator extends BasePolicyInterceptor 
	implements SOAPHandler<SOAPMessageContext> {
	
	public PayloadConfidentialityValidator() {
		super();
	}
	
	private static interface PayloadConfidentialityValidatorConstants {
		interface ConfigurationParameters {
		}
		
		interface Type {
		}

		interface HttpHeaders {

		}
		interface XPaths {
		}
	}
	
	private Logger log = LoggerFactory.getLogger(PayloadConfidentialityValidator.class.getSimpleName());
    
 	public boolean handleRequest(SOAPMessageContext messageContext) {
		info("Payload confidentiality checks will be done for this request. "
				+ "Message will be decrypted (\"encrypted_payload\") and set back on the message (\"clear_payload\") and "
				+ "the message forwarded to the implementation for processing.");
		return true;
	}

 	public boolean handleResponse(SOAPMessageContext messageContext) {
		info("Payload confidentiality enforced for this response. "
				+ "Clear message (\"clear_payload\")  will be encrypted and set in the response (\"encrypted_response\") before forwarding.");
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

			warn("Defaulting to hard-fixed defaults. [" + policyDefaults + "]");
		}
	}

}