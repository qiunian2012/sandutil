package com.sand.sandutil.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sand.sandutil.contants.I;


public class StringUtil {
	
	public  static String getUUIDValue(){
		return  UUID.randomUUID().toString()+System.currentTimeMillis();
	}
	
    /**
     * �Դ�ת��Ϊbcd��ascii�����ַ�������֤ģ��
     */
    public static Pattern patternBcd = Pattern.compile("^[\\d|:|;|<|=|>|\\?]*$");
    
    /**
     * �Դ�ת��Ϊhex��ascii�����ַ�������֤ģ��
     */
    public static Pattern patternHex = Pattern.compile("^[\\d|a-f|A-F]*$");

    /**
     * �����ַ�
     */
    public static String LINE = System.getProperty("line.separator");
    
	private static final String[]NUMBER_ARRAY = {"��","Ҽ","��","��","��","��","½","��","��","��"};
	
	private static final String[]CURR_ARRAY = {"��","��","Ԫ","ʰ","��","Ǫ","��","ʰ","��","Ǫ","��","ʰ","��","Ǫ","��","��","ʰ","��","Ǫ","��"};
	
	private static final String UNITS="Ԫ������";

    /**
     * ���ҡ���������ַ�
     * 
     * @param str
     *            �������ַ���������Ϊnull
     * @param fillChar
     *            �����ַ�
     * @param fillSide ['left','right','both']
     *            ���ķ���
     * @param size
     *            ����ַ����Ĺ̶�byte���ȡ�
     * @return String
     */
    public static String charFill(String str, char fillChar, String  fillSide, int size) {
        str = (str == null) ? "" : str;
        StringBuffer sb = new StringBuffer(str);
        int len = str.length();
        if (len >= size) 
            return (("left".equals(fillSide)) ? str.substring(len - size) : str.substring(0, size));
        int n = size - len;
        if ("left".equals(fillSide))
        	for (int i = 0; i < n; ++i)
        		sb.insert(0, fillChar);
        else if ("right".equals(fillSide))
        	for (int i = 0; i < n; ++i)
        		sb.append(fillChar);
        else if ("both".equals(fillSide)) {
        	for (int i = 0; i < n; ++i) {
        		if (i % 2 == 0)
        			sb.insert(0, fillChar);
        		else
        			sb.append(fillChar);
            }
        }
        return sb.toString();
    }

    /**
     * ȥ��(���ҡ�����)�����ַ�
     * 
     * @param str      ��ȥ���ַ����ַ���
     * @param trimChar ��ȥ�����ַ�
     * @param trimSide ���� ['left','right','both']
     * @return String
     */
    public static String trimChar(String str, char trimChar, String fillSide) {
        if (str == null) return null;
        if (("left".equals(fillSide)) || ("both".equals(fillSide)) )
        	str = str.replaceAll("^" + trimChar + "+", "");
        if (("rigth".equals(fillSide)) || ("both".equals(fillSide)) )
        	str = str.replaceAll(trimChar + "+$", "");
        return str;
    }


