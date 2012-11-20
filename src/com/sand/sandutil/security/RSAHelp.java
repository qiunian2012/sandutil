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
	 * ����
	 * @param byte[] plaintext
	 * @param BigInteger e
	 * @param BigInteger n
	 * @param cstr
	 * @return
	 */
	public String encryptRSA(byte[] plaintext,BigInteger e,BigInteger n);
	/**
	 * ����
	 * @param plaintext
	 * @param eStr
	 * @param nStr
	 * @return
	 */
	public String encryptRSA(String plaintext, String eStr, String nStr)throws Exception;
	
	
	/**
	 * ����
	 * @param ciphertext
	 * @param BigInteger d
	 * @param BigInteger n
	 * @return byte[]
	 */
	public byte[] decryptRSA(String ciphertext,BigInteger d,BigInteger n);
	
	/**
	 * ����
	 * @param ciphertext
	 * @param dStr
	 * @param nStr
	 * @return
	 */
	public String decryptRSA(String ciphertext, String dStr, String nStr);
	
	/**
	 * ��Կ����
	 * @param e
	 * @param n
	 * @param plaintext
	 * @return
	 */
	public String encrypt(BigInteger e,BigInteger n, byte[] plaintext);

	/**
	 * ˽Կ����
	 * @param d
	 * @param n
	 * @param cipherstr
	 * @return
	 */
	public byte[] decrypt(BigInteger d,BigInteger n, String cipherstr);
	
    /**
     * ˽Կ����
     * @param privateKey ˽Կ����
     * @param pbyte �����ֽ�����
     * @return �����ַ���
     */
	public String encrypt(PrivateKey privateKey, byte[] pbyte);
	
	/**
	 * ��Կ����
	 * @param publicKey ��Կ����
	 * @param cipherStr �����ַ���
	 * @return �����ֽ�����
	 */
	public byte[] decrypt(PublicKey publicKey, String cipherStr);
	
	
	/** 
     * ˽Կ���� 
     * @param data���������� 
     * @param key ��Կ 
     * @return byte[] �������� 
     * */ 
	public byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception;
	
	
	   /** 
     * ��Կ���� 
     * @param data���������� 
     * @param key ��Կ 
     * @return byte[] �������� 
     * */  
    public  byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception;
    
    
    /** 
     * ˽Կ���� 
     * @param data ���������� 
     * @param key ��Կ 
     * @return byte[] �������� 
     * */  
    public  byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception;
    
    
    /** 
     * ��Կ���� 
     * @param data ���������� 
     * @param key ��Կ 
     * @return byte[] �������� 
     * */  
    public  byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception;
    
    /** 
     * �����Կ
     * @param key ��Կ���� 
     * @return byte[] ��Կ�ֽ� 
     * */ 
    public byte[] getKey(Key key)throws Exception;
    
    /**
     * @param certPath X509֤��·��
     * @return ��Կ
     * @throws Exception
     */
    public byte[] getPubKeyByCert(String certPath)throws Exception;
    
    /**
     * @param certPath Pkcs12֤��
     * @return ˽Կ
     * @throws Exception
     */
    public byte[] getPriKeyByCert(String certPath,String certPwd)throws Exception;
    
    
    public String encryptB64Str(PrivateKey privateKey, String plaintext);
    
	public String decryptB64Str(PublicKey publicKey, String cipher64text);
	 
	 
	 
}
