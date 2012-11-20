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
	 * 旧版短信接口
	 * @param messageIp 服务器地址
	 * @param messagePort 服务器端口
	 * @param message 待发送的消息
	 * @param phone 手机号
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
	 * 新版短信接口
	 * @param messageIp 服务器地址
	 * @param messagePort 服务器端口
	 * @param String[5] preInfos<br>
	 *        		String[0]:  version	    string	版本号	01（长度：2）<br>
	 *        		String[1]:  appNumber	    String	应用系统编码	（长度：10，不足10位右补空格）          没有填写空“”<br>
	 *        		String[2]:  srcNumber      String	渠道编码	（长度：10，不足10位右补空格）		没有填写空“”<br>
	 *        		String[3]:  businessNumber String	业务编码	（长度：10，不足10位右补空格）		没有填写空“”<br>
	 *        		String[4]:  accountNumber	String	机构编码	（长度：10，不足10位右补空格）		没有填写空“”<br>
	 *        		String[5]:  sendUserName	String	发送者用户	（长度：10，不足10位右补空格)		没有填写空“”<br>
	 *          
	 * @param phone 手机号        
	 * @param message 要发送的短消息 （长度：200字节，不足200右补空格）
	 * @param key 密钥key
	 * 
	 * @return String[2] 接收到的反馈  
	 * 				String[0]: <br>
	 * 					0 ->	 成功 <br>
	 * 					1 ->    失败  <br>
	 * 					2 ->    数据伪造(签名验证不通过)<br>
	 * 			String[1]:
	 * 					应用系统消息ID
	 * @throws BusinessException
	 */
	public static String[] sendMessage(String messageIp,String messagePort,String[] preInfos,String phone,String message,byte[] key)  {
		String[] result = new String[]{"1","unknow result"};
		Socket socket = null;
		OutputStream out = null;
		InputStream input  = null;
		try{	
			byte[] packMessage  = MessageUtil.pack(preInfos,phone,message,key);
log.info("发送短信组织的报文：" + new String(packMessage));
			socket = new Socket(messageIp, Integer.parseInt(messagePort));
			socket.setTcpNoDelay(true);
			//超时时间50秒
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
log.info("接收到的短信报文：" + new String(orgResult));		
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
					result[1] = "数据伪造";
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
			throw new RuntimeException("MessageHelper 操作失败：" + e.getMessage());
		} finally{
			try {
				if(out != null )out.close();
				if(input != null )input.close();
				if(socket != null )socket.close();
			} catch (IOException e) {
				throw new RuntimeException("MessageHelper 通讯异常： " + e.getMessage());
			}
		}
		return result;
	}
	
	
	public static void main(String []args)throws Exception{		
		byte[] key = Base64Decoder.decode("PUPxgFuhUUkxZJ0CWA15Rv7sMWReGi9u");	
		
		String[] preInfos = new String[]{"01","jmk","","","2",""};
		try{
			String[] rs = MessageHelper.sendMessage("61.129.71.103", "35678",preInfos,"13585554664", "回复", key);
			
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
					throw new RuntimeException("preInfos[1] 数据超长");
				}
				
				byte[] b2 = preInfos[2].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b2, 0, orgByte, 12, b2.length);
				if(b2.length < 10){
					for(int i =0 ;i < 10-b2.length ; i++){
						orgByte[12+b2.length+i] = " ".getBytes()[0];
					}
				}else if(b2.length > 10){
					throw new RuntimeException("preInfos[2] 数据超长");
				}
				
				byte[] b3 = preInfos[3].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b3, 0, orgByte, 22, b3.length);
				if(b3.length < 10){
					for(int i =0 ;i < 10-b3.length ; i++){
						orgByte[22+b3.length+i] = " ".getBytes()[0];
					}
				}else if(b3.length > 10){
					throw new RuntimeException("preInfos[3] 数据超长");
				}
				
				
				byte[] b4 = preInfos[4].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b4, 0, orgByte, 32, b4.length);
				if(b4.length < 10){
					for(int i =0 ;i < 10-b4.length ; i++){
						orgByte[32+b4.length+i] = " ".getBytes()[0];
					}
				}else if(b4.length > 10){
					throw new RuntimeException("preInfos[4] 数据超长");
				}
				
				byte[] b5 = preInfos[5].getBytes(I.ENCODEING_GBK);
				System.arraycopy(b5, 0, orgByte, 42, b5.length);
				if(b5.length < 10){
					for(int i =0 ;i < 10-b5.length ; i++){
						orgByte[42+b5.length+i] = " ".getBytes()[0];
					}
				}else if(b5.length > 10){
					throw new RuntimeException("preInfos[5] 数据超长");
				}
				
				System.arraycopy(phone.getBytes(I.ENCODEING_GBK), 0, orgByte, 52, 11);
				
				byte[] b6 = message.getBytes(I.ENCODEING_GBK);
				System.arraycopy(b6, 0, orgByte, 63, b6.length);
				if(b6.length < 200){
					for(int i =0 ;i < 200-b6.length ; i++){
						orgByte[63+b6.length+i] = " ".getBytes()[0];
					}
				}else if(b6.length > 200){
					throw new RuntimeException("preInfos[6] 数据超长");
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
