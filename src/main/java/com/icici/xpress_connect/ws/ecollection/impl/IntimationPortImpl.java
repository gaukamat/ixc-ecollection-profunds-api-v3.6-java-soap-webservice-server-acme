package com.icici.xpress_connect.ws.ecollection.impl;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.icici.xpress_connect.ws.ecollection.IntimationPort;
import com.icici.xpress_connect.ws.ecollection.IntimationRequestType;
import com.icici.xpress_connect.ws.ecollection.IntimationResponseType;
import com.icici.xpress_connect.ws.ecollection.ObjectFactory;
import com.icici.xpress_connect.ws.ecollection.RefundIntimationRequestType;
import com.icici.xpress_connect.ws.ecollection.RefundIntimationResponseType;
import com.icici.xpress_connect.ws.ecollection.impl.APIHelper.Constants.CompletionCode;

@WebService(endpointInterface = "com.icici.xpress_connect.ws.ecollection.IntimationPort")
@HandlerChain(file = "handlers.xml")
public class IntimationPortImpl implements IntimationPort {

	private ObjectFactory of = new ObjectFactory();
	private DatatypeFactory dtf = null;

	private APIHelper apiHelper = null;
	
	public IntimationPortImpl() {
		try {
			try {
				apiHelper = new APIHelper();
				
				dtf = DatatypeFactory.newInstance();
				System.out.println("[INFO]: \"javax.xml.datatype.DatatypeFactory\" instance created.");
			} 
			catch (DatatypeConfigurationException dtce) {
				System.out.println("[WARN]: Unable to instantiate \"javax.xml.datatype.DatatypeFactory\". [Cause: " + dtce + "]");

				InstantiationException ie = new InstantiationException("Failed to configure Intimation Port Implementation.");
				ie.initCause(dtce);
			}
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
			System.out.println("[WARN]: Configuration failure(s). Service implementation will not respond as desired.");
		}
	}
	
	@Override
	public IntimationResponseType intimate(IntimationRequestType intimationRequest) {
		System.out.println("[]NFO]: Processing intimation request.");
		
		IntimationResponseType intimationResponse = of.createIntimationResponseType();
		
		Mapper mapper = new DozerBeanMapper();
		mapper.map(intimationRequest, intimationResponse);
		
		intimationResponse.setResponseId(UUID.randomUUID().toString().toUpperCase());
		
		GregorianCalendar now = (GregorianCalendar)GregorianCalendar.getInstance();
		now.setTime(new Date());
		
		intimationResponse.setResponseDatetime(dtf.newXMLGregorianCalendar(now));
		
		String transactionId = intimationRequest.getTransactionId();
		System.out.println("[INFO]: Transaction ID \"" + transactionId + "\" would be used to determine the fate of this request.");
		
		String reasonCode = apiHelper.getTransactionReasonCode(transactionId);

		System.out.println("[INFO]: Request with Transaction ID \"" + transactionId + "\" is mapped to a response with reason code: \"" + 
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
		
		System.out.println("[]NFO]: Returning intimation response.");
		
		return intimationResponse;
	}

	@Override
	public RefundIntimationResponseType refundIntimate(RefundIntimationRequestType refundIntimationRequest) {
		System.out.println("[]NFO]: Processing refund intimation request.");
		
		RefundIntimationResponseType refundIntimationResponse = of.createRefundIntimationResponseType();
		
		Mapper mapper = new DozerBeanMapper();
		mapper.map(refundIntimationRequest, refundIntimationResponse);
		
		refundIntimationResponse.setResponseId(UUID.randomUUID().toString().toUpperCase());
		
		GregorianCalendar now = (GregorianCalendar)GregorianCalendar.getInstance();
		now.setTime(new Date());
		
		refundIntimationResponse.setResponseDatetime(dtf.newXMLGregorianCalendar(now));
		
		String transactionId = refundIntimationRequest.getTransactionId();
		System.out.println("[INFO]: Transaction ID \"" + transactionId + "\" would be used to determine the fate of this request.");
		
		String reasonCode = apiHelper.getTransactionReasonCode(transactionId);

		System.out.println("[INFO]: Request with Transaction ID \"" + transactionId + "\" is mapped to a response with reason code: \"" + 
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
		refundIntimationResponse.setCompletionCode(apiHelper.getCompletionCode(reasonCode));
		
		if("P".equals(refundIntimationRequest.getRefundPaymentStatus())) { // Refund Paid
			// refundIntimationResponse.setRefundPaymentStatus("P");	 			
			refundIntimationResponse.setCmsReferenceNumber("CMS-" + now.get(Calendar.YEAR) + 
						now.get(Calendar.MONTH) + now.get(Calendar.DATE) + "-" + RandomStringUtils.randomNumeric(4));
			refundIntimationResponse.setRefundCode(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
		}
		else { // Refund Cancelled
			// refundIntimationResponse.setRefundPaymentStatus("C");				
			refundIntimationResponse.setRefundRejectionCode(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
		}
		
		
		// Reason Code.
		refundIntimationResponse.setReasonCode(reasonCode);
		// Message (Reason)
		refundIntimationResponse.setMessage(apiHelper.getMessage(reasonCode));
		
		// Retry Indicator, Retry-After-Interval, Retry-After-DateTime
		refundIntimationResponse.setRetryIndicator(apiHelper.isRequestRetriable(reasonCode));
		
		if(refundIntimationResponse.getCompletionCode() == APIHelper.Constants.CompletionCode.SUCCESSFUL) {
			refundIntimationResponse.setClientDataExt(apiHelper.generateClientExtensionData());
		}
		
		if(apiHelper.isRequestRetriable(reasonCode)) {
			refundIntimationResponse.setRetryAfterInterval(BigInteger.valueOf(600));	// 10 min. 
			
			// Side-stepping object.clone();
			GregorianCalendar later = (GregorianCalendar) Calendar.getInstance();
			later.setTime(now.getTime());
			later.add(Calendar.SECOND, 600);	// 10 min. 
			
			refundIntimationResponse.setRetryAfterDatetime(dtf.newXMLGregorianCalendar(later));
		}
		
		System.out.println("[]NFO]: Returning refund intimation response.");
		
		return refundIntimationResponse;
	}

}
