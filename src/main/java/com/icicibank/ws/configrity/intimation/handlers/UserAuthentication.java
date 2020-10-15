package com.icicibank.ws.configrity.intimation.handlers;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAuthentication extends BasePolicyInterceptor 
	implements SOAPHandler<SOAPMessageContext> {
	
	public UserAuthentication() {
		super();
	}
	
	private static interface UserAuthenticationConstants {
		interface ConfigurationParameters {
			String AUTHENTICATION_TYPE 			= "api-user-authentication-type";
			String PARAMETERS_LOCATION 			= "api-user-authentication-parameters-location";
		//	String API_USER_ID_TOKEN					= "api-user-authentication-userId-token-name";
		//	String API_USER_ACCESS_TOKEN	 	= "api-user-authentication-access-token-name";
		}
		
		interface Type {
			String BASIC_AUTHENTICATION 		= "basic-auth";
			String CUSTOM_AUTHENTICATION 	= "custom-auth";
		}
		interface AutheticationKeys {
			String API_USER_ID_TOKEN				= "API-UserId-Token";
			String API_USER_ACCESS_TOKEN	 	= "API-Access-Token";
		}
		interface HttpHeaders {
		    String BASIC_AUTHENTICATION_HEADERNAME	= "Authorization";
		    String AUTHENTICATION_SCHEME 								= "Basic";
		}
		interface XPaths {
			String API_USER_ID_TOKEN				= "acmex:" + AutheticationKeys.API_USER_ID_TOKEN;
			String API_USER_ACCESS_TOKEN	 	= "acmex:" + AutheticationKeys.API_USER_ACCESS_TOKEN;
		}
	}
	
	private Logger log = LoggerFactory.getLogger(UserAuthentication.class.getSimpleName());
    private Map<String, String> apiUserDB;
    
 	public boolean handleRequest(SOAPMessageContext messageContext) {
		 return handleAuthentication(messageContext);
	}

 	public boolean handleResponse(SOAPMessageContext messageContext) {
		info("Authentication is not relevant for response flow.");
		 return true;
	}

 	private boolean handleAuthentication(SOAPMessageContext messageContext) {
		info("User authentication will be done against the sample username-password pairs. [" + apiUserDB + "]");
		Boolean allowAccess = Boolean.FALSE;
		
		String authType = policyDefaults.getProperty(UserAuthenticationConstants.ConfigurationParameters.AUTHENTICATION_TYPE);

		if(authType.equals(UserAuthenticationConstants.Type.BASIC_AUTHENTICATION)) {
			allowAccess = handleBasicAuthentication(messageContext);
		}
		else if(authType.equals(UserAuthenticationConstants.Type.CUSTOM_AUTHENTICATION)) {
			allowAccess = handleCustomAuthentication(messageContext);
		} 
		
		if(! allowAccess) {
			throw new WebServiceException("API user authentication failure. User authentication is done against the sample username-password pairs. [" + apiUserDB + "]");
		}
		
		return allowAccess;
	}
	
	private Boolean handleCustomAuthentication(SOAPMessageContext messageContext) {
		String username = null, password = null;
		
		String authParameterLocation = policyDefaults.getProperty(
				UserAuthenticationConstants.ConfigurationParameters.PARAMETERS_LOCATION);
		
		if(authParameterLocation.equals(FieldValue.Location.HTTP_HEADER)) {
			username = getSingleHttpRequestHeader(messageContext, 
					UserAuthenticationConstants.AutheticationKeys.API_USER_ID_TOKEN);
			password = getSingleHttpRequestHeader(messageContext, 
					UserAuthenticationConstants.AutheticationKeys.API_USER_ACCESS_TOKEN);
		}
		else if(authParameterLocation.equals(FieldValue.Location.MESSAGE_BODY)) {
			username = getSingleSOAPBodyFieldValue(messageContext,  UserAuthenticationConstants.XPaths.API_USER_ID_TOKEN);
			password = getSingleSOAPBodyFieldValue(messageContext,  UserAuthenticationConstants.XPaths.API_USER_ACCESS_TOKEN);
		}
		else if(authParameterLocation.equals(FieldValue.Location.SOAP_HEADER)) {
			username = getSingleSOAPHeaderFieldValue(messageContext,  UserAuthenticationConstants.XPaths.API_USER_ID_TOKEN);
			password = getSingleSOAPHeaderFieldValue(messageContext,  UserAuthenticationConstants.XPaths.API_USER_ACCESS_TOKEN);
		}
		
		return authenticateUser(username, password);
	}

	private Boolean handleBasicAuthentication(SOAPMessageContext messageContext) {
		String authParameterLocation = policyDefaults.getProperty(UserAuthenticationConstants.ConfigurationParameters.PARAMETERS_LOCATION);
		
		if(authParameterLocation.equals(FieldValue.Location.HTTP_HEADER)) {
			String basicAuthHeader = getSingleHttpRequestHeader(messageContext, 
							UserAuthenticationConstants.HttpHeaders.BASIC_AUTHENTICATION_HEADERNAME);

			// Get encoded username and password
			final String encodedUserPassword = basicAuthHeader.replaceFirst(
					UserAuthenticationConstants.HttpHeaders.AUTHENTICATION_SCHEME + " ", "");

			// Decode username and password
			String usernameAndPassword = null;
			try {
				usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword));
			} 
			catch (IllegalArgumentException e) {
				throw new WebServiceException("Basic Authentication header encoding error.", e);
			}

			// Split username and password tokens
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();

			// Verifying username and password
			boolean isAuthenticated = authenticateUser(username, password);
			log.info("Authenticating API user using username \"" + username + "\" and password \"" +password + "\". [Authenticated ? " + isAuthenticated + "]");
			
			return isAuthenticated;
		}
		else /* if(authParameterLocation.equals(FieldValue.Location.SOAP_HEADER)) */ {
			throw new WebServiceException("Unable to validate api user.", 
					new UnsupportedOperationException("Basic Authentication should be within HTTP header. "
							+ "SOAP header currently unsupported."));
		}
	}

	private boolean authenticateUser(String username, String password) {
		log.warn("Authenticating username \"" + username + "\" and password \"" + password+ "\".");
		Boolean isAllowed =  (apiUserDB.containsKey(username) && apiUserDB.get(username).equals(password));

		 log.info("User authenticated ? " + isAllowed);
		 if(!isAllowed) {
			 log.warn("Username/password combinations should be one of  " + apiUserDB + ".");
		 }
		return isAllowed;
	}

	@Override
	protected void initPolicy() {
		setPolicyName("UserAuthentication");
		
		final String policyDefaultsFileName = "policy-defaults/user-authentication-defaults.properties";
		policyDefaults = loadPolicyConfiguration(policyDefaultsFileName);

		if(policyDefaults.isEmpty()) {
			warn("Configurable defaults were not found within the properties file \"" + policyDefaultsFileName + "\". "
					+ "Check if the properties file is on the classpath (within classes of the .war). "
					+ "Validate the relative path, the filename and the read permissions.");

			policyDefaults.setProperty(UserAuthenticationConstants.ConfigurationParameters.AUTHENTICATION_TYPE, 
					UserAuthenticationConstants.Type.BASIC_AUTHENTICATION);
			policyDefaults.setProperty(UserAuthenticationConstants.ConfigurationParameters.PARAMETERS_LOCATION, 
																  FieldValue.Location.HTTP_HEADER);

			warn("Defaulting to hard-fixed defaults. [" + policyDefaults + "] (Basic Authentication)");
		}
		
		apiUserDB	= new HashMap<>();

		apiUserDB.put("tomcat", "s3cret");
		apiUserDB.put("mclaren", "pa55word@1");
		apiUserDB.put("williams", "pa55word@2");
		apiUserDB.put("redbull", "pa55word@3");
	}

}