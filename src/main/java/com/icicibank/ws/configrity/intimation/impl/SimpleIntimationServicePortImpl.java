package com.icicibank.ws.configrity.intimation.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icicibank.ws.configrity.intimation.SimpleIntimationServicePort;
import com.icicibank.ws.configrity.intimation.util.APIHelper;
import com.icicibank.ws.configrity.model.IntimationRequestType;
import com.icicibank.ws.configrity.model.IntimationResponseType;
import com.icicibank.ws.configrity.model.ObjectFactory;


//		Client <--> SOAP / HTTP Listener (Servlet Connector) <--> SOAP Message Handlers <--> Port Implementation
//		SOAP Message Handlers:
//			- Message Auditor
//			- Basic Authenticator
//			- Custom Authenticator
//			- Integrity Validator (HTTP Header -> SOAP Header -> SOAP Body)
//			- Confidentiality Handler (Decrypt ->, <- Encrypt)
// 					- Fields			(Wrap Encrypted <-> Wrap Clear)
// 					- Payload		(Encrypted <-> Clear)
//		Port Implementation:
//			- Simple: 		Parse, evaluate, return response
//			- Wrap: 			Map Struct to Simple and delegate to Simple
//			- Compact: 	Parse clear payload, delegate to Simple
//

@WebService(endpointInterface = "com.icicibank.ws.configrity.intimation.SimpleIntimationServicePort")
@HandlerChain(file = "handler-chain-simple.xml")
// @HandlerChain(file = "handler-chain-common.xml")
public class SimpleIntimationServicePortImpl implements SimpleIntimationServicePort {

	private DatatypeFactory dtf 	= null;
	private APIHelper apiHelper 	= null;
	
	private final Logger log 	= 
			LoggerFactory.getLogger(SimpleIntimationServicePortImpl.class.getSimpleName());

	// com.icicibank.ws.configrity.model.
	private ObjectFactory 	modelOF	 = 
			new com.icicibank.ws.configrity.model.ObjectFactory();

	public SimpleIntimationServicePortImpl() {
		super();
		try {
			apiHelper = new APIHelper();
			dtf = DatatypeFactory.newInstance();
		} 
		catch (InstantiationException | DatatypeConfigurationException e) {
			log.warn("Configuration failure(s). Service implementation will not respond as desired.", e);
		}
	}

	@Override
	public IntimationResponseType intimate(IntimationRequestType intimationRequest) {
		log.info("Processing intimation request.");
		
		log.info("Request DateTime Fields: [Request: " + intimationRequest.getRequestDatetime() + 
				", Original Request: " + intimationRequest.getOriginalRequestDatetime() + 
				", Transaction: " + intimationRequest.getTransactionDatetime() + "]");
		
		IntimationResponseType intimationResponse = modelOF.createIntimationResponseType();
		
		Mapper mapper = new DozerBeanMapper();
		mapper.map(intimationRequest, intimationResponse);
		
		intimationResponse.setResponseId(UUID.randomUUID().toString().toUpperCase());
		
		GregorianCalendar now = (GregorianCalendar)GregorianCalendar.getInstance();
		now.setTime(new Date());
		
		intimationResponse.setResponseDatetime(dtf.newXMLGregorianCalendar(now));
		
		String transactionId = intimationRequest.getTransactionId();
		log.warn("Transaction ID \"" + transactionId + "\" would be used to determine the fate of this request.");
		
		if(transactionId.endsWith("999")) {
			log.warn(">>>>>>>>>> Request with transaction ID \"" + transactionId + "\" ends with 999. This request will be held back for some time.");
			synchronized(this) {
			try { 
				Thread.currentThread().sleep(1000*6); 
			}
			catch (InterruptedException e) { 
				log.warn(">>>>>>>>>> Interrupted: " + e); 
			}
			log.warn(">>>>>>>>>> Request with transaction ID \"" + transactionId + "\" was held back. It is now released. Response will be returned shortly.");
			}
		}

		String reasonCode = apiHelper.getTransactionReasonCode(transactionId);

		log.warn("Request with Transaction ID \"" + transactionId + "\" is mapped to a response with reason code: \"" + 
									reasonCode + "\" [ Message: \"" + apiHelper.getMessage(reasonCode) + "\"].");
		
		if(apiHelper.getHttpStatus(reasonCode) == 500) { 	 // Internal Server Error.
			apiHelper.apiInternalServerErrorResponse();
		}
		else if(apiHelper.getHttpStatus(reasonCode) == 502) { 	 // Bad Gateway
			apiHelper.apiBadGatewayErrorResponse();
		}
		else if(apiHelper.getHttpStatus(reasonCode) == 503) { 	 // Service Unavailable
			apiHelper.apiServiceUnavailableResponse();
		}

		// Completion Code.
		intimationResponse.setCompletionCode(apiHelper.getCompletionCode(reasonCode));
		// Reason Code.
		intimationResponse.setReasonCode(reasonCode);
		// Message (Reason)
		intimationResponse.setMessage(apiHelper.getMessage(reasonCode));
		
		// Retry Indicator, Retry-After-Interval, Retry-After-DateTime
		intimationResponse.setRetryIndicator(apiHelper.isRequestRetriable(reasonCode));
		
		if(intimationResponse.getCompletionCode() == APIHelper.Constants.CompletionCode.SUCCESSFUL) {
			intimationResponse.setClientDataExt(apiHelper.generateClientExtensionData());
		}
		
		if(apiHelper.isRequestRetriable(reasonCode)) {
			intimationResponse.setRetryAfterInterval(BigInteger.valueOf(600));	// 10 min. 
			
			// Side-stepping object.clone();
			GregorianCalendar later = (GregorianCalendar) Calendar.getInstance();
			later.setTime(now.getTime());
			later.add(Calendar.SECOND, 600);	// 10 min. 
			
			intimationResponse.setRetryAfterDatetime(dtf.newXMLGregorianCalendar(later));
		}
		
		log.info("Response DateTime Fields: [Response: " + intimationResponse.getResponseDatetime() +
				", Request: " + intimationResponse.getRequestDatetime() + 
				", Original Request: " + intimationResponse.getOriginalRequestDatetime() + 
				", Transaction: " + intimationResponse.getTransactionDatetime() + "]");
		
		log.info("Returning intimation response.");
		
		return intimationResponse;
	}

}
