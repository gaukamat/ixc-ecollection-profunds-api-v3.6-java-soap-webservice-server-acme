package com.icicibank.ws.configrity.intimation.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icicibank.ws.configrity.intimation.WrapperIntimationServicePort;
import com.icicibank.ws.configrity.model.wrap.IntimationRequestType;
import com.icicibank.ws.configrity.model.wrap.IntimationResponseType;
import com.icicibank.ws.configrity.model.wrap.ObjectFactory;

@WebService(endpointInterface = "com.icicibank.ws.configrity.intimation.WrapperIntimationServicePort")
@HandlerChain(file = "handler-chain-wrapper.xml")
public class WrapperIntimationServicePortImpl implements WrapperIntimationServicePort {

	private final Logger log 	= 
			LoggerFactory.getLogger(WrapperIntimationServicePortImpl.class.getSimpleName());

	// com.icicibank.ws.configrity.model.wrap
	private ObjectFactory 	wrapModelOF	 = 
			new com.icicibank.ws.configrity.model.wrap.ObjectFactory();

	private com.icicibank.ws.configrity.model.ObjectFactory 	modelOF	 = 
			new com.icicibank.ws.configrity.model.ObjectFactory();

	private SimpleIntimationServicePortImpl simpleServicePortImpl = new SimpleIntimationServicePortImpl();
	
	private DozerBeanMapper mapper = null;
	
	public WrapperIntimationServicePortImpl() {
		super();

		List<String> maps = new ArrayList<>();
		maps.add("dozer.xml");

		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(maps);
		
	}

	@Override
	public IntimationResponseType intimate(IntimationRequestType intimationRequestWrap) {
		com.icicibank.ws.configrity.model.IntimationRequestType intimationRequest = modelOF.createIntimationRequestType();
		
		log.info("First level message entity transformation (forward path) from wrapped to bare message type.");
		
		mapper.map(intimationRequestWrap, intimationRequest);
		
		log.info("Intimation Request Wrapper: " + intimationRequestWrap);
		log.info("Intimation Request: " + intimationRequest);
		
		log.info("Delegating to the base service implementation.");
		
		com.icicibank.ws.configrity.model.IntimationResponseType intimationResponse = null;
		intimationResponse = simpleServicePortImpl.intimate(intimationRequest);

		log.info("First level message entity transformation (return path) from bare to wrapped type.");
		log.info("Intimation Response: " + intimationResponse);
	
		IntimationResponseType intimationResponseWrap = wrapModelOF.createIntimationResponseType();
		mapper.map(intimationResponse, intimationResponseWrap);

		log.info("Intimation Response Wrapper: " + intimationResponseWrap);
		
		return intimationResponseWrap;
	}
}
