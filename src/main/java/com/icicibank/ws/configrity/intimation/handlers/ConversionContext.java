package com.icicibank.ws.configrity.intimation.handlers;

import java.security.Key;

import org.apache.commons.codec.binary.Hex;

public class ConversionContext {
	private Key encryptionKey;
	private Key decryptionKey;

	private String algorithm;
	private String mode;
	private String padType;
	
	private String encoding;
	private String iv;
	private String ivLocation;
	
	private String fields;
	
	public ConversionContext(Key encryptionKey, Key decryptionKey, String algo, String mode, String padType, String encoding) {
		super();

		this.encryptionKey 	= encryptionKey;
		this.decryptionKey 	= decryptionKey;
		this.algorithm 			= algo;
		this.mode 					= mode;
		this.padType 				= padType;
		this.encoding 				= encoding;
	}
	
	public ConversionContext(Key encryptionKey, Key decryptionKey, String algo, String mode, String padType, 
				String encoding, String iv) {
		super();

		this.encryptionKey 		= encryptionKey;
		this.decryptionKey 	= decryptionKey;
		this.algorithm = algo;
		this.mode = mode;
		this.padType = padType;
		this.encoding = encoding;
		this.iv = iv;
		this.ivLocation = BasePolicyInterceptor.FieldValue.Location.HTTP_HEADER;
	}

	public Key getEncryptionKey() {
		return encryptionKey;
	}
	public void setEncryptionKey(Key encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	public Key getDecryptionKey() {
		return decryptionKey;
	}
	public void setDecryptionKey(Key decryptionKey) {
		this.decryptionKey = decryptionKey;
	}

	public String getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getPadType() {
		return padType;
	}
	public void setPadType(String padType) {
		this.padType = padType;
	}

	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getIv() {
		return iv;
	}
	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getIvLocation() {
		return ivLocation;
	}
	public void setIvLocation(String ivLocation) {
		this.ivLocation = ivLocation;
	}

	public String toString() {
		String keyData = null;
		if(getEncryptionKey() == getDecryptionKey()) {
			keyData = "Symmetric: " + getEncryptionKey().getClass().getName() + " [" + getEncryptionKey().getAlgorithm() + "/" + getEncryptionKey().getFormat() + "]";
		}
		else {
			keyData = "Asymmetric: Encryption: " + getEncryptionKey().getClass().getName() + " [" + getEncryptionKey().getAlgorithm() + "/" + getEncryptionKey().getFormat() + "], "
					+ "Decryption: " + getDecryptionKey().getClass().getName() + " [" + getDecryptionKey().getAlgorithm() + "/" + getDecryptionKey().getFormat() + "]";
		}
		keyData +=  " [ Keys (Hex): Decryption: \"" + Hex.encodeHexString(getDecryptionKey().getEncoded()) + "\"  Encryption: \"" + Hex.encodeHexString(getEncryptionKey().getEncoded()) + "\" ] ";
		return super.toString() + " [Cipher: " + getAlgorithm() + "/" + getMode() + "/" + getPadType() + "; IV: " + getIv() + "; Encoding: " + getEncoding() + ", Keys: ["  + keyData +"]]";
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}
}
