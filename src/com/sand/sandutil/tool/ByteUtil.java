package com.sand.sandutil.tool;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import com.sand.sandutil.contants.I;
/**
 * @author Nano
 * 
 * �ֽڲ���������
 *
 */
public class ByteUtil {
	
	/**
     * �ϲ�����byte���顣�ϲ�������齫bs1������bs2��ǰ�档
     * @param bs1
     * @param bs2
     * @return byte[]
     */
    public static byte[] union(byte[]bs1,byte[]bs2){
        byte[]bs=new byte[bs1.length+bs2.length];
        for(int i=0;i<bs1.length;i++)
            bs[i]=bs1[i];
        for(int i=0;i<bs2.length;i++)
            bs[bs1.length+i]=bs2[i];
        return bs;
    }
    
    /**
     * ��ȡ��byte[]�����һ����
     * @param bs
     * @param first
     * @param length
     * @return byte[]
     */
    public static byte[] sub(byte[]bs,int first,int length){
    	length=(first+length)>bs.length?bs.length-first:length;
    	byte[]nb=new byte[length];
    	for(int i=0;i<length;i++)
    		nb[i]=bs[i+first];
    	return nb;
    }
    
 
    /**
     * ���ҡ���������ַ�
     * 
     * @param bs
     *            �������ַ���������Ϊnull
     * @param fillByte
     *            �����ֽ�
     * @param fillSide 
     *            ���ķ���['left','right','both']
     * @param size
     *            ����ַ����Ĺ̶�byte���ȡ�
     * @return String
     */
    public static byte[] fillByte(byte[]bs, byte fillByte, String fillSide, int size) {
        if (bs.length > size) 
            throw new RuntimeException("������byte���鳤��" + bs.length + "����������ܳ���" + size);
        int n = size - bs.length;
        byte[]tb={fillByte};
        if ("left".equalsIgnoreCase(fillSide)) {
        	for (int i = 0; i < n; i++) {
        		bs = union(tb, bs);
        	}
        } else if ("right".equalsIgnoreCase(fillSide)) {
        	for (int i = 0; i < n; i++) {
        		bs = union(bs, tb);
        	}
        } else if ("both".equalsIgnoreCase(fillSide)) {
        	for (int i = 0; i < n; i++) {
        		if (i % 2 == 0) {
        			bs = union(tb, bs);
        		} else {
        			bs = union(bs, tb);
        		}
        	}
        }
        return bs;
    }
    
    /**
     * 
     * @param bs �������ַ�����������Ϊnull
     * @param fillByte �����ֽ�
     * @param fillType ���ķ���['left','right','both']
     * @param fillLen  ���ĸ���
     * @return
     */
    public static byte[] fillFixByte(byte[]bs, byte fillByte, String fillType, int fillLen) {
        int n = fillLen;
        byte[]tb={fillByte};
        if ("left".equalsIgnoreCase(fillType)) {
        	for (int i = 0; i < n; i++) {
        		bs = union(tb, bs);
        	}
        } else if ("right".equalsIgnoreCase(fillType)) {
        	for (int i = 0; i < n; i++) {
        		bs = union(bs, tb);
        	}
        } else if ("both".equalsIgnoreCase(fillType)) {
        	for (int i = 0; i < n; i++) {
        		if (i % 2 == 0) {
        			bs = union(tb, bs);
        		} else {
        			bs = union(bs, tb);
        		}
        	}
        }
        return bs;
    }
    
    /**
     * ȥ��(���ҡ�����)�����ֽ�
     * 
     * @param bs      ��ȥ���ַ����ֽ�����
     * @param trimByte ��ȥ�����ֽ�
     * @param trimSide ����'left','right','both'��
     * @return byte[]
     */
    public static byte[] trimByte(byte[] bs, byte trimByte, String trimSide) {
    	if (("left".equalsIgnoreCase(trimSide)) || ("both".equalsIgnoreCase(trimSide))) {
    		int i = 0;
    		for (; (i < bs.length) && (bs[i] == trimByte); ++i);
    			bs = sub(bs, i, bs.length - i);
    	}
    	
    	if (("right".equalsIgnoreCase(trimSide)) || ("both".equalsIgnoreCase(trimSide))) {
    		int i = bs.length - 1;
    		for (; (i >= 0) && (bs[i] == trimByte); --i);
    			bs = sub(bs, 0, i + 1);
    	}
    	return bs;
    }
    
