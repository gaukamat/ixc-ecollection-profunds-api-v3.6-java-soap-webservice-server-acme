package model.mapstruct;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.icicibank.ws.configrity.intimation.handlers.ConversionContext;
import com.icicibank.ws.configrity.intimation.util.MessageSecurityHelper;

@Mapper
public abstract class IntimationResponseWrapperMapper {

	private static final Logger log = Logger.getLogger(IntimationResponseWrapperMapper.class.getSimpleName());
	private ConversionContext context;
	
	public static IntimationResponseWrapperMapper INSTANCE = 
			Mappers.getMapper(IntimationResponseWrapperMapper.class);
	

	public abstract com.icicibank.ws.configrity.model.wrap.IntimationResponseType 
				wrap(com.icicibank.ws.configrity.model.wrap.IntimationResponseType unwrap);

	@AfterMapping
	protected void lock(@MappingTarget com.icicibank.ws.configrity.model.wrap.IntimationResponseType wrap) {
		String algorithm 	= context.getAlgorithm();
		String mode 		= context.getMode();
		String padType	= context.getPadType();
		String encoding	= context.getEncoding();
		String iv 				= context.getIv();
		String ivHex			= (iv == null ? null : MessageSecurityHelper.encode(MessageSecurityHelper.decode(iv, encoding), "hex"));
		
		Key key  				= context.getEncryptionKey();

		String fields 			= context.getFields();
		
		List<String> fieldNames = null;
		if(fields == null || fields.equals("*")) {
			log.warning("ALL FIELD VALUES ENCRYPTED MODE.  Values for all fields will be encrypted.");
			fieldNames = Collections.EMPTY_LIST;
		}
		else {
			fieldNames = Arrays.asList(fields.split("\\|"));
		}
		
		log.info("Locking the Intimation Response using " + context);
	
		try {
			for (PropertyDescriptor pd : Introspector.getBeanInfo(com.icicibank.ws.configrity.model.wrap.IntimationResponseType.class).getPropertyDescriptors()) {
				  if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
					
					String fieldName 		= pd.getName();  
				    Object plainValue 		= pd.getReadMethod().invoke(wrap);

				    if(plainValue == null) {
				    	continue;
				    }
				    
				    if(!fieldNames.isEmpty() && !fieldNames.contains(pd.getName())) {
						log.warning("LOCKING " + fieldName + " field not in the list of the " + fieldNames.size() + " select fields for encryption. "
								+ "Value assumed to be clear text and will be copied as-is.");
				    	continue;
				    }
				    
				    if(plainValue instanceof String) {
				    	log.info("LOCKING " + fieldName + ", Plain Value: " + plainValue + " using " + 
		    					algorithm + "/" + mode + "/" + padType + " cipher and " + encoding + " encoding.");
				    	
						String secureValue 			=  MessageSecurityHelper.encryptWithKey(key, (String)plainValue, 
																	algorithm, mode, padType, encoding, null, ivHex);
				    	pd.getWriteMethod().invoke(wrap, secureValue);
				    	
				    	log.info("LOCKING " + fieldName + ", Value: " + plainValue + ", Secure Value: " + secureValue);
				    }
				} // read methods
			} // PropertyDescriptor loop ends
			
			log.info("Intimation Response locked.");
		} 
		catch (	IllegalAccessException | IllegalArgumentException | 
					InvocationTargetException | IntrospectionException e) {
			e.printStackTrace();
			throw new IllegalStateException("Locking of unsecure data failed.", e);
		}
	}	
	
	public ConversionContext getContext() {
		return context;
	}

	public void setContext(ConversionContext context) {
		this.context = context;
	}

	
}
