
package com.icicibank.ws.configrity.intimation.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;
import java.util.UUID;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.icicibank.ws.configrity.model.ClientDataExtType;
import com.icicibank.ws.configrity.model.ObjectFactory;

public class APIHelper {
	private ObjectFactory of = new ObjectFactory();
	private SOAPFactory  sf = null;
	
	private Logger log = LoggerFactory.getLogger(APIHelper.class.getSimpleName());

	public static interface Constants {
		interface CompletionCode {
			BigInteger SUCCESSFUL = BigInteger.ZERO;
		//	BigInteger WARNING = BigInteger.ONE;
			BigInteger ERROR = BigInteger.valueOf(2);
		}
		interface ReasonCode {
			String OK = "XC00202I";
			String TRANSACTION_ACCEPTED = "XC00441I";
		//	String TRANSACTION_REJECTED = "XC00442E";

		//	String BAD_GATEWAY = "XC00502E";
		//	String CLIENT_CODE_VIRTUAL_ACCOUNT_NUMBER_MISMATCH = "XC00403E";
		//	String DUPLICATE_TRANSACTION_ID = "XC00404E";
		//	String GATEWAY_TIMEOUT = "XC00504E";
		//	String INTERNAL_SERVER_ERROR = "XC00500E";
		//	String INVALID_CLIENT_CODE = "XC00401E";
		//	String INVALID_CLIENT_IFSC_CODE = "XC00407E";
		//	String INVALID_SENDER_IFSC_CODE = "XC00406E";
		//	String INVALID_VIRTUAL_ACCOUNT_NUMBER = "XC00402E";
		//	String MISMATCH_CLIENT_ACCOUNT_NUMBER = "XC00405E";
		//	String MULTI_STATUS = "XCMULTIW";
		//	String REFUND_CODE_NOT_FOUND = "XC00434E";
		//	String REJECTION_REASON_NOT_PROVIDED = "XC00435W";
		//	String REMITTER_ACCOUNT_NUMBER_NOT_FOUND = "XC00431E";
		//	String REQUEST_ACCEPTED = "XC00203I";
		//	String REQUEST_NOT_VALID = "XC00101E";
		//	String RESPONSE_NOT_VALID = "XC00102E";
		//	String RESPONSE_TIMEOUT  = "XC00998E";
		//	String RESPONSE_UNPROCESSABLE  = "XC00999E";
		//	String REVERSAL_STATUS_CODE_NOT_VALID = "XC00433E";
		//	String REVERSAL_STATUS_NOT_FOUND = "XC00432E";
		//	String SERVICE_UNAVAILABLE = "XC00503E";

		//	String REMITTER_ACCOUNT_NUMBER_NOT_FOUND = "XC00431E";
		//	String REVERSAL_STATUS_NOT_FOUND = "XC00432E";
		//	String REVERSAL_STATUS_CODE_NOT_VALID = "XC00433E";
		//	String REFUND_CODE_NOT_FOUND = "XC00434E";
		//	String REJECTION_REASON_NOT_PROVIDED = "XC00435W";
			
		}
	}
	
	private Properties reasonCodeConstants = null;
	private Properties reasonMessages = null;
	private Properties requestResponseReasonMap = null;
	private Properties reasonHttpStatusMap = null;
	
	public APIHelper() throws InstantiationException {
		super();
		try {
			sf = SOAPFactory.newInstance();
		} 
		catch (SOAPException se) {
			log.error("Unable to load JDK library.", se);
			
			InstantiationException ie = new InstantiationException("Failed to configure API Helper");
			ie.initCause(se);
			
;			throw ie;
		}

		String configurationProperties[] = { "reason codes",  "reason messages", "request-response reason code based map", "reason code to HTTP status code map"};
		int counter = 0;
		
		final String propertiesBasePath = "api-properties/";
		
		try {
			reasonCodeConstants = new Properties();
			reasonCodeConstants.load(this.getClass().getClassLoader().getResourceAsStream(propertiesBasePath + "reason_codes.properties"));
			log.info(reasonCodeConstants.size() + " " + configurationProperties[counter] + " loaded.");
			counter++;
			
			reasonMessages = new Properties();
			reasonMessages.load(this.getClass().getClassLoader().getResourceAsStream(propertiesBasePath + "reason_messages.properties"));
			log.info(reasonMessages.size() + " " + configurationProperties[counter] + " loaded.");
			counter++;

			requestResponseReasonMap = new Properties();
			requestResponseReasonMap.load(this.getClass().getClassLoader().getResourceAsStream(propertiesBasePath + "request_response_reason_map.properties"));
			log.info(requestResponseReasonMap.size() + " " + configurationProperties[counter] + " loaded.");
			counter++;

			reasonHttpStatusMap = new Properties();
			reasonHttpStatusMap.load(this.getClass().getClassLoader().getResourceAsStream(propertiesBasePath + "reason_code_httpStatus_map.properties"));
			log.info(reasonHttpStatusMap.size() + " " + configurationProperties[counter] + " loaded.");

			log.info((counter+1) + " of " + configurationProperties.length + " configuration steps completed.");
		}
		catch (IOException ioe) {
			log.error("Unable to load " + configurationProperties[counter] + ". ", ioe);
			log.error("Error at " + (counter+1) + " of " + configurationProperties.length + " configuration steps.");
			
			InstantiationException ie = new InstantiationException("Failed to configure API Helper");
			ie.initCause(ioe);
			
;			throw ie;
		}
	}
	