    /**
     * ASCII -> BCD
     * <p>
     * ԭ��<br>
     * ���磺 Ascii: 1234 ת��Ϊ BCD: **<br>
     * 
     * �ַ� 0123456789:;<=>? ��16���ַ���Hex��Ϊ[0x3*]����3��ʾǰ4λ��*��ʾ��4λ��*Ϊ0-F��������ѹ��ʱ��
     * �������ַ���ǰ4λ�����������ַ��ĺ�4λ�����һ����8λ���ַ���
     * <p>
     * 
     * @param bs
     *            ��ת����ascii�ֽ����飬����Ϊż����
     * @return תΪbcd����ֽ����顣
     */
    public static byte[] ascii2bcd(byte[] bs) {
        byte[] res = new byte[bs.length / 2];
        for (int i = 0, n = bs.length; i < n; i += 2)
            res[i / 2] = (byte) ((bs[i] << 4) | (bs[i + 1] & 0x0f));
        return res;
    }
    
    /**
     * BCD->ASCIIԭ��
     * <p>
     * ���byte��ǰ4λ�ͺ�4λ��Ȼ��������4λǰ�油��0x30(ascii��0123456789:; <=>�Ĺ�ͬǰ4λ) ��������µ�byte.
     * <p> 
     * @param bs
     *            ��ת����bcd�ֽ����顣
     * @return ת��Ϊascii����ֽ����顣
     */
    public static byte[] bcd2ascii(byte[] bs) {
        byte[] res = new byte[bs.length * 2];
        for (int i = 0, n = bs.length; i < n; i++) {
            res[i * 2] = (byte) (((bs[i] & 0xf0) >> 4) | 0x30);
            res[i * 2 + 1] = (byte) ((bs[i] & 0x0f) | 0x30);
        }
        return res;
    }
    
    /**
     * Asciiת��Ϊ16���Ʒ�ʽ�� <br>
     * ���磺�ַ�ABת��Ϊ0xAB��Ӧ���ַ���
     * 
     * @param bs
     *            ��ת����byte����
     * @return ѹ��Ϊ16���ƺ��byte����
     */
    public static byte[] ascii2hex(byte[] bs) {
        byte[] res = new byte[bs.length / 2];
        for (int i = 0, n = bs.length; i < n; i += 2) {
            res[i / 2] = (byte) (Integer.parseInt(new String(bs, i, 2), 16));
        }
        return res;
    }

    /**
     * 16����ת��ΪAscii
     * <p>
     * ���磺 �ַ�0xABת��ΪAscii�������ַ�AB
     * 
     * @param bs
     *            ��ת����16���Ƶ�����
     * @return ��ѹ���Ascii����
     */
    public static byte[] hex2ascii(byte[] bs) {
        byte[] res = new byte[bs.length * 2];
        for (int i = 0; i < bs.length; i++) {
            //��Ҫ������127��byteת��Ϊ����int��
            int ti = bs[i];
            ti = ti < 0 ? ti + 256 : ti;
            String t = Integer.toHexString(ti);
            if (t.length() < 2) t = "0" + t;
            res[i * 2] = (byte) t.charAt(0);
            res[i * 2 + 1] = (byte) t.charAt(1);
        }
        return res;
    }
    
