package model.mapstruct;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public abstract class IntimationResponseUnwrapperMapper {
	public static IntimationResponseUnwrapperMapper INSTANCE = 
			Mappers.getMapper(IntimationResponseUnwrapperMapper.class);

	@BeforeMapping
	protected void unlock(com.icicibank.ws.configrity.model.wrap.IntimationResponseType wrap) {
	}
	
	public abstract com.icicibank.ws.configrity.model.wrap.IntimationResponseType 
			unwrap(com.icicibank.ws.configrity.model.wrap.IntimationResponseType wrap);
}
