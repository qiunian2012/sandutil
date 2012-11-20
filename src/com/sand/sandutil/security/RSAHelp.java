package com.sand.sandutil.security;

import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface RSAHelp {

	public String getRootE();
	
	public String getRootN();
	
	/**
	 *  sign method1
	 * @param BigInteger d
	 * @param BigInteger n
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public String sign(BigInteger d,BigInteger n,String plainText);
	/**
	 * sign method2
	 * @param priKey
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public String sign(PrivateKey priKey,String plainText) throws Exception;
	
	 /**
	  * verify method1
	  * @param BigInteger e
	  * @param BigIneger n
	  * @param plainText
	  * @param signedStr
	  * @return true or false
	  * @throws Exception
	 */
	public boolean verify(BigInteger e, BigInteger n,String plainText, String signedStr);
	
   /**
    * verify method2
    * @param pubKey
    * @param plainText
    * @param signedStr
    * @return true or false
    * @throws Exception
    */
	public boolean verify(PublicKey pubKey,String plainText, String signedStr)throws Exception;
	
	
	/**
	 * 加密
	 * @param byte[] plaintext
	 * @param BigInteger e
	 * @param BigInteger n
	 * @param cstr
	 * @return
	 */
	public String encryptRSA(byte[] plaintext,BigInteger e,BigInteger n);
	/**
	 * 加密
	 * @param plaintext
	 * @param eStr
	 * @param nStr
	 * @return
	 */
	public String encryptRSA(String plaintext, String eStr, String nStr)throws Exception;
	
	
	/**
	 * 解密
	 * @param ciphertext
	 * @param BigInteger d
	 * @param BigInteger n
	 * @return byte[]
	 */
	public byte[] decryptRSA(String ciphertext,BigInteger d,BigInteger n);
	
	/**
	 * 解密
	 * @param ciphertext
	 * @param dStr
	 * @param nStr
	 * @return
	 */
	public String decryptRSA(String ciphertext, String dStr, String nStr);
	
	/**
	 * 公钥加密
	 * @param e
	 * @param n
	 * @param plaintext
	 * @return
	 */
	public String encrypt(BigInteger e,BigInteger n, byte[] plaintext);

	/**
	 * 私钥解密
	 * @param d
	 * @param n
	 * @param cipherstr
	 * @return
	 */
	public byte[] decrypt(BigInteger d,BigInteger n, String cipherstr);
	
    /**
     * 私钥加密
     * @param privateKey 私钥对象
     * @param pbyte 明文字节数组
     * @return 密文字符串
     */
	public String encrypt(PrivateKey privateKey, byte[] pbyte);
	
	/**
	 * 公钥解密
	 * @param publicKey 公钥对象
	 * @param cipherStr 密文字符串
	 * @return 明文字节数组
	 */
	public byte[] decrypt(PublicKey publicKey, String cipherStr);
	
	
	/** 
     * 私钥加密 
     * @param data待加密数据 
     * @param key 密钥 
     * @return byte[] 加密数据 
     * */ 
	public byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception;
	
	
	   /** 
     * 公钥加密 
     * @param data待加密数据 
     * @param key 密钥 
     * @return byte[] 加密数据 
     * */  
    public  byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception;
    
    
    /** 
     * 私钥解密 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密数据 
     * */  
    public  byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception;
    
    
    /** 
     * 公钥解密 
     * @param data 待解密数据 
     * @param key 密钥 
     * @return byte[] 解密数据 
     * */  
    public  byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception;
    
    /** 
     * 获得密钥
     * @param key 密钥对象 
     * @return byte[] 密钥字节 
     * */ 
    public byte[] getKey(Key key)throws Exception;
    
    /**
     * @param certPath X509证书路径
     * @return 公钥
     * @throws Exception
     */
    public byte[] getPubKeyByCert(String certPath)throws Exception;
    
    /**
     * @param certPath Pkcs12证书
     * @return 私钥
     * @throws Exception
     */
    public byte[] getPriKeyByCert(String certPath,String certPwd)throws Exception;
    
    
    public String encryptB64Str(PrivateKey privateKey, String plaintext);
    
	public String decryptB64Str(PublicKey publicKey, String cipher64text);
	 
	 
	 
}