    /**
     * �Զ�����ת��������
     * <p>
     * Ŀǰ֧��4������ת���� <br>
     * Ascii -> BCD <br>
     * Ascii -> Hex <br>
     * BCD -> Ascii <br>
     * Hex -> Ascii <br>
     * self -> self <br>
     * 
     * @param bs
     *            ��ת����byte����
     * @param sourceType ["asc","bcd","hex","binary"]
     *            ԭ���뷽ʽ
     * @param targetType ["asc","bcd","hex","binary"]
     *            Ŀ����뷽ʽ
     * @return byte[]
     */
    public static byte[] transit(byte[]bs, String sourceType, String targetType) {
        try {
            String s = sourceType.toLowerCase();
            String t = targetType.toLowerCase();
            if(s.equals("binary")){
            	s = "ascii";
            }
            if(t.equals("binary")){
            	t = "ascii";
            }
            if (s.equals("ascii") && t.equals("bcd")) {
                return ascii2bcd(bs);
            } else if (s.equals("ascii") && t.equals("hex")) {
                return ascii2hex(bs);
            } else if (s.equals("bcd") && t.equals("ascii")) {
                return bcd2ascii(bs);
            } else if (s.equals("hex") && t.equals("ascii")) {
                return hex2ascii(bs);
            } else if (s.equals(t)) {
                return bs;
            }
            throw new RuntimeException("��֧�ִ�����" + s + "��" + t + "��ת����");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * @param bs �ֽ�����
     * @param objectType ��������
     * @return objectType ��Ӧ�Ķ�������
     */
    public static Object transitByte(byte[]bs, String objectType){
    	try{
    		String bt = objectType.toLowerCase();
            if("string".equals(bt)){
            	return new String(bs,I.ENCODEING_DEFAULT);
            }else if("byte[]".equals(bt)){
            	return bs;
            }else if("boolean[]".equals(bt)){
            	return splitBitMap(bs);
            }else if("bigdecimal".equals(bt)){
            	String amountStr = new String(bs,I.ENCODEING_UTF8);
            	return new BigDecimal(amountStr).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            }else if(bt.startsWith("date")){
            	String[] pArray = bt.split("_");
            	String dateStr = new String(bs,I.ENCODEING_UTF8);
            	return StringUtil.getDate(dateStr, pArray[1]);	
            }else {
            	return bs;
            }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return bs;
    }
    
    /**
     * @param value ����
     * @param pattern �����ʽ����ʽ ������������� �õ���ֵ  yyyyMMdd yyMMddHHmmss
     * @return ����ASCII����
     */
	public static byte[] transitObject(Object value,String pattern) {
		try {
			if (value == null) {
				return null;
			}
			if (value instanceof byte[]) {
				return (byte[]) value;
			} else if (value instanceof String) {
				try {
					return value.toString().getBytes(I.ENCODEING_DEFAULT);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (value instanceof BigDecimal) {
				BigDecimal amount = (BigDecimal)value;		
				String amountStr = amount.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_DOWN).toString();
			    for (;amountStr.length() < 12;) {
			    	amountStr = "0" + amountStr;
				}
				return amountStr.getBytes();
			} else if (value instanceof Date) {
				Date date = (Date)value;
				return StringUtil.getDateString(date, pattern).getBytes();
			} else if (value instanceof boolean[]) {
				boolean[] source = (boolean[]) value;
				return mackBitMap(source);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    
    
    /**
     * ��װBitMap <p>
     * ������λͼ��boolean����ת��Ϊ����length/8���byte���顣
     * 
     * @param source
     * @return byte[]
     */
    public static byte[] mackBitMap(boolean[] source) {
        int n = source.length / 8;
        byte[] bs = new byte[n];
        for (int i = 0; i < n; i++) {
            char c = 0x00;
            for (int j = 0; j < 8; j++) 
                c = source[8 * i + j] ? (char) (c | (1 << (7 - j))) : c;
            bs[i] = (byte) c;
        }
        return bs;
    }
    
    /**
     * ���BitMap <p>
     * ���ֽ�����ת��Ϊ����length*8֮���booleanλͼ����
     * 
     * @param bs
     * @return boolean[]
     */
    public static boolean[] splitBitMap(byte[] bs) {
        boolean[] res = new boolean[bs.length * 8];
        for (int i = 0; i < bs.length; i++) {
            int x = bs[i] < 0 ? (bs[i] + 256) : bs[i];
            for (int j = 0; j < 8; j++)
                res[i * 8 + j] = (x & 1 << (7 - j)) == (1 << (7 - j)) ? true : false;
        }
        return res;
    }
    
    
    /**
     * ��16���Ƶ��ַ���ת��Ϊbyte[]
     * @param str
     * @return byte[]
     */
    public static byte[] hex2bytes(String str) {
    	if(!(StringUtil.patternHex.matcher(str).matches())||str.length()%2!=0)
    		throw new RuntimeException("16�����ַ�������ת��Ϊbyte[]�������˷Ƿ��ַ��򳤶Ȳ���2�ı�����ԭ�ַ�����"+str);
    	byte[]bs=new byte[str.length()/2];
    	for(int i=0;i<str.length();i+=2){
    		int nn=Integer.parseInt(str.substring(i,i+2),16);
    		bs[i/2]=(byte)nn;
    	}
    	return bs;
    }
    
    /**
     * ��byte[]����ת��Ϊ16�����ַ���
     * @param bs
     * @return String
     */
    public static String bytes2hex(byte[]bs){
    	StringBuffer sb=new StringBuffer();
    	for (int i = 0; i < bs.length; i++) {
    		int nn = bs[i]< 0 ? bs[i] + 256 : bs[i];
    		String t = Integer.toHexString(nn).toUpperCase();
    		sb.append(t.length()<2?"0"+t:t);
		}
    	return sb.toString();
    }
    
    /**
     * ��byte[]����ת��ΪHEXDECIMAL
     * @param bs
     * @return String
     */
    public static String byte2HexDecimal(byte[] bs){
    	StringBuffer sb = new StringBuffer();
    	String bb = new String(bs);
    	for (int i = 0; i < bb.length(); i++) {
    		int n = Integer.parseInt(bb.substring(i, i+1));
    		String t = Integer.toString(n, 16).toUpperCase();
    		sb.append(t.length()<2?"0"+t:t);
		}
    	return sb.toString();
    }
    
    
    /**
     * ��ʮ������ת��Ϊʮ��������
     */
    public static String intNum2HexStr(int intNum){
    	return Integer.toString(intNum, 16);
    }
    
    /**
     * ��ʮ��������ת��Ϊʮ������
     */
    public static String hexNum2IntStr(String hexNum){
    	return Integer.valueOf(hexNum,16).toString();
    }
    
    /**
     * 
     * @param ascii ascii ���ʮ�������ַ���
     * @return
     * @throws SandException
     */
    public static String ascii2str(String ascii){
    	StringBuffer sb = new StringBuffer();
    	if(ascii.length()%2 != 0 ) throw new RuntimeException("ת����ascii���ַ������Ȳ���2�ı���");
    	for (int i = 0; i < ascii.length();i=i+2) {
    		String s = ascii.substring(i, i+2);
    		sb.append(""+(char)Integer.parseInt(hexNum2IntStr(s)));
		}
    	return sb.toString();
    }
    
    /**
     * @param bs ѹ������ֽ� bcd hex
     * @return
     */
    public static String toHexInfo(byte[]bs){
    	StringBuffer sb=new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            int nn = bs[i]< 0 ? bs[i] + 256 : bs[i];
            String t = Integer.toHexString(nn).toUpperCase();
            sb.append(t.length()<2?"0"+t:t).append(i<bs.length-1?" ":"");
        }
        return sb.toString();
    }
    
    /**
     * @param bs δѹ�����ֽ� Ascii
     * @return 
     */
    public static String toAsciiInfo(byte[] bs){
    	return new String(bs);
    }
    
	public static byte[] str2bcd(String s) {
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	public static String bcd2str(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int h = ((b[i] & 0xff) >> 4) + 48;
			sb.append((char) h);
			int l = (b[i] & 0x0f) + 48;
			sb.append((char) l);
		}
		return sb.toString();
	}
	
	/**
     * ��ʮ��������ת��Ϊʮ������
     */
    public static String hexNum2IntegerStr(String hexNum){
    	return Integer.valueOf(hexNum,16).toString();
    }
	
	public static void main(String []args){
	}
	
}
