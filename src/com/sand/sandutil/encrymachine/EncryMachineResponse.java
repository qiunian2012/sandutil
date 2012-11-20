package com.sand.sandutil.encrymachine;

import java.io.Serializable;
/**
 * 
 * @author Nano
 * @description 加密机相应对象
 */
public class EncryMachineResponse implements Serializable {
	
	private static final long serialVersionUID = 6362391628721752864L;

	public EncryMachineResponse(){
		
	}
	
	public String resCode; //响应代码
   
	public String errorCode; //错误代码
	
	public int length; //数据长度
	
	public byte[] resCotext; //响应数据内容
	
	public String errorMessage; //错误信息提示

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
