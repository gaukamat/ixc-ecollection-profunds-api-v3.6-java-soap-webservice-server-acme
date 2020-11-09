package com.icicibank.ws.configrity.intimation.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageAuditor extends BasePolicyInterceptor 
	implements SOAPHandler<SOAPMessageContext> {
	
	private Logger log = LoggerFactory.getLogger(MessageAuditor.class.getSimpleName());
	
	public MessageAuditor() {
		super();
	}
	public boolean handleRequest(SOAPMessageContext messageContext) {
		logHttpHeaders(messageContext);
		 log(messageContext);
		return true;
	}

	public boolean handleResponse(SOAPMessageContext messageContext) {
		logHttpHeaders(messageContext);
		log(messageContext);
		return true;
	}

	public boolean handleFault(SOAPMessageContext messageContext) {
		logHttpHeaders(messageContext);
		log(messageContext);
		return true;
	}

	public void close(MessageContext context) {
	}

	public Set<QName> getHeaders() {
		return Collections.EMPTY_SET;
	}
	
	private void logHttpHeaders(SOAPMessageContext messageContext) {
		Boolean isOutbound = (Boolean)messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		String messageLabel = (isOutbound ? "response" : "request");
		
		Map<String, List<String>> httpHeaders = null;
		
		if(isOutbound) {
			httpHeaders = (Map<String, List<String>>) messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
		}
		else {
			httpHeaders = (Map<String, List<String>>) messageContext.get(MessageContext.HTTP_REQUEST_HEADERS);
		}
		
		log.info(messageLabel.toUpperCase() + " HTTP Headers: " + httpHeaders);
	}
	
	private void log(SOAPMessageContext messageContext) {
		SOAPMessage msg = messageContext.getMessage(); 
		Boolean isOutbound = (Boolean)messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		String messageLabel = (isOutbound ? "response" : "request");
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			msg.writeTo(baos);
			String message = baos.toString(getMessageEncoding(messageContext.getMessage()));
			
			log.info(messageLabel.toUpperCase() + " Payload: [" + message + "]");
		} 
		catch (SOAPException | IOException ex) {            
			log.error("Unable to log the SOAP " +  messageLabel +" message.", ex);
		} 
	}

	@Override
	protected void initPolicy() {
		setPolicyName("MessageAuditor");
	}
}