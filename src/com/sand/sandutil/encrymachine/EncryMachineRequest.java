package com.sand.sandutil.encrymachine;

import java.io.Serializable;
/**
 * 
 * @author Nano
 * @description ���ܻ��������
 *
 */
public class EncryMachineRequest implements Serializable {

	private static final long serialVersionUID = 4792838722889740845L;
	
	//ָ�����ܻ�ģʽ
	private  boolean model = false;
	
	/**
	 * @param model ָ��ʹ��ģʽ true Ϊ����ģʽ
	 * 
	 */
	public EncryMachineRequest(boolean model){
		this.model = model;
	}
	
	/**
	 * @param model ָ��ʹ��ģʽ true Ϊ����ģʽ
	 * @param defaultKeyIndex �Ƿ�ʹ��ɼ��Ĭ��˽Կ
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
	
	public String encryHost;//���ܻ�����
	
	public int encryPort;//���ܻ��˿�
	
	public String command;//ָ�����
	
	public String padding;//��䷽ʽ
	
	public String encryIndex;//��Կ����
	
	public int length;//���ݳ���
	
	public byte[] reqContext;//������������
	

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
