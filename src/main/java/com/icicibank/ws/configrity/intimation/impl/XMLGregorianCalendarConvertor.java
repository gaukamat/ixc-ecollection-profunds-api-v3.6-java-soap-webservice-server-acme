package com.icicibank.ws.configrity.intimation.impl;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dozer.CustomConverter;
import org.dozer.converters.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLGregorianCalendarConvertor implements CustomConverter {

	private final static Logger log = LoggerFactory.getLogger(XMLGregorianCalendarConvertor.class.getSimpleName());
	
	@Override
	public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		log.info("[IN] Source Field Type: " + sourceClass.getName() + ", Source Field Value: " + sourceFieldValue + 
				", Destination Field Type: " + destinationClass.getName() + ", Existing Destination Field Value: " + existingDestinationFieldValue);
		Object finalDestinationFieldValue = null;
		
		if (sourceFieldValue == null) {
			finalDestinationFieldValue = null;
		}
		else {
			if (destinationClass.isAssignableFrom(XMLGregorianCalendar.class)) {  // String --> XMLGregorianCalendar
				String isoDateTime = (String) sourceFieldValue;
				try {
					finalDestinationFieldValue = DatatypeFactory.newInstance().newXMLGregorianCalendar(isoDateTime);
				} 
				catch (DatatypeConfigurationException e) {
					throw new ConversionException("Unable to convert datetime to calendar.", e);
				}
			} 
			else if (sourceFieldValue instanceof XMLGregorianCalendar) { // XMLGregorianCalendar --> String
				XMLGregorianCalendar xmlGC = (XMLGregorianCalendar) sourceFieldValue;
				finalDestinationFieldValue = xmlGC.toXMLFormat();
			}
		}

		log.info("[OUT] Source Field Type: " + sourceClass.getName() + ", Source Field Value: " + sourceFieldValue + 
				", Destination Field Type: " + destinationClass.getName() + ", New Destination Field Value: " + finalDestinationFieldValue + 
				", New Destination Field Type: " + (finalDestinationFieldValue == null ? "--" : finalDestinationFieldValue.getClass()));
		
		return finalDestinationFieldValue;
	}

}
