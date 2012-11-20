package com.sand.sandutil.encrymachine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sand.sandutil.security.MessageDig;
import com.sand.sandutil.tool.ByteUtil;

/**
 * @author Nano
 * @description 加密机操作的相关API
 */
public class EncryMachineApi {
	
	private  static Log log = LogFactory.getLog(EncryMachineApi.class);
	
    /**
     *  加密机私钥解密或签名等过程处理
     * @param request 加密机请求对象
     * @return EncryMachineResponse 加密机相应对象
     * @throws Exception
    */
	public static EncryMachineResponse secProcessByMachine(EncryMachineRequest request) throws Exception{
		EncryMachineResponse response = null;
		String errorMessage = "";
		try{
			StringBuffer sb = new StringBuffer();
			byte[] sd = null;
			byte[] rd = null;//接收数据
			if(request != null){
				log.info("<<<加密机请求命令[" + request.getCommand() + "] 填充方式["+request.getPadding()+"] 密钥索引["+request.getEncryIndex()+"] 请求数据hex [" +ByteUtil.bytes2hex(request.getReqContext())+ "]>>>");
				String lenS = "" + request.getReqContext().length;
				for (int i = 0; lenS.length() < 4; i++) {
					lenS = "0" + lenS;
				}
				sb.setLength(0);
				sb.append(request.getCommand())
				  .append(request.getPadding())
				  .append(request.getEncryIndex())
				  .append(lenS);
				
				sd = ByteUtil.union(sb.toString().getBytes(), request.getReqContext());
				String sd_length = ByteUtil.intNum2HexStr(sd.length);
				for (int i = 0; sd_length.length() < 4; i++) {
					sd_length = "0" + sd_length;
				}
				sd = ByteUtil.union(ByteUtil.ascii2hex(sd_length.getBytes()), sd);//添加长度十六进制
				
				rd = EncryMachineSocket.socketEncryMachine(sd, request.getEncryHost(), request.getEncryPort());				
				log.info("加密机收到的返回结果=" + new String(ByteUtil.bytes2hex(rd)));
				
				if(rd.length > 0){
					
					response = new EncryMachineResponse();
					String pakLength = ByteUtil.hexNum2IntegerStr(new String(ByteUtil.hex2ascii(ByteUtil.sub(rd, 0, 2))));//长度十六进制 占用了两个字节
					if(Integer.parseInt(pakLength) > 3){//加密机返回长度大于3
						String resCode = new String(ByteUtil.sub(rd, 2, 2));//位移两个字节
						String errorCode = new String(ByteUtil.sub(rd, 4, 2));
						if(EncryMachineConst.ENCRYMACHINE_SUCCESSFUL_CODE_00.equalsIgnoreCase(errorCode)){//处理结果返回成功
							int  lenth  = Integer.parseInt(new String(ByteUtil.sub(rd, 6, 4)));
							byte[] data = ByteUtil.sub(rd, 10, lenth);
							response.setResCotext(data);						
							response.setLength(lenth);
							errorCode = EncryMachineConst.ENCRYMACHINE_SUCCESSFUL_CODE_00;
						}else{
							if(EncryMachineConst.ENCRYMACHINE_RESPONSE_COMMAND_34.equalsIgnoreCase(resCode)){
								log.error("加密机私钥解密返回错误代号["+errorCode+"],请参考错误码表");
								errorMessage = "加密机私钥解密失败";
							}else if(EncryMachineConst.ENCRYMACHINE_RESPONSE_COMMAND_38.equalsIgnoreCase(resCode)){
								log.error("加密机私钥签名返回错误代号["+errorCode+"],请参考错误码表");
								errorMessage = "加密机私钥签名失败";
							}else{
								log.error("加密机处理后返回错误代号["+errorCode+"],请参考错误码表");
								errorMessage = "加密机处理过程失败";
							}	
						}
						response.setResCode(resCode);
						response.setErrorCode(errorCode);
						response.setErrorMessage(errorMessage);
					}
				}	
			}	
		}catch(Exception e){
			e.printStackTrace();
			log.error("加密机处理过程中发生错误,原因如下："+ e.getMessage());
		}
		return response;
	}
	
	public static void main(String []args){
		
		MessageDig mdig = new MessageDig();
		
		byte[] plaintext = mdig.mDigest("DJSFJSALJFLAJFUROEWUQRU123", "MD5", "UTF-8");
	
		EncryMachineRequest reqeust = new EncryMachineRequest(false,true);
		reqeust.setCommand(EncryMachineConst.ENCRYMACHINE_REQUEST_COMMAND_37);
		reqeust.setPadding(EncryMachineConst.ENCRYMACHINE_PADDING_1);
		reqeust.setReqContext(plaintext);
		
		try {
			EncryMachineResponse response =  EncryMachineApi.secProcessByMachine(reqeust);
			System.out.println("签名后的数据为:=" + ByteUtil.bytes2hex(response.getResCotext()));
			
			//System.out.println("解密后的数据为:=" + new String(response.getResCotext()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
