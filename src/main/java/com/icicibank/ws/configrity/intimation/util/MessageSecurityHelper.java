package com.icicibank.ws.configrity.intimation.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.c14n.InvalidCanonicalizerException;
import org.apache.xml.security.utils.XMLUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MessageSecurityHelper {
	private static final String CLASS_NAME = MessageSecurityHelper.class.getName();
	private static final Logger log = Logger.getLogger(CLASS_NAME);
	
	
	private static Map<String, KeyStore> SECURITY_KEYSTORES = new HashMap<>();
	
	// Canonical XML 2.0
	public static final String XML_DSIG_CANONICAL_METHOD_C14N2 			= "http://www.w3.org/2010/xml-c14n2";
	
	// Canonical XML 1.0 (omits comments) 									http://www.w3.org/TR/2001/REC-xml-c14n-20010315			
	// Apache Santuario Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS 
	public static final String XML_DSIG_CANONICAL_METHOD_C14N1 			= "http://www.w3.org/TR/2001/REC-xml-c14n-20010315";
	
	// Canonical XML 1.1 (omits comments)	 								http://www.w3.org/2006/12/xml-c14n11			
	// Apache Santuario Canonicalizer.ALGO_ID_C14N11_OMIT_COMMENTS 
	public static final String XML_DSIG_CANONICAL_METHOD_C14N11		= "http://www.w3.org/2006/12/xml-c14n11";
	
	// Exclusive XML Canonicalization 1.0 (omits comments) 	http://www.w3.org/2001/10/xml-exc-c14n#		
	// Apache Santuario Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS
	public static final String XML_DSIG_CANONICAL_METHOD_C14N1_EXCLUSIVE			
																													= "http://www.w3.org/2001/10/xml-exc-c14n#";
	
	// Canonical XML 1.0 with Comments 										http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments	
	// Apache Santuario Canonicalizer.ALGO_ID_C14N_WITH_COMMENTS 
	public static final String XML_DSIG_CANONICAL_METHOD_C14N1_WITH_COMMENTS 			
																													= "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments";
	
	// Canonical XML 1.1 with Comments 										http://www.w3.org/2006/12/xml-c14n11#WithComments	
	// Apache Santuario Canonicalizer.ALGO_ID_C14N11_WITH_COMMENTS 
	public static final String XML_DSIG_CANONICAL_METHOD_C14N11_WITH_COMMENTS 			
																													= "http://www.w3.org/2006/12/xml-c14n11#WithComments";

	// Exclusive XML Canonicalization 1.0 with Comments 		http://www.w3.org/2001/10/xml-exc-c14n#WithComments			
	// Apache Santuario Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS
	public static final String XML_DSIG_CANONICAL_METHOD_C14N1_WITH_COMMENTS_EXCLUSIVE 			
																													= "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
	
	static {
		org.apache.xml.security.Init.init();
		log.info("Apache security initialized.");
		Security.addProvider(new BouncyCastleProvider());
		log.info("Bouncy Castle security provider added.");
	}
	public MessageSecurityHelper() {
		super();
	}

	 public static String hashUsingPBKDF2(String data, byte[] salt,  Long iterations, Long keyLength) {
			String hashValue = null;
	        try {
	            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
	            PBEKeySpec spec = new PBEKeySpec(data.toCharArray(), salt, iterations.intValue(), keyLength.intValue());
	            
	            SecretKey key = skf.generateSecret(spec);
	            
	            byte[] res = key.getEncoded();

				hashValue = Hex.encodeHexString(res);
	        } 
			catch (NoSuchAlgorithmException nsae) {
				log.log(Level.SEVERE, "Failed to hash data. No such algorithm.", nsae);
			}
			catch (InvalidKeySpecException ikse) {
				log.log(Level.SEVERE, "Failed to hash data. Invalid key spec.", ikse);
			}
	        return hashValue;
	 }

	 public static String hashUsingPBKDF2_cs(String data, String salt,  Long iterations, Long keyLength, String charsetName) {
			String hashValue = null;
	        try {
	            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
	            PBEKeySpec spec = new PBEKeySpec(data.toCharArray(), salt.getBytes(charsetName), iterations.intValue(), keyLength.intValue());
	            
	            SecretKey key = skf.generateSecret(spec);
	            
	            byte[] res = key.getEncoded();

	            Hex hex = new Hex(charsetName);
				hashValue = new String(hex.encode(res), charsetName);
	        } 
			catch (NoSuchAlgorithmException nsae) {
				log.log(Level.SEVERE, "Failed to hash data. No such algorithm.", nsae);
			}
			catch (InvalidKeySpecException ikse) {
				log.log(Level.SEVERE, "Failed to hash data. Invalid key spec.", ikse);
			}
			catch (UnsupportedEncodingException une) {
				log.log(Level.SEVERE, "Failed to hash data. Unsupported character set encoding.", une);
			}
	        return hashValue;
	 }
	 
	// JSE 8 supports the following algo.
	//	    MD5
	//	    SHA-1
	//	    SHA-256
	// 	SHA-512
	public static String hash_cs(String algo, String data, String charsetName) {
		String hashValue = null;
		
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			byte[] hash = md.digest(data.getBytes(Charset.forName(charsetName)));
			
			Hex hex = new Hex(charsetName);
			hashValue = new String(hex.encode(hash), charsetName);
		} 
		catch (NoSuchAlgorithmException nsae) {
			log.log(Level.SEVERE, "Failed to hash data. No such algorithm.", nsae);
		}
		catch (UnsupportedEncodingException une) {
			log.log(Level.SEVERE, "Failed to hash data. Unsupported character set encoding.", une);
		}
		return hashValue;
	}
	
	public static String hash(String algo, byte[] data) {
		String hashValue = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			byte[] hash = md.digest(data);
			hashValue = Hex.encodeHexString(hash);
		} 
		catch (NoSuchAlgorithmException nsae) {
			log.log(Level.SEVERE, "Failed to hash data. No such algorithm.", nsae);
		}
		return hashValue;
	}

	public static String bc_hash(String algo, byte[] data) {
		String hashValue = null;
		try {
			
			MessageDigest md = MessageDigest.getInstance(algo, "BC");
			byte[] hash = md.digest(data);
			hashValue = Hex.encodeHexString(hash);
		} 
		catch (NoSuchAlgorithmException | NoSuchProviderException nsae) {
			log.log(Level.SEVERE, "Failed to hash data. No such algorithm.", nsae);
		}
		return hashValue;
	}
	
	// [13-Jul-2020]: Message Security (Checksum and Encryption)
	public static String canonicalizeXml(String xml, String canonicalAlgo, String xpath, Map<String, String> namespacesMap) {
		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        try {
	        	// TODO: 
	        	// [09-Aug-2020]: Commented out the FEATURE_SECURE_PROCESSING feature of now. 
	        	// Need to check which version of XMLConstants supports this constant.
				// dbf.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);
		        dbf.setNamespaceAware(true);

		        Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
		        log.info("Document created from source XML.");
		        
		        Canonicalizer c14n = Canonicalizer.getInstance(canonicalAlgo);
		        
		        log.info("Canonicalizer for \"" + canonicalAlgo + "\"  algo created [." + c14n + "]");

		        byte[] c14nBytes = null;
		        
		        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
		            if (xpath == null || xpath.trim().isEmpty()) {
		            	log.info("No XPath found. Entire XML will be canonicalized.");
		                c14n.canonicalizeSubtree(doc, baos);
		                c14nBytes = baos.toByteArray();
		            } 
		            else {
		            	log.info("XPath \"" + xpath + "\" found. XPath will be used to carve out the XML to be canonicalized.");
						log.info("Namespaces " + namespacesMap + " will be used to apply XPath to the XML input.");
						
		                XPathFactory xpf = XPathFactory.newInstance();
		                XPath xPath = xpf.newXPath();
		                
		                DSNamespaceContext namespaceContext =
		                    new DSNamespaceContext(namespacesMap);
		                xPath.setNamespaceContext(namespaceContext);

		                NodeList nl = (NodeList)xPath.evaluate(xpath, doc, XPathConstants.NODESET);

		                log.info("XPath applied. NodeList extracted. [" +nl +"]");
		                
		                c14n.canonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(nl), baos);
		                c14nBytes = baos.toByteArray();
		            }
		        }
		        
		        log.info("XML canonicalized conforming to \"" + canonicalAlgo+ "\" algo.");
		        return new String(c14nBytes);
			} 
	        catch (XPathExpressionException |
	        			CanonicalizationException | 
	        			ParserConfigurationException | 
	        			SAXException | 
	        			IOException | 
	        			InvalidCanonicalizerException e) {
				
	        	log.log(Level.SEVERE, "Failed to canonicalize XML using \""+ canonicalAlgo + "\" algo.", e);
			}
	        
	        return null;
	}
	
	public static class DSNamespaceContext implements NamespaceContext {

	    private Map<String, String> namespaceMap =
	        new HashMap<>();

	    public DSNamespaceContext() {
	        namespaceMap.put("ds", "http://www.w3.org/2000/09/xmldsig#");
	        namespaceMap.put("dsig", "http://www.w3.org/2000/09/xmldsig#");
	    }

	    public DSNamespaceContext(Map<String, String> namespaces) {
	        this();
	        namespaceMap.putAll(namespaces);
	    }

	    public String getNamespaceURI(String arg0) {
	        return namespaceMap.get(arg0);
	    }

	    public void putPrefix(String prefix, String namespace) {
	        namespaceMap.put(prefix, namespace);
	    }

	    public String getPrefix(String arg0) {
	        for (String key : namespaceMap.keySet()) {
	            String value = namespaceMap.get(key);
	            if (value.equals(arg0)) {
	                return key;
	            }
	        }
	        return null;
	    }

	    public Iterator<String> getPrefixes(String arg0) {
	        return namespaceMap.keySet().iterator();
	    }
	}
	
	// Data of length L, need multiples of length N.
	// Example: data is of length 249 bytes. Need multiples of 8 bytes. The pad will return 7. 
	// So now you need to add 7 bytes to you data that will make the data length 256 bytes which is now a multiple of 8.
	public static final Long pad(Long N, Long L) {
		Long pad =  N - L % N;
		if (pad < 0) {
			pad = pad * -1;
		}
		return pad;
	}
	
	private static final KeyStore getKeyStore(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword) {
		KeyStore keyStore = SECURITY_KEYSTORES.get(keyStoreName);
		if(keyStore != null) {
			log.info("Keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\" loaded from CACHE.");
			return keyStore;
		}
		
		File keyStoreFile = new File(keyStorePath, keyStoreName);
		
		if(!(keyStoreFile.exists() && keyStoreFile.isFile() && keyStoreFile.canRead())) {
			log.severe("Unable to access the keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". Check file path / name and permissions.");
		}
		else {
			log.info("Found keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". Keystore accessible.");

			try {
				keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(new FileInputStream(keyStoreFile), keyStorePassword.toCharArray());
				
				log.info("Keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\" loaded and CACHED.");
				
				SECURITY_KEYSTORES.put(keyStoreName, keyStore);
			} 
			catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException e) {
				log.severe("Unable to load the keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". [Root Cause: "  + e +"]");
			}
		}
		
		return keyStore;
	}
	
	private static PrivateKey getPrivateKey(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, String keyAliasName, String keyPassword) {
		KeyStore keyStore = getKeyStore(keyStoreType, keyStoreName, keyStorePath, keyStorePassword);
		if(keyStore == null) {
			return null;
		}
		
		PrivateKey privateKey = null;
		try {
			privateKey = (PrivateKey) keyStore.getKey(keyAliasName, (keyPassword == null ? keyStorePassword : keyPassword).toCharArray());
			if(privateKey == null) {
				log.severe("Private key with alias name \"" + keyAliasName + "\" not found in the keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". Check if the keystore contains a key with this alias name.");
				return null;
			}
			log.severe("Private key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\".");
		} 
		catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			log.severe("Unable to access the key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". [Root Cause: " + e +"]");
		}
		return privateKey;
	}

	public  static Certificate getPublicKeyCertificate(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, String keyAliasName) {
		KeyStore keyStore = getKeyStore(keyStoreType, keyStoreName, keyStorePath, keyStorePassword);
		if(keyStore == null) {
			return null;
		}
		
		Certificate publicKeyCertificate = null;
		try {
			publicKeyCertificate = keyStore.getCertificate(keyAliasName);
			if(publicKeyCertificate == null) {
				log.severe("Public key certificate with alias name \"" + keyAliasName + "\" not found in the keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". Check if the keystore contains a key with this alias name.");
				return null;
			}
			log.info("Public key certificate accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\".");
		} 
		catch (KeyStoreException e) {
			log.severe("Unable to access the public key certificate accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". [Root Cause: " + e +"]");
		}
		return publicKeyCertificate;
	}

	public static PublicKey getPublicKey(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, String keyAliasName) {
		Certificate publicKeyCertificate = getPublicKeyCertificate(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyAliasName);
		if(publicKeyCertificate == null) {
			return null;
		}
		
		return publicKeyCertificate.getPublicKey();
	}
	
	public static String getKeyStringify(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, Long keyType, String keyAliasName, String keyPassword, String encoding) {
		Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
		if(key == null) {
			return "";
		}
		return encode(key.getEncoded(), encoding);
	}
	
	public static Key getKey(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, Long keyType, String keyAliasName, String keyPassword) {
		KeyStore keyStore = getKeyStore(keyStoreType, keyStoreName, keyStorePath, keyStorePassword);
		if(keyStore == null) {
			return null;
		}

		Key key = null;
		String keyTypeName = "?????";
		
		try {
			switch(keyType.intValue()) {
				case Cipher.PUBLIC_KEY:
					key = getPublicKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyAliasName);
					keyTypeName = "PUBLIC";
					break;
				case Cipher.PRIVATE_KEY:
					keyTypeName = "PRIVATE";
					key = keyStore.getKey(keyAliasName, (keyPassword == null ? keyStorePassword : keyPassword).toCharArray());
					break;
				case Cipher.SECRET_KEY:
					keyTypeName = "SECRET";
					key = keyStore.getKey(keyAliasName, (keyPassword == null ? keyStorePassword : keyPassword).toCharArray());
					break;
			}
			
			if(key == null) {
				log.severe(keyTypeName + " key with alias name \"" + keyAliasName + "\" not found in the keystore  \"" + keyStoreName +
						"\" at \"" + keyStorePath + "\". Check if the keystore contains a " + keyTypeName + " key with this alias name.");
				return null;
			}
			
			log.info(keyTypeName + " key with alias name \"" + keyAliasName + "\" FOUND in the keystore  \"" + keyStoreName +
					"\" at \"" + keyStorePath + "\".");
		} 
		catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
			log.severe("Unable to access the " + keyTypeName + " key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". [Root Cause: " + e +"]");
		}
		return key;
	}

	public static String encode(byte[] data, String encoding) {
		if(data == null || data.length == 0) {
			return null;
		}
		
		if("base64".equals(encoding)) {
			return Base64.getEncoder().encodeToString(data);
		}
		else if("hex".equals(encoding)) {
			return Hex.encodeHexString(data);
		}

		return null;
	}

	public static byte[] decode(String data, String encoding) {
		if(data == null || data.length() == 0) {
			return null;
		}
		
		if("base64".equals(encoding)) {
			return Base64.getDecoder().decode(data);
		}
		else if("hex".equals(encoding)) {
			try {
				return Hex.decodeHex(data);
			}
			catch (DecoderException e) {
			}
		}
		return null;
	}

	// Initialization Vector (IV):
	// 	ECB 										java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV.
	// 	DES and DESede				java.security.InvalidAlgorithmParameterException: IV must be 8 bytes long.
	public static byte[] iv(String algo, Long noOfBytes) {
		byte[] iv = new byte[noOfBytes.intValue()];
		SecureRandom srandom = null;
		if(algo == null) {
			srandom = new SecureRandom();
		}
		else {
			try {
				srandom = SecureRandom.getInstance(algo);
			}
			catch (NoSuchAlgorithmException e) {
				log.severe("Unable to generate an IV. [Root Cause: " + e + "]");
			}
		}
		srandom.nextBytes(iv);
		return iv;
	}
	
	public static String ivHexString(String algo, Long noOfBytes) {
		byte[] iv = new byte[noOfBytes.intValue()];
		SecureRandom srandom = null;
		if(algo == null) {
			srandom = new SecureRandom();
		}
		else {
			try {
				srandom = SecureRandom.getInstance(algo);
			}
			catch (NoSuchAlgorithmException e) {
				log.severe("Unable to generate an IV. [Root Cause: " + e + "]");
				return null;
			}
		}
		srandom.nextBytes(iv);
		return encode(iv, "hex");
	}
	
	// All of the below information is in context to Oracle JDK 8.
	// Key Store Types: 
	//		jceks 	The proprietary keystore implementation provided by the SunJCE provider.
	//		jks 	The proprietary keystore implementation provided by the SUN provider.
	//		dks 	A domain keystore is a collection of keystores presented as a single logical keystore. It is specified by configuration data whose syntax is described in DomainLoadStoreParameter.
	//		pkcs11 	A keystore backed by a PKCS #11 token.
	//		pkcs12 	The transfer syntax for personal identity information as defined in PKCS #12: Personal Information Exchange Syntax v1.1.
	
	// Message Digest Algo:
	// https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#MessageDigest
	// 	MD2
	//	    MD5
	//	    SHA-1
	//	    SHA-256
	// 	SHA-384
	// 	SHA-512
	
	// Cipher:
	// https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
	// AES
	// 	To use the AES cipher with only one valid key size, use the format AES_<n>, where <n> can be 128, 192, or 256.
	// Blowfish
	// DES
	// DESede		Triple DES
	// ECIES
	// PBEWith<digest>And<encryption> 
	// 		Examples: 
	// 			PBEWithMD5AndDES 
	// PBEWith<prf>And<encryption>
	// RC2, RC4, RC5
	// RSA
	
	// Cipher Algorithm Modes:
	// 	CBC					Cipher Block Chaining Mode
	// 	CCM 				Counter/CBC Mode	
	// 	CFB					Cipher Feedback Mode
	// 	CTR		 			A simplification of OFB
	// 	CTS					Cipher Text Stealing
	// 	ECB					Electronic Codebook Mode
	// 	GCM 				Galois/Counter Mode
	// 	OFB, OFBx 		Output Feedback Mode
	// 	PCBC 				Propagating Cipher Block Chaining
	
	// Cipher Algorithm Padding:
	// 	NoPadding 															No padding
	// 	ISO10126Padding 												Padding for block ciphers
	// 	OAEPPadding														Optimal Asymmetric Encryption Padding
	// 	OAEPWith<digest>And<mgf>Padding 			Examples: OAEPWithMD5AndMGF1Padding and OAEPWithSHA-512AndMGF1Padding.
	// 	PKCS1Padding
	// 	PKCS5Padding
	// 	SSL3Padding
	
	//
	// WE WILL SUPPORT THE FOLLOWING TRANSFORMS:
	// https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
	
	//	"Blowfish",																				// (not a part of JDK 8 min req). (32 - 448 bits)
	//	"AES/CBC/NoPadding", 														//  (128)		
	//	"AES/CBC/PKCS5Padding", 													//  (128)		
	//	"AES/ECB/NoPadding", 														//  (128)
	//	"AES/ECB/PKCS5Padding"													//  (128)
	//	"DES/CBC/NoPadding", 														//  (56)
	//	"DES/CBC/PKCS5Padding", 													//  (56)
	//	"DES/ECB/NoPadding", 														//  (56)
	//	"DES/ECB/PKCS5Padding", 													//  (56)
	//	"DESede/CBC/NoPadding", 												//  (168)
	//	"DESede/CBC/PKCS5Padding", 											//  (168)
	//	"DESede/ECB/NoPadding", 												//  (168)
	//	"DESede/ECB/PKCS5Padding", 											//  (168)
	//
	
	// Padding Types:
	//	    Pad with bytes all of the same value as the number of padding bytes
	//	    Pad with 0x80 followed by zero bytes
	//	    Pad with zeroes except make the last byte equal to the number of padding bytes
	//	    Pad with zero (null) characters
	//	    Pad with space characters
	
	// Initialization Vector (IV):
	// 	ECB 										java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV.
	// 	DES and DESede				java.security.InvalidAlgorithmParameterException: IV must be 8 bytes long.
	
	public static final int PADDING_TYPE_PADDED_BYTE_NUMBER = 1;			//  (PKCS5 padding)
	public static final int PADDING_TYPE_0x80_FOLLOWED_BY_ZERO_BYTES = 2; 	// Pad with (0x80) followed by all (0x00)
	public static final int PADDING_TYPE_ZEROS_END_WITH_LAST_BYTE_NUMBER = 3; // Pad with (0x00) till the last but one byte. Then pad with the byte number. 
	public static final int PADDING_TYPE_NULL = 4;			// Pad with (0x00)
	public static final int PADDING_TYPE_SPACE = 5;			// Pad with (0x20)
	public static final int PADDING_TYPE_NONE = 0;

	// We will support only PADDING_TYPE_NULL and PADDING_TYPE_SPACE
	public static String encrypt(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
			Long keyType, String keyAliasName, String keyPassword,
			String message,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding, String encoding, 
			Long paddingType, String ivHex) {

			Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
	
			if(key == null) {
				return null;
			}

			return encryptWithKey(key, message, 
					cipherAlgo, cipherAlgoMode, cipherAlgoPadding,
					encoding, 
					paddingType, ivHex);
	}

	public static String encryptEncodedMessage(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
			Long keyType, String keyAliasName, String keyPassword,
			String encodedMessage,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding, String encoding, 
			Long paddingType, String ivHex) {

			Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
	
			if(key == null) {
				return null;
			}

			return encryptEncodedMessageWithKey(key, encodedMessage, 
					cipherAlgo, cipherAlgoMode, cipherAlgoPadding,
					encoding, 
					paddingType, ivHex);
	}

	public static Key genKey(String cipherAlgo, Long keySize) {
		KeyGenerator kg;
		try {
			kg = KeyGenerator.getInstance(cipherAlgo, "BC");
			kg.init(keySize.intValue());

			log.info("Key generator initialized. Key Algo: " + kg.getAlgorithm() + ", Provider: " + kg.getProvider() + "; Key Size: " + keySize);
			Key key = kg.generateKey();
			log.info("Key generated. Key Algo: " + key.getAlgorithm() + ", Format: " + key.getFormat() + ", Key Size: " + keySize);
			
			return  key;
		} 
		catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			log.severe("Unable to generate key. Key Algo: " + cipherAlgo + ", Key Size: " + keySize + "[Root Cause: " + e+"]");
		}
		return null;
	}
	
	//	"Blowfish",																				// Key size of Blowfish must be 32 - 448 bit and Block sizes: 64 bits
	//	"AES/CBC/NoPadding", 														//  (128)		
	//	"AES/CBC/PKCS5Padding",													//  (128)		
	//	"AES/ECB/NoPadding", 														//  (128)
	//	"AES/ECB/PKCS5Padding",													//  (128)
	//	"DES/CBC/NoPadding", 														//  (56)
	//	"DES/CBC/PKCS5Padding", 													//  (56)
	//	"DES/ECB/NoPadding", 														//  (56)
	//	"DES/ECB/PKCS5Padding", 													//  (56)
	//	"DESede/CBC/NoPadding", 												//  (168)
	//	"DESede/CBC/PKCS5Padding", 											//  (168)
	//	"DESede/ECB/NoPadding", 												//  (168)
	//	"DESede/ECB/PKCS5Padding"											//  (168)
	//	"RSA/ECB/PKCS1Padding", 													//  (1024, 2048)		
	//	"RSA/ECB/OAEPWithSHA-1AndMGF1Padding", 				//  (1024, 2048)
	//	"RSA/ECB/OAEPWithSHA-256AndMGF1Padding"	 		//  (1024, 2048)    	
	public static Long getKeySize(String algo) {
		int keySize = 0;
		if("Blowfish".equals(algo)) {
			keySize = 128;	// Key size of Blowfish must be 32 - 448 bit and Block sizes: 64 bits
		}
		else if("AES".equals(algo)) {
			keySize = 128;
		}
		else if("DES".equals(algo)) {
			keySize = 56;
		}
		else if("DESede".equals(algo)) {
			keySize = 168;
		}
		else if("RSA".equals(algo)) {
			keySize = 2048;
		}
		else if("DSA".equals(algo)) {
			keySize = 2048;
		}
		return new Long(keySize);
	}

	// Default Intialization Vector (IV) sizes.
	// 	ECB 										java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV.
	// 	DES and DESede				java.security.InvalidAlgorithmParameterException: IV must be 8 bytes long.
	// 	AES 										java.security.InvalidAlgorithmParameterException: IV must be 16 bytes long.
	public static Long getDefaultIVSize(String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding) {
		int ivSize = 0;
		// Cipher Algo.
		if("Blowfish".equals(cipherAlgo)) {
			ivSize = 64;
		}
		else if("AES".equals(cipherAlgo)) {
			ivSize = 128;
		}
		else if("DES".equals(cipherAlgo)) {
			ivSize = 64;
		}
		else if("DESede".equals(cipherAlgo)) {
			ivSize = 64;
		}
		else if("RSA".equals(cipherAlgo)) {
			ivSize = 2048;
		}
		else if("DSA".equals(cipherAlgo)) {
			ivSize = 2048;
		}
		if("ECB".equals(cipherAlgoMode)) {	// Cipher Algo Mode.
			ivSize = 0;
		}
		return new Long(ivSize);
	}

	
	public static String encryptSelfGenKey(String message,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding, String encoding, 
			Long paddingType, String ivHex, String[] output) {
		
			return encryptSelfGenKey2(message, 
					cipherAlgo, cipherAlgoMode, cipherAlgoPadding,
					encoding, 
					paddingType, ivHex, getKeySize(cipherAlgo), output);
	}

	public static String encryptSelfGenKey2(String message,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding, String encoding, 
			Long paddingType, String ivHex, Long keySize, String[] output) {
		
			Key key = genKey(cipherAlgo, keySize);

			if(key == null) {
				return null;
			}
			
			output[0] = encode(key.getEncoded(), "hex");

			return encryptWithKey(key, message, 
					cipherAlgo, cipherAlgoMode, cipherAlgoPadding,
					encoding, 
					paddingType, ivHex);
	}

	public static String encryptEncodedMessageWithKey(Key key,
			String encodedMessage,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding, String encoding, 
			Long paddingType, String ivHex) {
		
		String cipherTransformKey = cipherAlgo;

		if (cipherAlgoMode != null) {
			cipherTransformKey += "/" + cipherAlgoMode; 
		}
		if (cipherAlgoPadding != null) {
			cipherTransformKey += "/" + cipherAlgoPadding; 
		}
		
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance(cipherTransformKey, "BC");
			
			if(ivHex == null || "ECB".equals(cipherAlgoMode)) {      	// java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}
			else {
				IvParameterSpec ivspec = new IvParameterSpec(decode(ivHex, "hex"));
				cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
			}

			log.info("Cipher \"" + cipherTransformKey + "\" initialized.");
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | 
					InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
			log.severe("Unable to initialize the cipher \"" + cipherTransformKey + "\". Check the transformation algo and padding combination along with the key (key type) if they are valid. Few things do not mix. [Root Cause: " + e +"]");
			return null;
		}

    	// CBC and ECB are block ciphers, so the input must be a multiple of the block length. With the crypto provider  
    	// not doing padding, you need to ensure yourself that your input is a multiple of the block size, else
    	//  encryption / decryption will not be possible and you'll get this 
    	// javax.crypto.IllegalBlockSizeException: data not block size aligned error.
    	
    	// "Block size": The block size for DES is 64 bits or 8 bytes; For AES it 128 bits or 16 bytes.

		Long paddingLength = new Long(0);
		if(encodedMessage == null) {
			return null;
		}
		
		byte[] decodedMessage = decode(encodedMessage, encoding);
		
		int messageLength = decodedMessage.length;
		
		if("NoPadding".equals(cipherAlgoPadding)) {
			
			// We need padding.
			if (!((paddingType == PADDING_TYPE_NULL) || (paddingType == PADDING_TYPE_SPACE))) {
				log.severe("Currently supporting PADDING_TYPE_NULL and PADDING_TYPE_SPACE cipher algo modes for NoPadding. "
						+ "Cannot encrypt the message for transform type \"" + cipherAlgo + "/" + cipherAlgoMode + "/" + cipherAlgoPadding + "\".");
				return null;
			}
			
			if(!("CBC".equals(cipherAlgoMode) || "ECB".equals(cipherAlgoMode))) {
				log.severe("Currently supporting CBC and ECB cipher algo modes for NoPadding. "
						+ "Cannot encrypt the message for transform type \"" + cipherAlgo + "/" + cipherAlgoMode + "/" + cipherAlgoPadding + "\".");
				return null;
			}

			paddingLength = pad(new Long(cipher.getBlockSize()), new Long(messageLength));
			
			log.info("Padding message of length " + messageLength + " with " + paddingLength + ". Cipher Block Size = " + cipher.getBlockSize() + ".");
		}
		
		byte[] messageBytes = new byte[messageLength + paddingLength.intValue()];
		System.arraycopy(decodedMessage, 0, messageBytes, 0, messageLength);
		
		if(paddingType != null) {
			if (paddingType == PADDING_TYPE_NULL) {
				// no need to do anything. array is initialized with nulls.
			}
			else if (paddingType == PADDING_TYPE_SPACE) {
				for(int i = messageLength; i < messageBytes.length; i++) {
					try {
						messageBytes[i] = Hex.decodeHex("20")[0];  // 0x20 whitespace
					} 
					catch (DecoderException e) {
						return null;
					}
				}
			}
		}

		try {
			byte[] encryptedData = cipher.doFinal(messageBytes);
			
			log.info("Message ENCRYPTED successfully using cipher \"" + cipherTransformKey + ".");
			
			return encode(encryptedData, encoding);
			
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) {
				log.info("Failed to ENCRYPT message using cipher \"" + cipherTransformKey + 
						"\" using key [Root Cause: " + e + "]");
		}
		return null;
	}

	// We will support only PADDING_TYPE_NULL and PADDING_TYPE_SPACE
	public static String encryptWithKey(Key key,
									String message,
									String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding, String encoding, 
									Long paddingType, String ivHex) {
		
		String cipherTransformKey = cipherAlgo;

		if (cipherAlgoMode != null) {
			cipherTransformKey += "/" + cipherAlgoMode; 
		}
		if (cipherAlgoPadding != null) {
			cipherTransformKey += "/" + cipherAlgoPadding; 
		}
		
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance(cipherTransformKey, "BC");
			
			if(ivHex == null || "ECB".equals(cipherAlgoMode)) {      	// java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}
			else {
				IvParameterSpec ivspec = new IvParameterSpec(decode(ivHex, "hex"));
				cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
			}

			log.info("Cipher \"" + cipherTransformKey + "\" initialized.");
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | 
					InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
			log.severe("Unable to initialize the cipher \"" + cipherTransformKey + "\". Check the transformation algo and padding combination along with the key (key type) if they are valid. Few things do not mix. [Root Cause: " + e +"]");
			return null;
		}

    	// CBC and ECB are block ciphers, so the input must be a multiple of the block length. With the crypto provider  
    	// not doing padding, you need to ensure yourself that your input is a multiple of the block size, else
    	//  encryption / decryption will not be possible and you'll get this 
    	// javax.crypto.IllegalBlockSizeException: data not block size aligned error.
    	
    	// "Block size": The block size for DES is 64 bits or 8 bytes; For AES it 128 bits or 16 bytes.

		Long paddingLength = new Long(0);
		if(message == null) {
			return null;
		}
		int messageLength = message.getBytes().length;
		
		if("NoPadding".equals(cipherAlgoPadding)) {
			
			// We need padding.
			if (!((paddingType == PADDING_TYPE_NULL) || (paddingType == PADDING_TYPE_SPACE))) {
				log.severe("Currently supporting PADDING_TYPE_NULL and PADDING_TYPE_SPACE cipher algo modes for NoPadding. "
						+ "Cannot encrypt the message for transform type \"" + cipherAlgo + "/" + cipherAlgoMode + "/" + cipherAlgoPadding + "\".");
				return null;
			}
			
			if(!("CBC".equals(cipherAlgoMode) || "ECB".equals(cipherAlgoMode))) {
				log.severe("Currently supporting CBC and ECB cipher algo modes for NoPadding. "
						+ "Cannot encrypt the message for transform type \"" + cipherAlgo + "/" + cipherAlgoMode + "/" + cipherAlgoPadding + "\".");
				return null;
			}

			paddingLength = pad(new Long(cipher.getBlockSize()), new Long(messageLength));
			
			log.info("Padding message of length " + messageLength + " with " + paddingLength + ". Cipher Block Size = " + cipher.getBlockSize() + ".");
		}
		
		byte[] messageBytes = new byte[messageLength + paddingLength.intValue()];
		System.arraycopy(message.getBytes(), 0, messageBytes, 0, messageLength);
		
		if(paddingType != null) {
			if (paddingType == PADDING_TYPE_NULL) {
				// no need to do anything. array is initialized with nulls.
			}
			else if (paddingType == PADDING_TYPE_SPACE) {
				for(int i = messageLength; i < messageBytes.length; i++) {
					try {
						messageBytes[i] = Hex.decodeHex("20")[0];  // 0x20 whitespace
					} 
					catch (DecoderException e) {
						return null;
					}
				}
			}
		}

		try {
			byte[] encryptedData = cipher.doFinal(messageBytes);
			
			log.info("Message ENCRYPTED successfully using cipher \"" + cipherTransformKey + ".");
			
			return encode(encryptedData, encoding);
			
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) {
				log.info("Failed to ENCRYPT message using cipher \"" + cipherTransformKey + 
						"\" using key " + key.toString() + " [Root Cause: " + e + "]");
		}
		return null;
	}

	public static String decrypt(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
			Long keyType, String keyAliasName, String keyPassword,
			String encodedData, String encoding,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
			String ivHex) {
		
//			Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
//			
//			if(key == null) {
//				return null;
//			}
//	
//			return decryptWithKey(key, message, encoding, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, ivHex);
		
			byte[] raw= decryptRaw(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, 
													keyType, keyAliasName, keyPassword, encodedData, encoding, 
													cipherAlgo, cipherAlgoMode, cipherAlgoPadding, ivHex);
		
			return new String(raw);
	}

//	public static String decryptWithKey2(byte[] key, String message, String encoding,
//			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
//			String ivHex) {
//			
//    	SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
//		return decryptWithKey(originalKey, message, encoding, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, ivHex);
//	}
	
	public static String decryptWithKey(Key key, String message, String encoding,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
			String ivHex) {
			
			byte[] messageBytes = decode(message, encoding);
			if(messageBytes == null || messageBytes.length == 0) {
				return null;
			}
			
			String cipherTransformKey = cipherAlgo;
			
			if (cipherAlgoMode != null) {
				cipherTransformKey += "/" + cipherAlgoMode; 
			}
			if (cipherAlgoPadding != null) {
				cipherTransformKey += "/" + cipherAlgoPadding; 
			}
			
			Cipher cipher = null;
			
			try {
				cipher = Cipher.getInstance(cipherTransformKey, "BC");
				if(ivHex == null || "ECB".equals(cipherAlgoMode)) {      	// java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV
					cipher.init(Cipher.DECRYPT_MODE, key);
				}
				else {
					IvParameterSpec ivspec = new IvParameterSpec(decode(ivHex, "hex"));
					cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
				}
				
				log.severe("Cipher \"" + cipherTransformKey + "\" initialized.");
			} 
			catch (NoSuchAlgorithmException | NoSuchPaddingException | 
						InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
				log.severe("Unable to initialize the cipher \"" + cipherTransformKey + "\". Check the transformation algo and padding combination along with the key (key type) if they are valid. Few things do not mix. [Root Cause: " + e +"]");
				return null;
			}
			
			try {
				byte[] messagePlainTextBytes = cipher.doFinal(messageBytes);
			
				log.info("Message DECRYPTED successfully using cipher \"" + cipherTransformKey + ".");
			
				return new String(messagePlainTextBytes);
			} 
			catch (IllegalBlockSizeException | BadPaddingException e) {
				log.info("Failed to DECRYPT message using cipher \"" + cipherTransformKey + "\" using key " + key.toString() + " [Root Cause: " + e + "]");
			}
			return null;
		}

	// Signature Algorithms
	// https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Signature
	// 	NONEwithRSA
	// 	MD2withRSA
	// 	MD5withRSA
	// 	SHA1withRSA					RSAwithSHA1 												http://www.w3.org/2000/09/xmldsig#rsa-sha1
	// 	SHA256withRSA				RSAwithSHA256 											http://www.w3.org/2001/04/xmldsig-more#rsa-sha256 [RFC6931]
	// 	SHA384withRSA
	//  	SHA512withRSA				RSAwithSHA512 											http://www.w3.org/2001/04/xmldsig-more#rsa-sha512
	//		SHA1withDSA					DSAwithSHA1 (signature verification) 		http://www.w3.org/2000/09/xmldsig#dsa-sha1 [RFC6931]
	//		SHA256withDSA				DSAwithSHA256 											http://www.w3.org/2009/xmldsig11#dsa-sha256
	//		SHA384withDSA
	//		SHA512withDSA 
	public static String sign(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
									String keyAliasName, String keyPassword,
									String signatureAlgo,
									String message) {
		
		PrivateKey key = getPrivateKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyAliasName, keyPassword);
		if(key == null) {
			return null;
		}
		
		Signature signature = null;
		try {
			signature = Signature.getInstance(signatureAlgo, "BC");
			signature.initSign(key);
			
			log.info("Initialized signature with \""+ signatureAlgo +"\" and key having alias \"" + keyAliasName +"\" taken from key store \""+ keyStoreName+"\" at \"" + keyStorePath+"\".");
		} 
		catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException e) {
			log.info("Failed to initialize signature with \""+ signatureAlgo +"\" and key having alias \"" + keyAliasName +"\" taken from key store \""+ keyStoreName+"\" at \"" + keyStorePath+"\". [Root Cause: " + e +"]");
		}
		
		try {
			signature.update(message.getBytes());
			byte[] digitalSignature = signature.sign();
			
			log.info("Message SIGNED successfully using signature algo \"" + signatureAlgo + 
					"\" using a key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\".");
			
			return Hex.encodeHexString(digitalSignature);
		} 
		catch (SignatureException e) {
			log.info("Failed to SIGN message using signature algo \"" + signatureAlgo +
					"\" using a key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". [Root Cause: " + e + "]");
		}
		
		return null;
	}
	
	public static Boolean sign_verify(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
			String keyAliasName, String keyPassword,
			String signatureAlgo,
			String message,
			String receivedSignature) {

		boolean isDigitalSignatureValid = false;
		PublicKey publicKey = getPublicKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyAliasName);

		if(publicKey == null) {
			return isDigitalSignatureValid;
		}

		Signature signature = null;
		try {
			signature = Signature.getInstance(signatureAlgo, "BC");
			signature.initVerify(publicKey);
		
			log.info("Initialized signature with \""+ signatureAlgo +"\" and key having alias \"" + keyAliasName +"\" taken from key store \""+ keyStoreName+"\" at \"" + keyStorePath+"\".");
		} 
		catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException e) {
			log.info("Failed to initialize signature with \""+ signatureAlgo +"\" and key having alias \"" + keyAliasName +"\" taken from key store \""+ keyStoreName+"\" at \"" + keyStorePath+"\". [Root Cause: " + e +"]");
			return isDigitalSignatureValid;
		}

		try {
			log.info(">>>>>>>>>>>>>>>>> Message: " + message);
			log.info(">>>>>>>>>>>>>>>>> Received Signature: " + receivedSignature);
			signature.update(message.getBytes());
			isDigitalSignatureValid = signature.verify(Hex.decodeHex(receivedSignature));
		
		log.info("Message signature VERIFIED successfully using signature algo \"" + signatureAlgo + 
				"\" using a key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + 
				"\". Signature is " + (isDigitalSignatureValid ? "VALID." : "INVALID."));
		
		} 
		catch (SignatureException | DecoderException e) {
			log.info("Failed to validate the signature of message using signature algo \"" + signatureAlgo + 
						"\" using a key accessed from keystore  \"" + keyStoreName + "\" at \"" + keyStorePath + "\". [Root Cause: " + e + "]");
		}
		
		return isDigitalSignatureValid;
	}

	public static String encryptRaw(String keyStoreType, String keyStoreName,
			String keyStorePath, String keyStorePassword, Long keyType,
			String keyAliasName, String keyPassword, byte[] raw,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
			String encoding, Long paddingType, String ivHex) {
		
		Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
		
		if(key == null) {
			return null;
		}
		
		return encryptRawWithKey(key, raw, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, encoding, paddingType, ivHex);
	}
		public static String encryptRawWithKey(Key key, byte[] raw,
				String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
				String encoding, Long paddingType, String ivHex) {
			
		String cipherTransformKey = cipherAlgo;

		if (cipherAlgoMode != null) {
			cipherTransformKey += "/" + cipherAlgoMode; 
		}
		if (cipherAlgoPadding != null) {
			cipherTransformKey += "/" + cipherAlgoPadding; 
		}
		
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance(cipherTransformKey, "BC");
			
			if(ivHex == null || "ECB".equals(cipherAlgoMode)) {      	// java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV
				cipher.init(Cipher.ENCRYPT_MODE, key);
			}
			else {
				IvParameterSpec ivspec = new IvParameterSpec(decode(ivHex, "hex"));
				cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
			}

			log.info("Cipher \"" + cipherTransformKey + "\" initialized.");
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | 
					InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
			log.severe("Unable to initialize the cipher \"" + cipherTransformKey + "\". Check the transformation algo and padding combination along with the key (key type) if they are valid. Few things do not mix. [Root Cause: " + e +"]");
			return null;
		}

    	// CBC and ECB are block ciphers, so the input must be a multiple of the block length. With the crypto provider  
    	// not doing padding, you need to ensure yourself that your input is a multiple of the block size, else
    	//  encryption / decryption will not be possible and you'll get this 
    	// javax.crypto.IllegalBlockSizeException: data not block size aligned error.
    	
    	// "Block size": The block size for DES is 64 bits or 8 bytes; For AES it 128 bits or 16 bytes.

		Long paddingLength = new Long(0);
		int messageLength = raw.length;
		
		if("NoPadding".equals(cipherAlgoPadding)) {
			
			// We need padding.
			if (!((paddingType == PADDING_TYPE_NULL) || (paddingType == PADDING_TYPE_SPACE))) {
				log.severe("Currently supporting PADDING_TYPE_NULL and PADDING_TYPE_SPACE cipher algo modes for NoPadding. "
						+ "Cannot encrypt the message for transform type \"" + cipherAlgo + "/" + cipherAlgoMode + "/" + cipherAlgoPadding + "\".");
				return null;
			}
			
			if(!("CBC".equals(cipherAlgoMode) || "ECB".equals(cipherAlgoMode))) {
				log.severe("Currently supporting CBC and ECB cipher algo modes for NoPadding. "
						+ "Cannot encrypt the message for transform type \"" + cipherAlgo + "/" + cipherAlgoMode + "/" + cipherAlgoPadding + "\".");
				return null;
			}

			paddingLength = pad(new Long(cipher.getBlockSize()), new Long(messageLength));
			
			log.info("Padding message of length " + messageLength + " with " + paddingLength + ". Cipher Block Size = " + cipher.getBlockSize() + ".");
		}
		
		byte[] messageBytes = new byte[messageLength + paddingLength.intValue()];
		System.arraycopy(raw, 0, messageBytes, 0, messageLength);
		
		if(paddingType != null) {
			if (paddingType == PADDING_TYPE_NULL) {
				// no need to do anything. array is initialized with nulls.
			}
			else if (paddingType == PADDING_TYPE_SPACE) {
				for(int i = messageLength; i < messageBytes.length; i++) {
					try {
						messageBytes[i] = Hex.decodeHex("20")[0];  // 0x20 whitespace
					} 
					catch (DecoderException e) {
						return null;
					}
				}
			}
		}

		try {
			byte[] encryptedData = cipher.doFinal(messageBytes);
			
			log.info("Message ENCRYPTED successfully using cipher \"" + cipherTransformKey + ".");
			
			return encode(encryptedData, encoding);
			
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) {
				log.info("Failed to ENCRYPT message using cipher \"" + cipherTransformKey + 
						"\" using key " + key.toString() + " [Root Cause: " + e + "]");
		}
		return null;
	}

	public static String decryptEncode(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
			Long keyType, String keyAliasName, String keyPassword,
			String encodedData, String encoding,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
			String ivHex) {

		Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
		
		if(key == null) {
			return null;
		}
		
		return encode(decryptRaw(key, encodedData, encoding, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, ivHex), encoding);
	}

	public static byte[] decryptRaw(String keyStoreType, String keyStoreName, String keyStorePath, String keyStorePassword, 
			Long keyType, String keyAliasName, String keyPassword,
			String encodedData, String encoding,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
			String ivHex) {

		Key key = getKey(keyStoreType, keyStoreName, keyStorePath, keyStorePassword, keyType, keyAliasName, keyPassword);
		
		if(key == null) {
			return null;
		}
		
		return decryptRaw(key, encodedData, encoding, cipherAlgo, cipherAlgoMode, cipherAlgoPadding, ivHex);
	}
	
	public static byte[] decryptRaw(Key key,
			String encodedData, String encoding,
			String cipherAlgo, String cipherAlgoMode, String cipherAlgoPadding,
			String ivHex) {

		byte[] messageBytes = decode(encodedData, encoding);
		if(messageBytes == null || messageBytes.length == 0) {
			return null;
		}
		
		String cipherTransformKey = cipherAlgo;
		
		if (cipherAlgoMode != null) {
			cipherTransformKey += "/" + cipherAlgoMode; 
		}
		if (cipherAlgoPadding != null) {
			cipherTransformKey += "/" + cipherAlgoPadding; 
		}
		
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance(cipherTransformKey, "BC");
			if(ivHex == null || "ECB".equals(cipherAlgoMode)) {      	// java.security.InvalidAlgorithmParameterException: ECB mode does not use an IV
				cipher.init(Cipher.DECRYPT_MODE, key);
			}
			else {
				IvParameterSpec ivspec = new IvParameterSpec(decode(ivHex, "hex"));
				cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
			}
			
			log.severe("Cipher \"" + cipherTransformKey + "\" initialized.");
		} 
		catch (NoSuchAlgorithmException | NoSuchPaddingException | 
					InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
			log.severe("Unable to initialize the cipher \"" + cipherTransformKey + "\". Check the transformation algo and padding combination along with the key (key type) if they are valid. Few things do not mix. [Root Cause: " + e +"]");
			return null;
		}
		
		try {
			byte[] decryptedRaw = cipher.doFinal(messageBytes);
		
			log.info("Message DECRYPTED successfully using cipher \"" + cipherTransformKey + ".");
		
			return decryptedRaw;
		} 
		catch (IllegalBlockSizeException | BadPaddingException e) {
			log.info("Failed to DECRYPT message using cipher \"" + cipherTransformKey + "\" using key " + key.toString() + " [Root Cause: " + e + "]");
		}
		return null;
	}
}
