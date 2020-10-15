package com.icicibank.ws.configrity.intimation.impl;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icicibank.ws.configrity.intimation.CompactV3IntimationServicePort;
import com.icicibank.ws.configrity.model.secure.compact.ObjectFactory;
import com.icicibank.ws.configrity.model.secure.compact.SecureMessageType;


@WebService(endpointInterface = "com.icicibank.ws.configrity.intimation.CompactV3IntimationServicePort")
@HandlerChain(file = "handler-chain-compact-v3.xml")
public class CompactV3IntimationServicePortImpl implements CompactV3IntimationServicePort {

	private final Logger log 	= 
			LoggerFactory.getLogger(CompactV3IntimationServicePortImpl.class.getSimpleName());

	// com.icicibank.ws.configrity.model.secure.compact
	private ObjectFactory 	modelOF	 = 
			new com.icicibank.ws.configrity.model.secure.compact.ObjectFactory();

	public CompactV3IntimationServicePortImpl() {
		super();
	}

	@Override
	public SecureMessageType intimate(SecureMessageType intimationRequestWrapPayloadCompact) {
		// TODO Auto-generated method stub
		return null;
	}

}
