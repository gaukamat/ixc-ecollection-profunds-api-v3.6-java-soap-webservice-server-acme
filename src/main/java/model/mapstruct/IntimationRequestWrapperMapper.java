package model.mapstruct;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class IntimationRequestWrapperMapper {
	
	public static IntimationRequestWrapperMapper INSTANCE = 
			Mappers.getMapper(IntimationRequestWrapperMapper.class);
	
	@AfterMapping
	protected void lock(@MappingTarget com.icicibank.ws.configrity.model.wrap.IntimationRequestType wrap) {
		
	}

	public abstract com.icicibank.ws.configrity.model.wrap.IntimationRequestType 
				wrap(com.icicibank.ws.configrity.model.wrap.IntimationRequestType unwrap);
}
