package com.sand.sandutil.security;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;
import com.sand.sandutil.tool.Base64Decoder;
import com.sand.sandutil.tool.Base64Encoder;
import com.sand.sandutil.tool.ByteUtil;


public class RSAHelpImpl implements RSAHelp {
	
	private static final String algorithm = "RSA";
	
	private static final String SIGNAlgorithm = "MD5";// SHA-1
	
	private static final String EDAlgorithm = "RSA/ECB/PKCS1Padding"; //RSA/ECB/PKCS1Padding,RSA/ECB/NoPadding
	
	private static final String signalgorithm = "MD5withRSA";/*SHA1WithRSA*/ /*MD5withRSA*/
	
	private String[] keyArray = new String[3];/*[0] e 公钥指数  ,[1] n 系数 , [2] d 私锁指数*/
	
	private boolean model = false;//是否生产模式
	
	public  RSAHelpImpl(boolean model){
		init(model);
	}
	
	public String getRootE(){
		if(keyArray[0] != null){
			return keyArray[0];
		}
		return null;
	}
	
	public String getRootN(){
		if(keyArray[1] != null){
			return keyArray[1];
		}
		return null;
	}
	
	private Signature signature = null;
	
	public RSAHelpImpl(){
		init(false);
	}
	
	private boolean init(boolean model) {
		try {	
			this.model = model;
			
			keyArray[0] = "65537";
			
			keyArray[1] = this.model?
						  "B6D363BFE1EDB743C20E4CCF09CE452E00E23FD2C20B0645A477D4CEAF01992B4585D44F4DB043784E2F2A8A673FF63A83B973EB817B169D892E4AA3118E74E857218087378D37386FEE01498E3C787DD56B3E90B9A3A169220DD2B6D0B35A5D2D48963C3D20ABF2AAA48916A0E106C7569BE232C63C5FC5E83F0D5E24313DCF"
					                             :
					      "D2F64C5D15BF54288281CFEAF37E949F39FB678E8BEA5936F6D22E47DA0516DC00C02C8B5BE413013FCBEAB563C57E697C81199BB9544E2047C341453BA57E1101F85DBD17BB1503B1D1E77496D168A7C89D7EC6A8C46A2755F3F9C2E92FD1817D2EDD66A94C0AB66F8932D2D230B40FEEC08F6C73391490867C7B7A7BCA8335";
		
			
			signature = Signature.getInstance(signalgorithm);
			return true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String sign(BigInteger d, BigInteger n, String plainText) {	
		try {
			//数字摘要
			MessageDig dig = new MessageDig();
			byte[] pbyte = dig.mDigest(plainText, SIGNAlgorithm,"UTF-8");//16字节
			//私钥加密摘要后的数据
			RSAPrivateKey priKey = (RSAPrivateKey)toKey(d,n,"private");
			String signStr = this.encrypt(priKey,pbyte);
			return signStr;
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			e1.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String sign(byte[] key, String plainText) {	
		try {
			//数字摘要
			MessageDig dig = new MessageDig();
			byte[] pbyte = dig.mDigest(plainText, SIGNAlgorithm,"UTF-8");//16字节
			String signStr = ByteUtil.bytes2hex(this.encryptByPrivateKey(pbyte, key));
			return signStr;
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			e1.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean verify(byte[] key, String plainText,String signedStr) {		
	    try {
	    	//数字摘要
	    	MessageDig dig = new MessageDig();
	    	byte[] pbyte = dig.mDigest(plainText, SIGNAlgorithm,"UTF-8");
	    	//公钥解密数据后摘要数据
	        byte[] data = ByteUtil.hex2bytes(signedStr);
            byte[] pbyte2  = this.decryptByPublicKey(data, key);
	        return Arrays.equals(pbyte, pbyte2);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			e1.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
}
	
	 
	
	public boolean verify(BigInteger e, BigInteger n, String plainText,String signedStr) {		
		    try {
		    	//数字摘要
		    	MessageDig dig = new MessageDig();
		    	byte[] pbyte = dig.mDigest(plainText, SIGNAlgorithm,"UTF-8");
		    	System.out.println(ByteUtil.bytes2hex(pbyte));
		    	//公钥解密数据后摘要数据
		        PublicKey pubKey = (PublicKey)toKey(e, n, "public");		 
	            byte[] pbyte2  = this.decrypt(pubKey, signedStr);
		        return Arrays.equals(pbyte, pbyte2);
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (InvalidKeySpecException e1) {
				e1.printStackTrace();
			} catch(Exception ex){
				ex.printStackTrace();
			}
			return false;
	}

	/**
	 * sign method
	 * @param priKey
	 * @param plainText
	 * @return string
	 * @throws Exception
	 */
	public String sign(PrivateKey priKey,String plainText) throws Exception {
		if( priKey != null && this.signature != null){
			signature.initSign(priKey);
			signature.update(plainText.getBytes("utf8"));
			byte[] signByte = signature.sign();
			return ByteUtil.bytes2hex(signByte);
		}
		return null;
	}
	
   /**
    * verify method
    * @param pubKey
    * @param plainText
    * @param signedStr
    * @return
    * @throws Exception
    */
	public boolean verify(PublicKey pubKey,String plainText, String signedStr)throws Exception {
		if(pubKey != null && plainText != null && signedStr != null){
			signature.initVerify(pubKey);
			signature.update(plainText.getBytes("utf8"));
			return signature.verify(ByteUtil.hex2bytes(signedStr));
		}
	     return false;
	}
	
	/**
	 * 加密
	 * @param plaintext
	 * @param eStr
	 * @param nStr
	 * @return
	 */
	public String encryptRSA(String plaintext, String eStr, String nStr) throws Exception{
				BigInteger e = new BigInteger(eStr);
				BigInteger n = new BigInteger(nStr);
				byte[] pb = plaintext.getBytes();
				return this.encryptRSA(pb, e, n);
	}
	
	/**
	 * 加密
	 * @param byte[] plaintext
	 * @param BigInteger e
	 * @param BigInteger n
	 * @param cstr
	 * @return
	 */
	public String encryptRSA(byte[] plaintext,BigInteger e,BigInteger n){
		if(plaintext != null){
			BigInteger m = new BigInteger(plaintext);
			BigInteger c = m.modPow(e, n);
			return c.toString(16);
		}
		return null;
	}
	
	/**
	 * encrypt method
	 * @param PublicKey publicKey 公锁
	 * @param byte[] plaintext 明文字节数组
	 * @return 密文十六进制字符串
	 */
	public String encrypt(BigInteger e,BigInteger en, byte[] plaintext) {
		if (e != null && en != null) {
			try {
				PublicKey publicKey = (PublicKey) toKey(e, en, "public");
				Cipher cipher = Cipher.getInstance(EDAlgorithm);
				cipher.init(Cipher.ENCRYPT_MODE, publicKey);
				byte[] bye = cipher.doFinal(plaintext);
				return ByteUtil.bytes2hex(bye);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * decrypt method
	 * @param PrivateKey privateKey 私锁
	 * @param cipherbyte 密文十六进制字符串
	 * @return byte[] 明文字节数组
	 */
	public byte[] decrypt(BigInteger d,BigInteger dn, String cipherstr){
		if (d != null && dn != null) {
			try {
				PrivateKey privateKey = (PrivateKey)toKey(d,dn,"private");
				Cipher cipher = Cipher.getInstance(EDAlgorithm);
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
				byte[] bye = ByteUtil.hex2bytes(cipherstr);
				return cipher.doFinal(bye);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Key toKey(BigInteger exponent,BigInteger modulus,String type) throws Exception{
		Key key = null;
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		if(type.equalsIgnoreCase("private")){
			RSAPrivateKeySpec prispec = new RSAPrivateKeySpec(modulus, exponent);
			key = keyFactory.generatePrivate(prispec);
		}else if(type.equalsIgnoreCase("public")){
			RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(modulus, exponent);
			key = keyFactory.generatePublic(pubSpec);
		}
		return key;
	}
	
	public String encrypt(PrivateKey privateKey, byte[] pbyte) {
		if (privateKey != null) {
			try {
				Cipher cipher= Cipher.getInstance(EDAlgorithm);
				cipher.init(Cipher.ENCRYPT_MODE, privateKey);
				byte[] bye = cipher.doFinal(pbyte);
				return ByteUtil.bytes2hex(bye);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return null;
	}
	
	public byte[] decrypt(PublicKey publicKey, String cipherStr) {
		if (publicKey != null) {
			try {
				byte[] cbyte = ByteUtil.hex2bytes(cipherStr);
				Cipher cipher = Cipher.getInstance(EDAlgorithm); 
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
				byte[] bye = cipher.doFinal(cbyte);
				return bye;
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 解密
	 * @param ciphertext
	 * @param dStr
	 * @param nStr
	 * @return String
	 */
	public String decryptRSA(String ciphertext, String dStr, String nStr) {
			BigInteger d = new BigInteger(dStr);
			BigInteger n = new BigInteger(nStr);
			byte[] mt = this.decryptRSA(ciphertext, d, n);
			if(mt != null){
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < mt.length; i++) {
					sb.append((char)mt[i]);
				}
				return sb.toString();	
			}
			return null;		
	}
	
	
	/**
	 * 解密
	 * @param ciphertext
	 * @param BigInteger d
	 * @param BigInteger n
	 * @return byte[]
	 */
	public byte[] decryptRSA(String ciphertext,BigInteger d,BigInteger n){
		if(ciphertext != null){
			BigInteger c = new  BigInteger(ciphertext,16);
			BigInteger m = c.modPow(d, n);
			byte[] mt = m.toByteArray();
			return mt;
		}
		return null;
	}
	
	
	/** 
     * 私钥加密 
     * @param data待加密数据 
     * @param key 密钥 
     * @return byte[] 加密数据 
     * */  
    public byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception{  
        //取得私钥   
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);  
        
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);  
        //生成私钥   
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);    
        //数据加密   
        Cipher cipher = Cipher.getInstance(EDAlgorithm);  
        
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);  
        
        return cipher.doFinal(data);  
    }  
    
    /** 
     * 公钥加密 
     * @param data待加密数据 
     * @param key 密钥 
     * @return byte[] 加密数据 
     * */  
    public  byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{  
        //实例化密钥工厂   
        KeyFactory keyFactory=KeyFactory.getInstance(algorithm);  
        //初始化公钥   
        //密钥材料转换   
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);  
        //产生公钥   
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);  
        //数据加密   
        Cipher cipher=Cipher.getInstance(EDAlgorithm);  
        
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);  
        
        return cipher.doFinal(data);  
    }
    
    
    /** 
     * 私钥解密 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密数据 
     * */  
    public  byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{  
        //取得私钥   
        PKCS8EncodedKeySpec pkcs8KeySpec=new PKCS8EncodedKeySpec(key);  
        
        KeyFactory keyFactory=KeyFactory.getInstance(algorithm);  
        //生成私钥   
        PrivateKey privateKey=keyFactory.generatePrivate(pkcs8KeySpec); 
        
        //数据解密 
        Cipher cipher=Cipher.getInstance(EDAlgorithm);  
        
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        
        return cipher.doFinal(data);  
    }  
    
    /** 
     * 公钥解密 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密数据 
     * */  
    public  byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception{
        //实例化密钥工厂   
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);  
        //初始化公钥   
        //密钥材料转换   
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);  
        //产生公钥   
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);      
//      System.out.println("m=" + ((RSAPublicKey)pubKey).getModulus().toString(16));        
//      System.out.println("n=" + ((RSAPublicKey)pubKey).getPublicExponent().toString(16));
        //数据解密 
        Cipher cipher = Cipher.getInstance(EDAlgorithm);  
        
        cipher.init(Cipher.DECRYPT_MODE, pubKey);  
        
        return cipher.doFinal(data);  
    }  
    
    
    public  byte[] getKeyOutWithHeaderAndFooter(String keyFilePath)throws Exception{ 
    	FileInputStream fin = new FileInputStream(keyFilePath);
//    	ByteArrayOutputStream bs = new ByteArrayOutputStream();
//    	int b = fin.read();
//    	bs.write(b);
//    	byte[] bufByte = new byte[512];
//    	int c = 0;
//    	while((c = fin.read(bufByte)) != -1){
//    		bs.write(bufByte,0,c);
//    	}
    	BASE64Decoder base64Decoder= new BASE64Decoder(); 
    	return base64Decoder.decodeBuffer(fin);
    }
    
    
    public  byte[] getKeyWithHeaderAndFooter(String keyFilePath)throws Exception{ 
    	FileInputStream fin = new FileInputStream(keyFilePath);
    	BufferedReader br = new BufferedReader(new InputStreamReader(fin));
    	StringBuffer sb = new StringBuffer(); 
    	String line  = "";
    	while((line = br.readLine()) != null){
    		line = line.trim();
    		if( line.equalsIgnoreCase("-----BEGIN PUBLIC KEY-----") ||line.equalsIgnoreCase("-----END PUBLIC KEY-----") ||//BASE64
    			line.equalsIgnoreCase("-----BEGIN RSA PRIVATE KEY-----") || line.equalsIgnoreCase("-----END RSA PRIVATE KEY-----") ||//BASE64
    			line.equalsIgnoreCase("-----BEGIN PRIVATE KEY-----") || line.equalsIgnoreCase("-----END PRIVATE KEY-----")){//PKCS8
    			
    			continue;
    		}else{
    			sb.append(line);
    		}
    	}
    	BASE64Decoder base64Decoder= new BASE64Decoder(); 
    	return base64Decoder.decodeBuffer(sb.toString());
    }
   
    /** 
     * 获得密钥
     * @param key 密钥对象 
     * @return byte[] 密钥字节 
     * */  
    public  byte[] getKey(Key key)throws Exception{  
        return key.getEncoded();  
    }  

	/**
	 * encrypt method
	 * @param privateKey 私锁
	 * @param String  明文字符串
	 * @return String 密文可视字符串
	 */
	public String encryptB64Str(PrivateKey privateKey, String plaintext) {
		if (privateKey != null && plaintext != null) {
			try {
				byte[] data = plaintext.getBytes();
				Cipher cipher = Cipher.getInstance(EDAlgorithm);
				cipher.init(Cipher.ENCRYPT_MODE, privateKey);
				byte[] b = cipher.doFinal(data);
				return Base64Encoder.encode(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * decryptB64Str method
	 * @param PublicKey publicKey 公锁
	 * @param String cipher64text 密文可视字符串
	 * @return 明文字符串
	 */
	public String decryptB64Str(PublicKey publicKey, String cipher64text) {
		if (publicKey != null) {
			try {
				byte[] cipherbyte = Base64Decoder.decode(cipher64text);
			    Cipher cipher = Cipher.getInstance(EDAlgorithm);
				cipher.init(Cipher.DECRYPT_MODE, publicKey);
				byte[] b = cipher.doFinal(cipherbyte);
				return new String(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public byte[] getPriKeyByCert(String certPath, String certPwd)throws Exception {
		byte[] privKey = null;
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream is = new FileInputStream(certPath);
			ks.load(is, certPwd.toCharArray());
			is.close();
			Enumeration enuma = ks.aliases();
			String keyAlias = null;
			if (enuma.hasMoreElements()) {
				keyAlias = (String) enuma.nextElement();
			}
			PrivateKey privatekey = (PrivateKey) ks.getKey(keyAlias, certPwd.toCharArray());
			privKey = privatekey.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return privKey;
	}

	//不包含-----BEGIN CERTIFICATE----- -----END CERTIFICATE----- 结构
	public byte[] getPubKeyByCert(String certPath) throws Exception {
		 byte[] pubKey = null;
		try{
			  CertificateFactory cff = CertificateFactory.getInstance("X.509");
		      FileInputStream certIn = new FileInputStream(certPath);
		      Certificate cf = cff.generateCertificate(certIn);
		      PublicKey publicKey = cf.getPublicKey();
		      pubKey = publicKey.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return pubKey;
	}

	/**
	 * @param filename
	 * @param type： 1-public 0-private
	 * @return key
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchProviderException 
	 */
	public Key loadKey(String merId,String keypath, int type) throws IOException,NoSuchAlgorithmException, InvalidKeySpecException {
		Properties props = new Properties();
		
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		 /*new URL("服务器上资源").openStream()*/
		props.load(new FileInputStream(keypath));
		/*判断商户号是否正确*/
		String loadId = new String(ByteUtil.hex2bytes(props.getProperty("merchantid")));
		if(merId == null || !merId.equals(loadId)){
			return null;
		} 
		if (type == 0) {
			String privateKeyValue = props.getProperty("privatekey");
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(ByteUtil.hex2bytes(privateKeyValue));
			PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
			return privateKey;
		} else {
			String privateKeyValue = props.getProperty("publickey");
			X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(ByteUtil.hex2bytes(privateKeyValue));
			PublicKey publicKey = keyFactory.generatePublic(bobPubKeySpec);
			return publicKey;
		}
	}
	
	public static void main(String []args)  throws Exception{}

}

