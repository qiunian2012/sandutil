package com.sand.sandutil.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Arrays;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sand.sandutil.contants.I;
import com.sand.sandutil.security.MessageDig;
import com.sand.sandutil.security.SandAPICipher;
import com.sand.sandutil.tool.Base64Decoder;
import com.sand.sandutil.tool.Base64Encoder;
import com.sand.sandutil.tool.ByteUtil;


public class MessageHelper {
	
	private  static Log log = LogFactory.getLog(MessageHelper.class);
	
	public static final String MSG_SPLIT = new String(new byte[] { 0x0d });

	/**
	 * �ɰ���Žӿ�
	 * @param messageIp ��������ַ
	 * @param messagePort �������˿�
	 * @param message �����͵���Ϣ
	 * @param phone �ֻ���
	 * @return
	 * @throws BusinessException
	 */
	public static boolean sendMessage(String messageIp,String messagePort,String message, String phone){
		boolean r = false;
		Socket sk = null;
		InputStream in = null;
		OutputStream os = null;
		try{
			sk = new Socket(messageIp, Integer.parseInt(messagePort));
			sk.setTcpNoDelay(true);
			os = sk.getOutputStream();
			os.write((phone + MSG_SPLIT + message).getBytes(I.ENCODEING_GBK));
			os.flush();
			byte[] bs = new byte[1024];
			in = sk.getInputStream();
			int n = in.read(bs);
			String sendResult = new String(bs,0,n);
			if(sendResult != null && sendResult.equalsIgnoreCase("000")){
				r = true;
			}
		}catch(Exception e){
			log.error(e.getMessage());
			throw new RuntimeException("MessageHelper " + e.getMessage());
		}finally{
			try {
				if(in != null)in.close();
				if(os != null)os.close();
				if(sk != null)sk.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	
	/**
	 * �°���Žӿ�
	 * @param messageIp ��������ַ
	 * @param messagePort �������˿�
	 * @param String[5] preInfos<br>
	 *        		String[0]:  version	    string	�汾��	01�����ȣ�2��<br>
	 *        		String[1]:  appNumber	    String	Ӧ��ϵͳ����	�����ȣ�10������10λ�Ҳ��ո�          û����д�ա���<br>
	 *        		String[2]:  srcNumber      String	��������	�����ȣ�10������10λ�Ҳ��ո�		û����д�ա���<br>
	 *        		String[3]:  businessNumber String	ҵ�����	�����ȣ�10������10λ�Ҳ��ո�		û����д�ա���<br>
	 *        		String[4]:  accountNumber	String	��������	�����ȣ�10������10λ�Ҳ��ո�		û����д�ա���<br>
	 *        		String[5]:  sendUserName	String	�������û�	�����ȣ�10������10λ�Ҳ��ո�)		û����д�ա���<br>
	 *          
	 * @param phone �ֻ���        
	 * @param message Ҫ���͵Ķ���Ϣ �����ȣ�200�ֽڣ�����200�Ҳ��ո�
	 * @param key ��Կkey
	 * 
	 * @return String[2] ���յ��ķ���  
	 * 				String[0]: <br>
	 * 					0 ->	 �ɹ� <br>
	 * 					1 ->    ʧ��  <br>
	 * 					2 ->    ����α��(ǩ����֤��ͨ��)<br>
	 * 			String[1]:
	 * 					Ӧ��ϵͳ��ϢID
	 * @throws BusinessException
	 */
	public static String[] sendMessage(String messageIp,String messagePort,String[] preInfos,String phone,String message,byte[] key)  {
		String[] result = new String[]{"1","unknow result"};
		Socket socket = null;
		OutputStream out = null;
		InputStream input  = null;
		try{	
			byte[] packMessage  = MessageUtil.pack(preInfos,phone,message,key);
log.info("���Ͷ�����֯�ı��ģ�" + new String(packMessage));
			socket = new Socket(messageIp, Integer.parseInt(messagePort));
			socket.setTcpNoDelay(true);
			//��ʱʱ��50��
			socket.setSoTimeout(50000);
			
			out = socket.getOutputStream();
			input = socket.getInputStream();

			out.write(packMessage);
			out.flush();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] b = new byte[128];
			int i = input.read();
			bout.write(i);
			while(input.available() > 0){
			    int n = input.read(b);
			    bout.write(b, 0, n);
			}
			byte[] orgResult = bout.toByteArray();		
log.info("���յ��Ķ��ű��ģ�" + new String(orgResult));		
			if(orgResult.length >= 95){
				byte[] status = new byte[1];
				System.arraycopy(orgResult, 0, status, 0, 1);
				byte[] appId = new byte[50];
				System.arraycopy(orgResult, 1, appId, 0, 50);
				byte[] orgByte = new byte[51];
				byte[] signByte = new byte[44];
				System.arraycopy(orgResult, 0, orgByte, 0, 51);
				System.arraycopy(orgResult, 51, signByte, 0, 44);
				boolean r = MessageUtil.verify(orgByte, signByte, key);
				if(r){
					result[0] = new String(status);
					result[1] = new String(appId,I.ENCODEING_GBK).trim();
				}else{
					result[0] = "2";
					result[1] = "����α��";
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
			throw new RuntimeException("MessageHelper ����ʧ�ܣ�" + e.getMessage());
		} finally{
			try {
				if(out != null )out.close();
				if(input != null )input.close();
				if(socket != null )socket.close();
			} catch (IOException e) {
				throw new RuntimeException("MessageHelper ͨѶ�쳣�� " + e.getMessage());
			}
		}
		return result;
	}
	
	
	public static void main(String []args)throws Exception{		
		byte[] key = Base64Decoder.decode("PUPxgFuhUUkxZJ0CWA15Rv7sMWReGi9u");	
		
		String[] preInfos = new String[]{"01","jmk","","","2",""};
		try{
			String[] rs = MessageHelper.sendMessage("61.129.71.103", "35678",preInfos,"13585554664", "�ظ�", key);
			
			System.out.println("rs[0]"+rs[0]);
			System.out.println("rs[1]"+rs[1]);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static class MessageUtil{
		
		public static byte[] pack(String[] preInfos,String phone,String message,byte[] key){
			try {	
				byte[] orgByte = new byte[263];
				byte[] b0 = preInfos[0].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b0, 0, orgByte, 0, 2);
				
				byte[] b1 = preInfos[1].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b1, 0, orgByte, 2, b1.length);
				if(b1.length < 10){
					for(int i =0 ;i < 10-b1.length ; i++){
						orgByte[2+b1.length+i] = " ".getBytes()[0];
					}
				}else if(b1.length > 10){
					throw new RuntimeException("preInfos[1] ���ݳ���");
				}
				
				byte[] b2 = preInfos[2].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b2, 0, orgByte, 12, b2.length);
				if(b2.length < 10){
					for(int i =0 ;i < 10-b2.length ; i++){
						orgByte[12+b2.length+i] = " ".getBytes()[0];
					}
				}else if(b2.length > 10){
					throw new RuntimeException("preInfos[2] ���ݳ���");
				}
				
				byte[] b3 = preInfos[3].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b3, 0, orgByte, 22, b3.length);
				if(b3.length < 10){
					for(int i =0 ;i < 10-b3.length ; i++){
						orgByte[22+b3.length+i] = " ".getBytes()[0];
					}
				}else if(b3.length > 10){
					throw new RuntimeException("preInfos[3] ���ݳ���");
				}
				
				
				byte[] b4 = preInfos[4].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b4, 0, orgByte, 32, b4.length);
				if(b4.length < 10){
					for(int i =0 ;i < 10-b4.length ; i++){
						orgByte[32+b4.length+i] = " ".getBytes()[0];
					}
				}else if(b4.length > 10){
					throw new RuntimeException("preInfos[4] ���ݳ���");
				}
				
				byte[] b5 = preInfos[5].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b5, 0, orgByte, 42, b5.length);
				if(b5.length < 10){
					for(int i =0 ;i < 10-b5.length ; i++){
						orgByte[42+b5.length+i] = " ".getBytes()[0];
					}
				}else if(b5.length > 10){
					throw new RuntimeException("preInfos[5] ���ݳ���");
				}
				
				System.arraycopy(phone.getBytes(I.ENCODEING_GBK), 0, orgByte, 52, 11);
				
				byte[] b6 = message.getBytes(I.ENCODEING_GBK);
				System.arraycopy(b6, 0, orgByte, 63, b6.length);
				if(b6.length < 200){
					for(int i =0 ;i < 200-b6.length ; i++){
						orgByte[63+b6.length+i] = " ".getBytes()[0];
					}
				}else if(b6.length > 200){
					throw new RuntimeException("preInfos[6] ���ݳ���");
				}
				return ByteUtil.union(orgByte, sign(orgByte, key));	
			} catch (UnsupportedEncodingException e) {	
					e.printStackTrace();
			}
			return null;
		}
	
		public static boolean verify(byte[]orgByte, byte[] signByte,byte[] key){
			String md5Str = MessageDig.md5Hex(orgByte).toLowerCase();
			
		    byte[] sign = Base64Encoder.encode(SandAPICipher.en3des(key, md5Str.getBytes())).getBytes();
		    
		    return  Arrays.equals(sign, signByte);
		}
	
		public static  byte[] sign(byte[]orgByte,byte[] key){	
			String md5Str = MessageDig.md5Hex(orgByte).toLowerCase();
		
		    byte[] sign = Base64Encoder.encode(SandAPICipher.en3des(key, md5Str.getBytes())).getBytes();
		 
		    return sign;
		}
		
	}
	
}
