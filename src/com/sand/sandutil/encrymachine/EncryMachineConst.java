package com.sand.sandutil.encrymachine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 
 * @author Administrator
 * @description ����˵��
 */
public class EncryMachineConst {
	
	
	public static String ENCRYMACHINE_HOST_NAME = "EncryMachine.HOST";
	public static String ENCRYMACHINE_PORT_NAME = "EncryMachine.PORT";
	public static String ENCRYMACHINE_KEY_INDEX_NAME = "EncryMachine.KEY_INDEX";

	//ָ�
	public static String ENCRYMACHINE_SUCCESSFUL_CODE_00 = "00";//�ɹ�
	public static String ENCRYMACHINE_REQUEST_COMMAND_33 = "33";//˽Կ����
	public static String ENCRYMACHINE_RESPONSE_COMMAND_34 = "34";//˽Կ������Ӧ����
	public static String ENCRYMACHINE_REQUEST_COMMAND_37 = "37";//˽Կǩ��
	public static String ENCRYMACHINE_RESPONSE_COMMAND_38 = "38";//˽Կǩ����Ӧ����
	
	public static String ENCRYMACHINE_MESSAGE_DIG_MD5 = "MD5";
	public static String ENCRYMACHINE_MESSAGE_DIG_SHA1 = "SHA1";
	
	
	
	
	//��䷽ʽ
	public static String ENCRYMACHINE_PADDING_0 = "0";//a)  ���������������Կ���ȵ��������������
	                                                  //b)	������ݲ�����Կ���ȵ������������油0x00��
	                                                  //c)	���зֶ�ǩ��/���ܡ�
	                                                  //d)	����ǩ��/���ܽ��������ǰ���ĵ��ֽ�����4�ֽڣ���������
                                                      //ע�⣺�˷�ʽ�����û���֤ÿ�����ݲ�������Կ��������֤/���ܻ�ʧ�ܡ�
	                                                  //����ǰ���ĵ��ֽ�����4�ֽڣ��ı�ʾ�������ֽ���ǰ�����ֽ��ں����磺78 56 34 12��ʾ����Ϊ0x12345678
                                                      //������䷽ʽ����֧�����������
	                                                  //�û�Ҫ����PKCS�����ĳ����䷽ʽ�������ⲿ��������䣬Ȼ��ͨ����������д���
    public static String ENCRYMACHINE_PADDING_1 = "1";//PKCS
                                                      //PKCS��䷽ʽ�����ù�Կ��˽Կ���д���ǰ�����ݰ����¸�ʽ������䣺
                                                      //00 BT PS 00 D
                                                      //BT��1�ֽڣ���˽Կʱ��0x01���ù�Կʱ��0x02����
                                                      //PS����䴮������8�ֽڡ�BT��01ʱ��FF����FF��BT��02ʱ���0���������
                                                      //D��	�����ݣ����Ϊ��Կ���ȣ�11�ֽڡ�
                                                      //����������ݳ��ȵ�����Կ���ȡ�
	//�������
    public static String ENCRYMACHINE_ERROR_CODE_00	= "������Ӧ�ɹ�";
	public static String ENCRYMACHINE_ERROR_CODE_42	= "����ȴ�";
	public static String ENCRYMACHINE_ERROR_CODE_43	= "����������� ";
	public static String ENCRYMACHINE_ERROR_CODE_44	= "��Ϣ���ȴ�";
	public static String ENCRYMACHINE_ERROR_CODE_45	= "��ɢ��������";
	public static String ENCRYMACHINE_ERROR_CODE_46	= "���ݳ��ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_47	= "û�л����Ȩ";
	public static String ENCRYMACHINE_ERROR_CODE_48	= "�������";
	public static String ENCRYMACHINE_ERROR_CODE_49	= "û��a��";
	public static String ENCRYMACHINE_ERROR_CODE_64	= "ͷ���ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_65	= "β���ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_66	= "mac���ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_10	= "ǩ����֤����";
	public static String ENCRYMACHINE_ERROR_CODE_81	= "RSA��Կ���ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_82	= "˽Կ��������";
	public static String ENCRYMACHINE_ERROR_CODE_83	= "дRSA��Կ����";
	public static String ENCRYMACHINE_ERROR_CODE_84	= "��RSA��Կ����";
	public static String ENCRYMACHINE_ERROR_CODE_85	= "RSA��Կ������";
	public static String ENCRYMACHINE_ERROR_CODE_86	= "��Կ�������";
	public static String ENCRYMACHINE_ERROR_CODE_87	= "ǩ�����ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_88	= "��Կ���ȴ���";
	public static String ENCRYMACHINE_ERROR_CODE_89	= "ǩ��ʱ��������";
	public static String ENCRYMACHINE_ERROR_CODE_95	= "PKCS���ܴ���";
	public static String ENCRYMACHINE_ERROR_CODE_96	= "��䷽ʽ����";
	public static String ENCRYMACHINE_ERROR_CODE_97	= "PKCS���ܴ���";  
	
	//���ܻ�ͨѶ�豸�ĳ�������
	public static String ENCRYMACHINE_HOST_PRODUCT = "";
	
	public static int ENCRYMACHINE_PORT_PRODUCT = 0;
	
	public static String ENCRYMACHINE_KEY_INDEX_PRODUCT = "";
	
	public static String ENCRYMACHINE_HOST_TEST = "";
	
	public static int ENCRYMACHINE_PORT_TEST = 0;
	
	public static String ENCRYMACHINE_KEY_INDEX_TEST = "";
	
	static{
		InputStream in = EncryMachineRequest.class.getResourceAsStream("EncryMachine.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			ENCRYMACHINE_HOST_PRODUCT = pro.getProperty("PRODUCT_"+EncryMachineConst.ENCRYMACHINE_HOST_NAME,"127.0.0.0");
			ENCRYMACHINE_PORT_PRODUCT = (Integer.parseInt(pro.getProperty("PRODUCT_"+EncryMachineConst.ENCRYMACHINE_PORT_NAME,"2020")));
			ENCRYMACHINE_KEY_INDEX_PRODUCT = pro.getProperty("PRODUCT_"+EncryMachineConst.ENCRYMACHINE_KEY_INDEX_NAME,"01");
			
			ENCRYMACHINE_HOST_TEST = pro.getProperty("TEST_"+EncryMachineConst.ENCRYMACHINE_HOST_NAME,"127.0.0.0");
			ENCRYMACHINE_PORT_TEST = (Integer.parseInt(pro.getProperty("TEST_"+EncryMachineConst.ENCRYMACHINE_PORT_NAME,"2020")));
			ENCRYMACHINE_KEY_INDEX_TEST = pro.getProperty("TEST_"+EncryMachineConst.ENCRYMACHINE_KEY_INDEX_NAME,"01");
		} catch (IOException e) {
			e.printStackTrace();
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	 	
}
