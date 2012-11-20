package com.sand.sandutil.encrymachine;

import java.io.Serializable;
/**
 * 
 * @author Nano
 * @description ���ܻ���Ӧ����
 */
public class EncryMachineResponse implements Serializable {
	
	private static final long serialVersionUID = 6362391628721752864L;

	public EncryMachineResponse(){
		
	}
	
	public String resCode; //��Ӧ����
   
	public String errorCode; //�������
	
	public int length; //���ݳ���
	
	public byte[] resCotext; //��Ӧ��������
	
	public String errorMessage; //������Ϣ��ʾ

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getResCotext() {
		return resCotext;
	}

	public void setResCotext(byte[] resCotext) {
		this.resCotext = resCotext;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
