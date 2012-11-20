package com.sand.sandutil.encrymachine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryMachineSocket {
	
	private  static Log log = LogFactory.getLog(EncryMachineSocket.class);
	
	public static final int timeout = 30000;
	
	public static final int buffer = 1024;
	
	public static byte[] socketEncryMachine(byte[] sd,String host,int port){
		try{
			log.debug("<<<开始发送数据到加密机>>>");
			Socket s = new Socket(host, port);
			s.setSoTimeout(timeout);
			InputStream inStream = s.getInputStream();
			OutputStream outStream = s.getOutputStream();
			outStream.write(sd);
			outStream.flush();
			log.debug("<<<开始接受加密机返回数据>>>");
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int n = inStream.read();
			bos.write(n);
			byte[] d = new byte[buffer];
			while (inStream.available() > 0) {
				n = inStream.read(d);
				bos.write(d, 0, n);	
			}
			bos.flush();
			outStream.close();
			inStream.close();
			s.close();
			log.debug("<<<成功接受加密机返回数据,其返回的数据长度为["+bos.size()+"]>>>");
			return bos.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			log.error("与加密机通讯失败！");
			throw new RuntimeException("与加密机通讯失败！");
		}
	}
}
