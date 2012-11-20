package com.sand.sandutil.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import com.sand.sandutil.tool.ByteUtil;


public class SandPayDes {
	
	private static final String  Algorithm = "DESede";//DES, DESede
	
	//DESede/ECB/SSL3Padding,DESede/ECB/NoPadding,DESede/ECB/PKCS5Padding(默认填充方式)
	private static final String  EDAlgorithm = "DESede/ECB/NoPadding";
	
	private static final String initSeed = "sandpay";

	private byte[] deskey;

	public SandPayDes(String seed) {
		init(seed);
	}

	public SandPayDes() {
		init(initSeed);
	}

	public byte[] getDeskey() {
		return this.deskey;
	}

	private void init(String seed) {
		if (this.deskey != null)
			return;
		try {
			Key skey = generateKey(seed);
			this.deskey = skey.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Key generateKey(String seed) {
		try {
			KeyGenerator keygen = null;
			keygen = KeyGenerator.getInstance(Algorithm);
			SecureRandom random = new SecureRandom();
			random.nextBytes(seed.getBytes());
			keygen.init(112, random);//DES - >56,DESede -> 112(key1+key2+key1),DESede -> 168(key1,key2,key3)
			Key skey = keygen.generateKey();
			return skey;
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		}
		return null;
	}

	
	private SecretKey toKey(byte[] key) {
		try {
			KeySpec dks = new DESedeKeySpec(key);// 这里改为了 DESKeySpec
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
			return keyFactory.generateSecret(dks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String decrypt(String desMess, byte[] key) {
		if ((desMess == null) || (key == null)) {
			return null;
		}
		byte[] aMess = ByteUtil.hex2bytes(desMess);
		try {
			Cipher c1 = Cipher.getInstance(EDAlgorithm);
			Key secretKey = toKey(key);
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] desDMess = fillDesData(aMess);
			byte[] cipherByte = c1.doFinal(desDMess);
			String res = new String(cipherByte,"utf8");
			return subFrontLen(res,4);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public byte[] endes(byte[] cleanMess, byte[] key) {
		if ((cleanMess == null) || (cleanMess.length == 0)|| (key == null))
			return null;
		try {
			Cipher c1 =  Cipher.getInstance("DES");
			KeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			Key secretKey = keyFactory.generateSecret(dks);
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] cipherByte = c1.doFinal(cleanMess);
			return cipherByte;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public byte[] dndes(byte[] cleanMess, byte[] key) {
		if ((cleanMess == null) || (cleanMess.length == 0)|| (key == null))
			return null;
		try {
			Cipher c1 =  Cipher.getInstance("DES");
			KeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			Key secretKey = keyFactory.generateSecret(dks);
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] cipherByte = c1.doFinal(cleanMess);
			return cipherByte;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String encrypt(String cleanMess, byte[] key) {
		if ((cleanMess == null) || (cleanMess.length() == 0)|| (key == null))
			return null;
		try {
			Cipher c1 =  Cipher.getInstance(EDAlgorithm);
			Key secretKey = toKey(key) ;
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			cleanMess = fillFrontLen(cleanMess,4);
			byte[] desEMess = fillDesData(cleanMess.getBytes("utf8"));
			byte[] cipherByte = c1.doFinal(desEMess);
			return ByteUtil.bytes2hex(cipherByte);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String decr(String desMess, byte[] key) {
		if ((desMess == null) || (key == null)) {
			return null;
		}
		byte[] aMess = ByteUtil.hex2bytes(desMess);
		try {
			Cipher c1 = Cipher.getInstance(EDAlgorithm);
			Key secretKey = toKey(key);
			c1.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] desDMess = fillDesData(aMess);
			byte[] cipherByte = c1.doFinal(desDMess);
			cipherByte = sub(cipherByte, 4);
			String res = new String(cipherByte,"utf8");
			return res;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String encr(String cleanMess, byte[] key) {
		if ((cleanMess == null) || (cleanMess.length() == 0)|| (key == null))
			return null;
		try {
			Cipher c1 =  Cipher.getInstance(EDAlgorithm);
			Key secretKey = toKey(key) ;
			c1.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] desEMess = fill(cleanMess.getBytes("utf8"),4);
			byte[] cipherByte = c1.doFinal(desEMess);
			return ByteUtil.bytes2hex(cipherByte);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private byte[] fill(byte[] srcByte,int filllen){
		int blen = srcByte.length;
		String lStr = "" + blen;
		for(; lStr.length() < filllen;){
			lStr = "0" + lStr;
		}
		srcByte =  ByteUtil.union(lStr.getBytes(), srcByte);
		int nn = 8 - srcByte.length % 8;
		if (nn != 8) {
			byte[] tbs = new byte[nn];
			for (int i = 0; i < nn; i++)
				tbs[i] = 0x00;
			srcByte = ByteUtil.union(srcByte, tbs);
		}
		return srcByte;
	}
	
	private byte[] sub(byte[] srcByte,int sublen){
		byte[] blen = ByteUtil.sub(srcByte, 0, sublen);
		int len = Integer.parseInt(new String(blen));
		return ByteUtil.sub(srcByte, 4, len);
	}

	//去掉长度
	private String subFrontLen(String src,int sublen){
		int len = Integer.parseInt(src.substring(0, sublen));
		src = src.substring(sublen, len+sublen);
		return src;
	}
	
	//填充长度
	private String fillFrontLen(String src,int filllen){
		String lStr = "" + src.length();
		for(; lStr.length() < filllen;){
			lStr = "0" + lStr;
		}
		return lStr + src;
	}
	
	/**
	 * 数据填充为des需要的8的倍数，不足在后面填充0x00
	 * @param data
	 * @return byte[]
	 */
	private byte[] fillDesData(byte[] data) {
		int nn = 8 - data.length % 8;
		if (nn != 8) {
			byte[] tbs = new byte[nn];
			for (int i = 0; i < nn; i++)
				tbs[i] = 0x00;
			data = ByteUtil.union(data, tbs);
		}
		return data;
	}
	
	public static void main(String[] args) throws Exception{}
	
}
