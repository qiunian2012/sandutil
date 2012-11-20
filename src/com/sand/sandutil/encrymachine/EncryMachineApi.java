package com.sand.sandutil.encrymachine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sand.sandutil.security.MessageDig;
import com.sand.sandutil.tool.ByteUtil;

/**
 * @author Nano
 * @description ���ܻ����������API
 */
public class EncryMachineApi {
	
	private  static Log log = LogFactory.getLog(EncryMachineApi.class);
	
    /**
     *  ���ܻ�˽Կ���ܻ�ǩ���ȹ��̴���
     * @param request ���ܻ��������
     * @return EncryMachineResponse ���ܻ���Ӧ����
     * @throws Exception
    */
	public static EncryMachineResponse secProcessByMachine(EncryMachineRequest request) throws Exception{
		EncryMachineResponse response = null;
		String errorMessage = "";
		try{
			StringBuffer sb = new StringBuffer();
			byte[] sd = null;
			byte[] rd = null;//��������
			if(request != null){
				log.info("<<<���ܻ���������[" + request.getCommand() + "] ��䷽ʽ["+request.getPadding()+"] ��Կ����["+request.getEncryIndex()+"] ��������hex [" +ByteUtil.bytes2hex(request.getReqContext())+ "]>>>");
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
				sd = ByteUtil.union(ByteUtil.ascii2hex(sd_length.getBytes()), sd);//��ӳ���ʮ������
				
				rd = EncryMachineSocket.socketEncryMachine(sd, request.getEncryHost(), request.getEncryPort());				
				log.info("���ܻ��յ��ķ��ؽ��=" + new String(ByteUtil.bytes2hex(rd)));
				
				if(rd.length > 0){
					
					response = new EncryMachineResponse();
					String pakLength = ByteUtil.hexNum2IntegerStr(new String(ByteUtil.hex2ascii(ByteUtil.sub(rd, 0, 2))));//����ʮ������ ռ���������ֽ�
					if(Integer.parseInt(pakLength) > 3){//���ܻ����س��ȴ���3
						String resCode = new String(ByteUtil.sub(rd, 2, 2));//λ�������ֽ�
						String errorCode = new String(ByteUtil.sub(rd, 4, 2));
						if(EncryMachineConst.ENCRYMACHINE_SUCCESSFUL_CODE_00.equalsIgnoreCase(errorCode)){//���������سɹ�
							int  lenth  = Integer.parseInt(new String(ByteUtil.sub(rd, 6, 4)));
							byte[] data = ByteUtil.sub(rd, 10, lenth);
							response.setResCotext(data);						
							response.setLength(lenth);
							errorCode = EncryMachineConst.ENCRYMACHINE_SUCCESSFUL_CODE_00;
						}else{
							if(EncryMachineConst.ENCRYMACHINE_RESPONSE_COMMAND_34.equalsIgnoreCase(resCode)){
								log.error("���ܻ�˽Կ���ܷ��ش������["+errorCode+"],��ο��������");
								errorMessage = "���ܻ�˽Կ����ʧ��";
							}else if(EncryMachineConst.ENCRYMACHINE_RESPONSE_COMMAND_38.equalsIgnoreCase(resCode)){
								log.error("���ܻ�˽Կǩ�����ش������["+errorCode+"],��ο��������");
								errorMessage = "���ܻ�˽Կǩ��ʧ��";
							}else{
								log.error("���ܻ�����󷵻ش������["+errorCode+"],��ο��������");
								errorMessage = "���ܻ��������ʧ��";
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
			log.error("���ܻ���������з�������,ԭ�����£�"+ e.getMessage());
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
			System.out.println("ǩ���������Ϊ:=" + ByteUtil.bytes2hex(response.getResCotext()));
			
			//System.out.println("���ܺ������Ϊ:=" + new String(response.getResCotext()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
