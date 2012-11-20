package com.sand.sandutil.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.sand.sandutil.tool.Base64Encoder;
import com.sand.sandutil.tool.ByteUtil;

public class MessageDig {

	public MessageDig() {
    }
    
	//alg= MD5 SHA SHA-1
	public byte[] mDigest(String s,String alg,String charset) {
		try {
			byte[] strTemp = s.getBytes(charset);
			MessageDigest mdTemp = MessageDigest.getInstance(alg);
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			return md;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    static MessageDigest getDigest(String alg) {
        try {
            return MessageDigest.getInstance(alg);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] md5(byte[] data) {
        return getDigest("MD5").digest(data);
    }


    public static byte[] md5(String data) {
        return md5(data.getBytes());
    }
    
    public static byte[] md5(String data,String charset) {
        try {
			return md5(data.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }

    public static String md5Base64(byte[] data) {
        return Base64Encoder.encode((md5(data)));
    }
   
    public static String md5Hex(byte[] data) {
        return ByteUtil.bytes2hex(md5(data));
    }
    
    public static String md5Hex(String data,String charset) {
        return ByteUtil.bytes2hex(md5(data));
    }
}