	public String getMessage(String reasonCode) {
		return reasonMessages.getProperty(reasonCode, "No description found for reason code \"" + reasonCode + "\".");
	}
	
	// Example:
	// If Transaction ID (request field) is N266190934399442
	// Last-3-Digits = 442
	// Lookup 442 in request_response_reason_map.properties. This returns TRANSACTION_REJECTED.
	// Lookup TRANSACTION_REJECTED in reason_codes.properties. This returns XC00442E.
	// Why so many properties file ? Why not an XML or JSON map ? 
	// 		- Simpler solution. Works ! Easier to change !
	public String getTransactionReasonCode(String transactionId) {
		if(transactionId == null) {
			transactionId = "NULL";
		}
		String lastThreeDigits = transactionId.substring(transactionId.length()-3);
		String reasonCodeConstantName = requestResponseReasonMap.getProperty(lastThreeDigits, "OK");
		
		String reasonCode =  reasonCodeConstants.getProperty(reasonCodeConstantName, "OK");

		System.out.println("Transaction ID: \"" + transactionId +"\", Last-3-Digits: \""+ lastThreeDigits+"\", "
				+ "Reason Constant Name: \"" + reasonCodeConstantName + "\", Reason Code: \""+ reasonCode +"\"");
		 
		return reasonCode; 
	}
	
	public Integer getHttpStatus(String reasonCode) {
		String value = reasonHttpStatusMap.getProperty(reasonCode, "200,false");		// XC00202I=200,false		reason_code_httpStatus_map.properties
		int delimiterIndex = value.indexOf(",");
		if(delimiterIndex == -1) {
			return Integer.parseInt(value);
		}
		return Integer.parseInt(value.substring(0, delimiterIndex));
	}
	
	public Boolean isRequestRetriable(String reasonCode) {
		String value = reasonHttpStatusMap.getProperty(reasonCode, "200,false"); 		// XC00202I=200,false		reason_code_httpStatus_map.properties
		int delimiterIndex = value.indexOf(",");
		if(delimiterIndex == -1) {
			return Boolean.parseBoolean(value);
		}
		return Boolean.parseBoolean(value.substring(delimiterIndex + 1));
	}
	
	public BigInteger getCompletionCode(String reasonCode) {
		if(Constants.ReasonCode.OK.equals(reasonCode) || 
			Constants.ReasonCode.TRANSACTION_ACCEPTED.equals(reasonCode)) {
			return Constants.CompletionCode.SUCCESSFUL;
		}
		return Constants.CompletionCode.ERROR;
	}
	
	
	public ClientDataExtType generateClientExtensionData() {
		Document dom = null;
		try {
			dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element internalTransactionId = dom.createElementNS("http://www.acme.com/api-services/icici-collection-api", "InternalTransactionId");
			internalTransactionId.setTextContent("TRANX-REF-" + UUID.randomUUID().toString().toUpperCase());
			
			ClientDataExtType clientXData = of.createClientDataExtType();
			clientXData.getAny().add(0, internalTransactionId);
			
			System.out.println("[INFO]: Client Data Extension fields included in the response message.");
			
			return clientXData;
		} 
		catch (ParserConfigurationException e) {
			System.out.println("[WARN]: Unable to instantiate Document Builder. Client Data Extension fields will not be included in the response message.");
		}
		
		return null;
	}

	public void apiInternalServerErrorResponse() {
		System.out.println("[WARN]: Throwing \"java.lang.InternalError\" runtime exception.");
		throw new InternalError("Ambushed by Capt. Jack Sparrow !");
	}

	public void apiBadGatewayErrorResponse() {
		try {
			sf = SOAPFactory.newInstance();
			SOAPFault fault = sf.createFault("Goofed up parsing the request !", new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
			System.out.println("[WARN]: Throwing SOAP Fault.");
			throw new SOAPFaultException(fault);
		} 
		catch (SOAPException e) {
			e.printStackTrace();
		}
	}

	public void apiServiceUnavailableResponse() {
		try {
			SOAPFault fault = sf.createFault("On a coffee break ! See you later !", new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server"));
			
			Detail faultDetail = fault.addDetail();
			faultDetail.addNamespaceDeclaration("xprsc", "http://www.icici.com/xpress-connect/ws/fault/eCollection-api");

			SOAPElement coffeeBreak = faultDetail.addChildElement("coffee-break", "xprsc");
			coffeeBreak.addAttribute(new QName("break-time"), "10 mins");
			coffeeBreak.addChildElement("coffee-place","xprsc").addTextNode("Cafeteria");
			
			System.out.println("[WARN]: Throwing SOAP Fault.");
			throw new SOAPFaultException(fault);
		} 
		catch (SOAPException e) {
			e.printStackTrace();
		}
	}
}
