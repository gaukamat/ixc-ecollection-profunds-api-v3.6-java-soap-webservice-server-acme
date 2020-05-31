package com.icici.xpress_connect.ws.ecollection.impl;

import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

// https://javaee.github.io/metro-jax-ws/doc/user-guide/ch03.htm
public class IntimationPortMessageHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("Message intercepted.");
		
		Boolean outBoundProperty = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		
		System.out.println("Outbound Message ? " + outBoundProperty);
		try {
			if(outBoundProperty) {
				System.out.println("[IBM]: Outbound message: ");
			}
			else {
				System.out.println("[IBM]: Inbound message: ");
			}
			
			SOAPMessage message = context.getMessage();
			
			System.out.println("[IBM]: \n");
			message.writeTo(System.out);
			System.out.println("\n[IBM]: \n");
		} 
		catch (SOAPException | IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public void close(MessageContext context) {
		System.out.println("[IBM]: Message context closed.");
	}

	@Override
	public Set<QName> getHeaders() {
		return null;
	}

}
