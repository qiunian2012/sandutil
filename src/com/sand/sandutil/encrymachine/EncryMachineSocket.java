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
			log.debug("<<<��ʼ�������ݵ����ܻ�>>>");
			Socket s = new Socket(host, port);
			s.setSoTimeout(timeout);
			InputStream inStream = s.getInputStream();
			OutputStream outStream = s.getOutputStream();
			outStream.write(sd);
			outStream.flush();
			log.debug("<<<��ʼ���ܼ��ܻ���������>>>");
			
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
			log.debug("<<<�ɹ����ܼ��ܻ���������,�䷵�ص����ݳ���Ϊ["+bos.size()+"]>>>");
			return bos.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			log.error("����ܻ�ͨѶʧ�ܣ�");
			throw new RuntimeException("����ܻ�ͨѶʧ�ܣ�");
		}
	}
}
