package com.icicibank.ws.configrity.intimation.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

public class StringUtil {
	
	private static final Logger log = LoggerFactory.getLogger(StringUtil.class.getSimpleName());

	public static final Boolean matches(String string, String regex) {
		Boolean found = Boolean.FALSE;

		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(string);
			found = m.matches();
		} 
		catch (Exception e) {
			log.warn("Failed to parse the string \""+ string +"\" using the regex pattern \""+ regex + "\". [Cause: "  + e + "]", e);
		}
		return found;
	}
	
	public static final String replaceAll(String sourceString, String searchRegex, String replacementString) {
		String newSourceString = null;
		// System.out.println("Search Regex: " + searchRegex);
		// System.out.println("Replacement String: " + replacementString);
		// System.out.println("Source String: \n" + sourceString + "\n\n");
		
		try {
			Pattern p = Pattern.compile(searchRegex);
			Matcher m = p.matcher(sourceString);
			if(m.find()) {
				// System.out.println("Pattern matched !");
				newSourceString = m.replaceAll(replacementString);
			}
			else {
				newSourceString = sourceString;
			}
		} 
		catch (Exception e) {
			log.warn("Source string \""+ sourceString +"\" replacement using the regex pattern \""+ searchRegex + "\" failed. [Cause: "  + e + "]", e);
		}
		
		// System.out.println("New Source String: \n" + newSourceString + "\n\n\n\n");
		return newSourceString;
	}
	
	
	// Group 0: <main:request_id>3728B8BC-BA46-4267-BBFE-F4654D9D9597</main:request_id>
	// Group 1: main:request_id
	// Group 2: main:
	// Group 3:
	// Group 4:     3728B8BC-BA46-4267-BBFE-F4654D9D9597

	// Group 0: <request_datetime>2020-09-19T10:41:36.614+05:30</request_datetime>
	// Group 1: request_datetime
	// Group 2: null
	// Group 3:
	// Group 4:     2020-09-19T10:41:36.614+05:30

	// Group 0: <transaction_amount>28800.00</transaction_amount>
	// Group 1: transaction_amount
	// Group 2: null
	// Group 3:
	// Group 4:     28800.00

	// Group 0: <ns3:narration-3 ns3:lang="en" locale="en_US" xmlns:ns3="http://www.ns3.org/p1/p3">PHENOMENAL NARRATION</ns3:narration-3>
	// Group 1: ns3:narration-3
	// Group 2: ns3:
	// Group 3: ns3:lang="en" locale="en_US" xmlns:ns3="http://www.ns3.org/p1/p3"
	// Group 4:     PHENOMENAL NARRATION

	public static String concatenateXmlFieldValuePairs(String message,
			String fieldNVSeparator, String fieldNVPSeparator,
			String elementNameToSkip, String fieldNameCase, boolean dropNSPrefix) {

	// String regexPatternStr = "\\<(([A-Za-z0-9\\-_]+:){0,1}[A-Za-z0-9\\-_]+)\\>([^\\<\\>]*)\\</\\1\\>";
		String regexPatternStr = "\\<(([A-Za-z0-9\\-_]+:){0,1}[A-Za-z0-9\\-_]+)([^\\<\\>]*)\\>([^\\<\\>]*)\\</\\1\\>";
		Matcher matcher = Pattern.compile(regexPatternStr).matcher(message);

		boolean deleteLastDelimiter = matcher.find(0);
		StringBuffer computePayloadBuffer = new StringBuffer();

		boolean isWrapType = (fieldNVPSeparator.length() > 1);
		String keyCase = fieldNameCase.toLowerCase();
		matcher.reset();
		while (matcher.find()) {
			String key = matcher.group(1);
			String nsPrefix = matcher.group(2);
			
			if(dropNSPrefix && !(nsPrefix == null)) {
				key = key.replace(nsPrefix, "");
			}
			
			if ((elementNameToSkip != null && !elementNameToSkip.isEmpty())
					&& elementNameToSkip.equalsIgnoreCase(key)) {
				continue;
			}
			
			String newKey = ("upper".equals(keyCase) ? key.toUpperCase()
					: ("lower".equals(keyCase) ? key.toLowerCase() : key));

			String value = matcher.group(4);

			if (isWrapType) {
				computePayloadBuffer.append(fieldNVPSeparator.charAt(0));
				computePayloadBuffer.append(newKey + fieldNVSeparator + value);
				computePayloadBuffer.append(fieldNVPSeparator.charAt(1));
			} else {
				computePayloadBuffer.append(newKey + fieldNVSeparator + value);
				computePayloadBuffer.append(fieldNVPSeparator);
			}
		}
		if (deleteLastDelimiter && !isWrapType) {
			computePayloadBuffer
					.deleteCharAt(computePayloadBuffer.length() - 1);
		}
		return computePayloadBuffer.toString();
	}
	
    public static String removeXmlWhitespaces(String whiteXML) {
    	if(whiteXML == null || whiteXML.isEmpty()) {
    		return null;
    	}
        String regex = ">\\s+<";
        String replacementXMLString = "><";
    	
    	String 	nonWhiteXML = StringUtil.replaceAll(whiteXML, regex, replacementXMLString);
    	nonWhiteXML = nonWhiteXML.trim();
    	
    	return nonWhiteXML;
    }
}
