package com.sand.sandutil.encrymachine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 
 * @author Administrator
 * @description 常量说明
 */
public class EncryMachineConst {
	
	
	public static String ENCRYMACHINE_HOST_NAME = "EncryMachine.HOST";
	public static String ENCRYMACHINE_PORT_NAME = "EncryMachine.PORT";
	public static String ENCRYMACHINE_KEY_INDEX_NAME = "EncryMachine.KEY_INDEX";

	//指令集
	public static String ENCRYMACHINE_SUCCESSFUL_CODE_00 = "00";//成功
	public static String ENCRYMACHINE_REQUEST_COMMAND_33 = "33";//私钥解密
	public static String ENCRYMACHINE_RESPONSE_COMMAND_34 = "34";//私钥解密响应代码
	public static String ENCRYMACHINE_REQUEST_COMMAND_37 = "37";//私钥签名
	public static String ENCRYMACHINE_RESPONSE_COMMAND_38 = "38";//私钥签名相应代码
	
	public static String ENCRYMACHINE_MESSAGE_DIG_MD5 = "MD5";
	public static String ENCRYMACHINE_MESSAGE_DIG_SHA1 = "SHA1";
	
	
	
	
	//填充方式
	public static String ENCRYMACHINE_PADDING_0 = "0";//a)  如果数据正好是密钥长度的整倍数，不做填补
	                                                  //b)	如果数据不是密钥长度的整倍数，后面补0x00。
	                                                  //c)	进行分段签名/加密。
	                                                  //d)	返回签名/加密结果：处理前明文的字节数（4字节）＋处理结果
                                                      //注意：此方式，由用户保证每段数据不大于密钥，否则验证/解密会失败。
	                                                  //处理前明文的字节数（4字节）的表示法：低字节在前，高字节在后。例如：78 56 34 12表示长度为0x12345678
                                                      //这种填充方式用于支持以下情况：
	                                                  //用户要采用PKCS以外的某种填充方式，可在外部先自行填充，然后通过密码机进行处理。
    public static String ENCRYMACHINE_PADDING_1 = "1";//PKCS
                                                      //PKCS填充方式，在用公钥或私钥进行处理前，数据按如下格式进行填充：
                                                      //00 BT PS 00 D
                                                      //BT：1字节，用私钥时填0x01，用公钥时填0x02，。
                                                      //PS：填充串，至少8字节。BT＝01时填FF……FF；BT＝02时填非0的随机数。
                                                      //D：	是数据，最多为密钥长度－11字节。
                                                      //＊填充后的数据长度等于密钥长度。
	//错误代码
    public static String ENCRYMACHINE_ERROR_CODE_00	= "命令响应成功";
	public static String ENCRYMACHINE_ERROR_CODE_42	= "命令长度错";
	public static String ENCRYMACHINE_ERROR_CODE_43	= "命令参数错误 ";
	public static String ENCRYMACHINE_ERROR_CODE_44	= "消息长度错";
	public static String ENCRYMACHINE_ERROR_CODE_45	= "离散次数错误";
	public static String ENCRYMACHINE_ERROR_CODE_46	= "数据长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_47	= "没有获得授权";
	public static String ENCRYMACHINE_ERROR_CODE_48	= "密码错误";
	public static String ENCRYMACHINE_ERROR_CODE_49	= "没有a卡";
	public static String ENCRYMACHINE_ERROR_CODE_64	= "头长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_65	= "尾长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_66	= "mac长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_10	= "签名验证错误";
	public static String ENCRYMACHINE_ERROR_CODE_81	= "RSA密钥长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_82	= "私钥索引错误";
	public static String ENCRYMACHINE_ERROR_CODE_83	= "写RSA密钥错误";
	public static String ENCRYMACHINE_ERROR_CODE_84	= "读RSA密钥错误";
	public static String ENCRYMACHINE_ERROR_CODE_85	= "RSA密钥不存在";
	public static String ENCRYMACHINE_ERROR_CODE_86	= "公钥解码错误";
	public static String ENCRYMACHINE_ERROR_CODE_87	= "签名长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_88	= "公钥长度错误";
	public static String ENCRYMACHINE_ERROR_CODE_89	= "签名时发生错误";
	public static String ENCRYMACHINE_ERROR_CODE_95	= "PKCS解密错误";
	public static String ENCRYMACHINE_ERROR_CODE_96	= "填充方式错误";
	public static String ENCRYMACHINE_ERROR_CODE_97	= "PKCS加密错误";  
	
	//加密机通讯设备的常规配置
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
