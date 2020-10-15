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

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.icicibank.ws.configrity.intimation.handlers.ConversionContext;
import com.icicibank.ws.configrity.intimation.util.MessageSecurityHelper;

@Mapper
public abstract class IntimationRequestUnwrapperMapper {
	
	private static final Logger log = Logger.getLogger(IntimationRequestUnwrapperMapper.class.getSimpleName());
	private ConversionContext context;

	public static IntimationRequestUnwrapperMapper INSTANCE = 
			Mappers.getMapper(IntimationRequestUnwrapperMapper.class);

	public abstract com.icicibank.ws.configrity.model.wrap.IntimationRequestType 
				unwrap(com.icicibank.ws.configrity.model.wrap.IntimationRequestType wrap);

	@BeforeMapping
	protected void unlock(com.icicibank.ws.configrity.model.wrap.IntimationRequestType wrap) {

		String algorithm 	= context.getAlgorithm();
		String mode 			= context.getMode();
		String padType		= context.getPadType();
		String encoding		= context.getEncoding();
		String iv 					= context.getIv();
		String ivHex			= (iv == null ? null : MessageSecurityHelper.encode(MessageSecurityHelper.decode(iv, encoding), "hex"));
		
		Key key  					= context.getDecryptionKey();
		
		String fields 			= context.getFields();
		
		List<String> fieldNames = null;
		if(fields == null || fields.equals("*")) {
			log.warning("ALL FIELD VALUES ENCRYPTED MODE.  Values for all fields are expected to be encrypted and we will be decrypting them.");
			fieldNames = Collections.EMPTY_LIST;
		}
		else {
			fieldNames = Arrays.asList(fields.split("\\|"));
		}

		log.info("Unlocking the Intimation Request using " + context);
	
		try {
			for (PropertyDescriptor pd : Introspector.
					getBeanInfo(com.icicibank.ws.configrity.model.wrap.IntimationRequestType.class).getPropertyDescriptors()) {
				if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {

					String fieldName = pd.getName();
					Object value = pd.getReadMethod().invoke(wrap);

					if (value == null) {
						continue;
					}

					if (!fieldNames.isEmpty() && !fieldNames.contains(fieldName)) {
						log.warning("UNLOCKING " + fieldName + " field not in the list of the " + fieldNames.size() + " select fields for encryption. "
								+ "Value assumed to be clear text and will be copied as-is.");
						pd.getWriteMethod().invoke(wrap, value);
						continue;
					}

					if (value instanceof String) {
						log.info("UNLOCKING " + fieldName + ", Secure Value: " + value + " using " + algorithm + "/" + mode + "/" + padType + " cipher and " + encoding + " encoding.");
						
						String plainValue = MessageSecurityHelper.decryptWithKey(key, (String) value, encoding,
								algorithm, mode, padType, ivHex);
						
						pd.getWriteMethod().invoke(wrap, plainValue);
						
						log.info("UNLOCKING " + fieldName + ", Secure Value: " + value + ", Value: " + plainValue);
					}
				} // read methods
			} // PropertyDescriptor loop ends
			
			log.info("Intimation Request unlocked.");
		} 
		catch (IllegalAccessException | IllegalArgumentException | 
					InvocationTargetException | IntrospectionException e) {
			throw new IllegalStateException("Unlocking of secure data failed.", e);
		}
	}

	public ConversionContext getContext() {
		return context;
	}

	public void setContext(ConversionContext context) {
		this.context = context;
	}
}
