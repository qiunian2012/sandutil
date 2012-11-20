package com.sand.sandutil.encrymachine;

import java.io.Serializable;
/**
 * 
 * @author Nano
 * @description 加密机请求对象
 *
 */
public class EncryMachineRequest implements Serializable {

	private static final long serialVersionUID = 4792838722889740845L;
	
	//指定加密机模式
	private  boolean model = false;
	
	/**
	 * @param model 指定使用模式 true 为生产模式
	 * 
	 */
	public EncryMachineRequest(boolean model){
		this.model = model;
	}
	
	/**
	 * @param model 指定使用模式 true 为生产模式
	 * @param defaultKeyIndex 是否使用杉德默认私钥
	 */
	public EncryMachineRequest(boolean model,boolean defaultKeyIndex){
		if(model){
			initProductEncryMachine(defaultKeyIndex);
		}else{
			initTestEncryMachine(defaultKeyIndex);
		}
	}
	
	public void initProductEncryMachine(boolean defaultKeyIndex){
        setEncryHost(EncryMachineConst.ENCRYMACHINE_HOST_PRODUCT);
	    setEncryPort(EncryMachineConst.ENCRYMACHINE_PORT_PRODUCT);
		setEncryIndex(defaultKeyIndex ?EncryMachineConst.ENCRYMACHINE_KEY_INDEX_PRODUCT:null);
	}
	
	public void initTestEncryMachine(boolean defaultKeyIndex){
        setEncryHost(EncryMachineConst.ENCRYMACHINE_HOST_TEST);
	    setEncryPort(EncryMachineConst.ENCRYMACHINE_PORT_TEST);
		setEncryIndex(defaultKeyIndex ?EncryMachineConst.ENCRYMACHINE_KEY_INDEX_TEST:null);
	}
	
	public String encryHost;//加密机主机
	
	public int encryPort;//加密机端口
	
	public String command;//指令代码
	
	public String padding;//填充方式
	
	public String encryIndex;//密钥索引
	
	public int length;//数据长度
	
	public byte[] reqContext;//请求数据内容
	

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getPadding() {
		return padding;
	}

	public void setPadding(String padding) {
		this.padding = padding;
	}

	public String getEncryIndex() {
		return encryIndex;
	}

	public void setEncryIndex(String encryIndex) {
		this.encryIndex = encryIndex;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] getReqContext() {
		return reqContext;
	}

	public void setReqContext(byte[] reqContext) {
		this.reqContext = reqContext;
	}

	public String getEncryHost() {
		return encryHost;
	}

	public void setEncryHost(String encryHost) {
		this.encryHost = encryHost;
	}

	public int getEncryPort() {
		return encryPort;
	}

	public void setEncryPort(int encryPort) {
		this.encryPort = encryPort;
	}
}