    /**
     * ASCII -> BCD  <p>
     * �÷������ascii���ƵĲ���str���м�⣬�Ա�֤����ȷת��ΪBCD
     * @param str
     * @return String
     */
    public static String ascii2bcd(String str) {
        try {
            Matcher m = patternBcd.matcher(str);
            if (!m.matches())
                throw new RuntimeException("���ܽ�ascii�ַ���\"" + str + "\"ת��Ϊbcd��ʽ��");
            return new String(ByteUtil.ascii2bcd(str.getBytes(I.ENCODEING_DEFAULT)), I.ENCODEING_DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * BCDת��ΪAscii��
     * @param bcdData
     *            ��ת����bcd�ַ���
     * @return תΪΪascii����ַ���
     */
    public static String bcd2ascii(String bcdData) {
        try {
            return new String(ByteUtil.bcd2ascii(bcdData.getBytes(I.ENCODEING_DEFAULT)), I.ENCODEING_DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Hexת��ΪAscii��
     * <p>
     * 
     * <strong>ע��: </strong> �÷�����windows�£���byteת��ΪStringʱ���п��ܴ������ݶ�ʧ����ʱӦ��
     * ʹ��bcd2ascii(byte[]bs)������
     * 
     * @param str
     *            ��ת�����ַ���
     * @return תΪΪascii����ַ���
     */
    public static String hex2ascii(String str) {
        try {
            return new String(ByteUtil.hex2ascii(str.getBytes(I.ENCODEING_DEFAULT)), I.ENCODEING_DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
 
    /**
     * Asciiת��Ϊ16����<br>
     * �÷�����Բ���srt���м�飬�Ƿ�����Ƿ��ַ���
     * @param str
     * @return String
     */
    public static String ascii2hex(String str) {
        Matcher m = patternHex.matcher(str);
        if (!m.matches())
            throw new RuntimeException("���ܽ�ascii�ַ��� \"" + str + "\" ת��ΪHex��ʽ��");
        try {
            return new String(ByteUtil.ascii2hex(str.getBytes(I.ENCODEING_DEFAULT)), I.ENCODEING_DEFAULT);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * ����Ƿ�ȫ����hex�Ϸ��ַ�<br>
     * ��Ҫ��jdk1.3��֧��������ʽ
     */
    @SuppressWarnings("unused")
	private static void checkHexChar(String str){
        if(str!=null){
            byte[]bs=str.toUpperCase().getBytes();
            for(int i=0;i<bs.length;i++){
                if(!(bs[i]>0x29&&bs[i]<0x3a)&&!(bs[i]>0x40&&bs[i]<0x5b)){
                    throw new RuntimeException("\""+str+"\"�а����Ƿ���Hex�ַ���������0-9,a-z,A-Z��");
                }
            }
        }
        else
            throw new RuntimeException("\""+str+"\"�а����Ƿ���Hex�ַ���������0-9,a-z,A-Z��");
    }

    /**
     * ����Ƿ�ȫ����bcd�Ϸ��ַ�<br>
     * ��Ҫ��jdk1.3��֧��������ʽ
     */
    @SuppressWarnings("unused")
	private static void checkBcdChar(String str){
        if(str!=null){
            byte[]bs=str.getBytes();
            for(int i=0;i<bs.length;i++){
                if(!(bs[i]>0x29&&bs[i]<0x40)){
                    throw new RuntimeException("\""+str+"\"�а����Ƿ���Bcd�ַ���������0-9:;<=>?");
                }
            }
        }
        else
            throw new RuntimeException("\""+str+"\"�а����Ƿ���Bcd�ַ���������0-9:;<=>?");
    }
    
    /**
     * ��ȡ�ַ����ĳ���
     * @param str
     * @return int
     */
    public static int getByteLength(String str){
    	try{
    		return str.getBytes(I.ENCODEING_DEFAULT).length;
    	}catch(Exception e){
    		return -1;
    	}
    }
    
    /**
     * ֻ��ʾ0x20 - 0x80 ֮����ַ�
     */
    public static String getByteText(byte[] bs){
        byte[] bs2=new byte[bs.length];
        for(int i=0;i<bs.length;i++)
            bs2[i]=(bs[i]<0x20 || bs[i]>0x80)?0x20:bs[i];
        return new String(bs2);
    }
    
	/**
	 * ����дת��
	 * @param moneyStr
	 * @return
	 */
	public static String transMoney(String moneyStr) {
		BigDecimal bdNum = new BigDecimal(moneyStr);
		moneyStr = bdNum.multiply(new BigDecimal(100)).toString();
		int donet=moneyStr.indexOf(".");
		moneyStr=donet!=-1?moneyStr.substring(0,donet):moneyStr;
		int n = moneyStr.length();
		if(n>CURR_ARRAY.length)throw new RuntimeException("���ݵ��������ֳ��Ȳ��ܳ���"+(CURR_ARRAY.length-2)+"λ��");
		StringBuffer sb=new StringBuffer();
		boolean needUnit = false,preIsZero = false;
		for(int i=0;i<n;i++) { //ֻҪ��һ��Ԫ�ز�Ϊ0,����Ҫ��λ�� ���ǰ�����㣬���ظ���ӡ�
			int a123=Integer.parseInt(moneyStr.substring(i,i+1));
			String numStr = NUMBER_ARRAY[a123];
			String currStr =CURR_ARRAY[n-i-1];
			boolean isUnit=UNITS.indexOf(currStr)!=-1;
			needUnit=needUnit||a123!=0;
			sb.append(a123!=0?((preIsZero&&(i<n-2)?"��":"")+numStr+currStr):((needUnit&&isUnit)||(i==n-3)?currStr:""));
			needUnit=isUnit?false:needUnit;
			preIsZero=(a123!=0||(isUnit&&needUnit))?false:true;
		}
		return sb.append("��").toString();
	}
	
	
	

	/**
	 * ��doubleת��Ϊ�ֵ������ַ���
	 * 
	 * @param value
	 * @return
	 */
	public static String toFen(double value){
		return format(value).replaceAll(",","").replaceAll("\\.","");
	}
	
	
	/**
	 * ���ָ�ʽ��Ϊ��ȷ��Ԫ�Ƿָ�ʽ
	 * @param str
	 * @return
	 */
	public static String format(double value){
		return NumberFormat.getCurrencyInstance(Locale.CHINA).format(value).replaceAll("��","");
	}
	
    /**
     * equals function that actually compares two buffers.
     *
     * @param onearray First buffer
     * @param twoarray Second buffer
     * @return true if one and two contain exactly the same content, else false.
     */
    public static boolean bufEquals(byte onearray[], byte twoarray[] ) {
    	if (onearray == twoarray) return true;
        boolean ret = (onearray.length == twoarray.length);
        if (!ret) {
            return ret;
        }
        for (int idx = 0; idx < onearray.length; idx++) {
            if (onearray[idx] != twoarray[idx]) {
                return false;
            }
        }
        return true;
    }
    
    private static final char[] hexchars = { '0', '1', '2', '3', '4', '5',
                                            '6', '7', '8', '9', 'A', 'B',
                                            'C', 'D', 'E', 'F' };
    /**
     * 
     * @param s 
     * @return 
     */
    static String toXMLString(String s) {
        if (s == null)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < s.length(); idx++) {
          char ch = s.charAt(idx);
          if (ch == '<') {
            sb.append("&lt;");
          } else if (ch == '&') {
            sb.append("&amp;");
          } else if (ch == '%') {
            sb.append("%25");
          } else if (ch < 0x20) {
            sb.append("%");
            sb.append(hexchars[ch/16]);
            sb.append(hexchars[ch%16]);
          } else {
            sb.append(ch);
          }
        }
        return sb.toString();
    }
    
    static private int h2c(char ch) {
      if (ch >= '0' && ch <= '9') {
        return ch - '0';
      } else if (ch >= 'A' && ch <= 'F') {
        return ch - 'A';
      } else if (ch >= 'a' && ch <= 'f') {
        return ch - 'a';
      }
      return 0;
    }
    
    /**
     * 
     * @param s 
     * @return 
     */
    static String fromXMLString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int idx = 0; idx < s.length();) {
          char ch = s.charAt(idx++);
          if (ch == '%') {
            char ch1 = s.charAt(idx++);
            char ch2 = s.charAt(idx++);
            char res = (char)(h2c(ch1)*16 + h2c(ch2));
            sb.append(res);
          } else {
            sb.append(ch);
          }
        }
        
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @return 
     */
   public  static String toCSVString(String s) {
        if (s == null)
            return "";

        StringBuilder sb = new StringBuilder(s.length()+1);
        sb.append('\'');
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            switch(c) {
                case '\0':
                    sb.append("%00");
                    break;
                case '\n':
                    sb.append("%0A");
                    break;
                case '\r':
                    sb.append("%0D");
                    break;
                case ',':
                    sb.append("%2C");
                    break;
                case '}':
                    sb.append("%7D");
                    break;
                case '%':
                    sb.append("%25");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @throws java.io.IOException 
     * @return 
     */
    static String fromCSVString(String s) throws IOException {
        if (s.charAt(0) != '\'') {
            throw new IOException("Error deserializing string.");
        }
        int len = s.length();
        StringBuilder sb = new StringBuilder(len-1);
        for (int i = 1; i < len; i++) {
            char c = s.charAt(i);
            if (c == '%') {
                char ch1 = s.charAt(i+1);
                char ch2 = s.charAt(i+2);
                i += 2;
                if (ch1 == '0' && ch2 == '0') { sb.append('\0'); }
                else if (ch1 == '0' && ch2 == 'A') { sb.append('\n'); }
                else if (ch1 == '0' && ch2 == 'D') { sb.append('\r'); }
                else if (ch1 == '2' && ch2 == 'C') { sb.append(','); }
                else if (ch1 == '7' && ch2 == 'D') { sb.append('}'); }
                else if (ch1 == '2' && ch2 == '5') { sb.append('%'); }
                else {throw new IOException("Error deserializing string.");}
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @return 
     */
    static String toXMLBuffer(byte barr[]) {
        if (barr == null || barr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(2*barr.length);
        for (int idx = 0; idx < barr.length; idx++) {
            sb.append(Integer.toHexString(barr[idx]));
        }
        return sb.toString();
    }
    
    /**
     * 
     * @param s 
     * @throws java.io.IOException 
     * @return 
     */
    static byte[] fromXMLBuffer(String s)
    throws IOException {
        ByteArrayOutputStream stream =  new ByteArrayOutputStream();
        if (s.length() == 0) { return stream.toByteArray(); }
        int blen = s.length()/2;
        byte[] barr = new byte[blen];
        for (int idx = 0; idx < blen; idx++) {
            char c1 = s.charAt(2*idx);
            char c2 = s.charAt(2*idx+1);
            barr[idx] = Byte.parseByte(""+c1+c2, 16);
        }
        stream.write(barr);
        return stream.toByteArray();
    }
   
    
    /**
     * 
     * @param date ����
     * @param patteran  ��ʽ���������
     * @return
     */
    public static String getDateString(Date date,String pattern){
    	SimpleDateFormat sf = new SimpleDateFormat(pattern);
    	return sf.format(date);
    }
    
    
    /**
     * �ַ���ת��Ӧ���ڸ�ʽ
     * @param dateStr �ַ�������
     * @param pattern  yyyyMMddHHmmss
     * @return
     */
    public static Date getDate(String dateStr,String pattern){
    	SimpleDateFormat sf = new SimpleDateFormat(pattern);
    	try {
			return sf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    /**
     * 
     * @param length ����ֵ
     * @param size ת��Ϊ�ַ�����ĳ���λ
     * @return ���ȵ��ַ���ʾ��ʽ �м���ӿո�
     */
    public static String toLenStr(int length,int size){
    	String lenStr = ""+length;
    	for (int i = lenStr.length(); i < size; i++) {
    		if( (i%2) == 0){
    			lenStr = "0 " + lenStr;
    		}else{
    			lenStr = "0" + lenStr;
    		}
    		
		}
    	return lenStr;
    }
    
    /**
     * 
     * @param length ����ֵ
     * @param size ת��Ϊ�ַ�����ĳ���λ
     * @return ���ȵ��ַ���ʾ��ʽ ��߲�0
     */
    public static String toLen(int length,int size){
    	String lenStr = ""+length;
    	for (int i = lenStr.length(); i < size; i++) {
    	   lenStr = "0" + lenStr;
		}
    	return lenStr;
    }
    
    public static String toMoneyStr(String money,int size){
    	String moneyStr = "000000000000";
    	if(money != null){
    		int mlen = money.length();
    		for (; mlen < size; mlen++) {
    			money = "0" + money;
			}
    		moneyStr = money;
    	}
    	return moneyStr;
    }
    
    
    public static String toBitMapStr(boolean[] bitmap){
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < bitmap.length; i++) {
    		if(i%8 == 0 && i > 0){//8�ı��� ��һ���ո�
    			sb.append(" ");
    		}
    		if(bitmap[i]){
				sb.append("1");
			}else{
				sb.append("0");
			}
		
		}
    	return sb.toString();
    }
    
    
    
	/**
	 * ��ȡָ������ǰһ����ַ���
	 * 
	 * @param patten
	 *            ��ʽ���ַ���
	 * @return
	 */
	public static String getBeforeDate(Date date, String patten) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-1);
		SimpleDateFormat sf = new SimpleDateFormat(patten);
		return sf.format(c.getTime());
	}
	
	
	
    
    /**
     * 
     * @param buf 
     * @return 
     */
   public static String toCSVBuffer(byte barr[]) {
        if (barr == null || barr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(barr.length + 1);
        sb.append('#');
        for(int idx = 0; idx < barr.length; idx++) {
            sb.append(Integer.toHexString(barr[idx]));
        }
        return sb.toString();
    }
    
    /**
     * Converts a CSV-serialized representation of buffer to a new
     * ByteArrayOutputStream.
     * @param s CSV-serialized representation of buffer
     * @throws java.io.IOException 
     * @return Deserialized ByteArrayOutputStream
     */
    static byte[] fromCSVBuffer(String s)
    throws IOException {
        if (s.charAt(0) != '#') {
            throw new IOException("Error deserializing buffer.");
        }
        ByteArrayOutputStream stream =  new ByteArrayOutputStream();
        if (s.length() == 1) { return stream.toByteArray(); }
        int blen = (s.length()-1)/2;
        byte[] barr = new byte[blen];
        for (int idx = 0; idx < blen; idx++) {
            char c1 = s.charAt(2*idx+1);
            char c2 = s.charAt(2*idx+2);
            barr[idx] = Byte.parseByte(""+c1+c2, 16);
        }
        stream.write(barr);
        return stream.toByteArray();
    }
    public static int compareBytes(byte b1[], int off1, int len1, byte b2[], int off2, int len2) {
    	int i;
    	for(i=0; i < len1 && i < len2; i++) {
    		if (b1[off1+i] != b2[off2+i]) {
    			return b1[off1+i] < b2[off2+1] ? -1 : 1;
    		}
    	}
    	if (len1 != len2) {
    		return len1 < len2 ? -1 : 1;
    	}
    	return 0;
    } 
    
	public static void main(String[] args){
	}

}
