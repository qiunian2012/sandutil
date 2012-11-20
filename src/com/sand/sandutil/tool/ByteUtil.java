package com.sand.sandutil.tool;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import com.sand.sandutil.contants.I;
/**
 * @author Nano
 * 
 * 字节操作常规类
 *
 */
public class ByteUtil {
	
	/**
     * 合并两个byte数组。合并后的数组将bs1放在在bs2的前面。
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
     * 截取出byte[]数组的一部分
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
     * 左、右、两边填充字符
     * 
     * @param bs
     *            待填充的字符串，可以为null
     * @param fillByte
     *            填充的字节
     * @param fillSide 
     *            填充的方向，['left','right','both']
     * @param size
     *            输出字符串的固定byte长度。
     * @return String
     */
    public static byte[] fillByte(byte[]bs, byte fillByte, String fillSide, int size) {
        if (bs.length > size) 
            throw new RuntimeException("需填充的byte数组长度" + bs.length + "大于了填充总长度" + size);
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
     * @param bs 待填充的字符串，不可以为null
     * @param fillByte 填充的字节
     * @param fillType 填充的方向，['left','right','both']
     * @param fillLen  填充的个数
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
     * 去除(左、右、两边)填充的字节
     * 
     * @param bs      待去除字符的字节数组
     * @param trimByte 待去除的字节
     * @param trimSide 方向（'left','right','both'）
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
     * 原理：<br>
     * 例如： Ascii: 1234 转换为 BCD: **<br>
     * 
     * 字符 0123456789:;<=>? 共16个字符的Hex码为[0x3*]其中3表示前4位，*表示后4位，*为0-F，所以在压缩时，
     * 舍弃了字符的前4位，而将两个字符的后4位来组成一个新8位的字符。
     * <p>
     * 
     * @param bs
     *            待转换的ascii字节数组，必须为偶数。
     * @return 转为bcd后的字节数组。
     */
    public static byte[] ascii2bcd(byte[] bs) {
        byte[] res = new byte[bs.length / 2];
        for (int i = 0, n = bs.length; i < n; i += 2)
            res[i / 2] = (byte) ((bs[i] << 4) | (bs[i + 1] & 0x0f));
        return res;
    }
    
    /**
     * BCD->ASCII原理：
     * <p>
     * 拆分byte的前4位和后4位，然后将这两个4位前面补上0x30(ascii中0123456789:; <=>的共同前4位) 组成两个新的byte.
     * <p> 
     * @param bs
     *            待转换的bcd字节数组。
     * @return 转换为ascii后的字节数组。
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
     * Ascii转换为16进制方式： <br>
     * 例如：字符AB转换为0xAB对应的字符。
     * 
     * @param bs
     *            待转换的byte数组
     * @return 压缩为16进制后的byte数组
     */
    public static byte[] ascii2hex(byte[] bs) {
        byte[] res = new byte[bs.length / 2];
        for (int i = 0, n = bs.length; i < n; i += 2) {
            res[i / 2] = (byte) (Integer.parseInt(new String(bs, i, 2), 16));
        }
        return res;
    }

    /**
     * 16进制转换为Ascii
     * <p>
     * 例如： 字符0xAB转换为Ascii的两个字符AB
     * 
     * @param bs
     *            待转换的16进制的数组
     * @return 解压后的Ascii数组
     */
    public static byte[] hex2ascii(byte[] bs) {
        byte[] res = new byte[bs.length * 2];
        for (int i = 0; i < bs.length; i++) {
            //需要将大于127的byte转换为正的int型
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
     * 自动类型转换控制器
     * <p>
     * 目前支持4中类型转换： <br>
     * Ascii -> BCD <br>
     * Ascii -> Hex <br>
     * BCD -> Ascii <br>
     * Hex -> Ascii <br>
     * self -> self <br>
     * 
     * @param bs
     *            代转换的byte数组
     * @param sourceType ["asc","bcd","hex","binary"]
     *            原编码方式
     * @param targetType ["asc","bcd","hex","binary"]
     *            目标编码方式
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
            throw new RuntimeException("不支持从类型" + s + "向" + t + "的转换。");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * @param bs 字节数组
     * @param objectType 对象类型
     * @return objectType 对应的对象类型
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
     * @param value 对象
     * @param pattern 对象格式化方式 如果是日期类型 用到该值  yyyyMMdd yyMMddHHmmss
     * @return 对象ASCII数组
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
     * 组装BitMap <p>
     * 将代表位图的boolean数组转化为长度length/8后的byte数组。
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
     * 拆分BitMap <p>
     * 将字节数组转化为长度length*8之后的boolean位图数组
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
     * 将16进制的字符串转换为byte[]
     * @param str
     * @return byte[]
     */
    public static byte[] hex2bytes(String str) {
    	if(!(StringUtil.patternHex.matcher(str).matches())||str.length()%2!=0)
    		throw new RuntimeException("16进制字符串不能转换为byte[]，包含了非法字符或长度不是2的倍数。原字符串："+str);
    	byte[]bs=new byte[str.length()/2];
    	for(int i=0;i<str.length();i+=2){
    		int nn=Integer.parseInt(str.substring(i,i+2),16);
    		bs[i/2]=(byte)nn;
    	}
    	return bs;
    }
    
    /**
     * 将byte[]数组转化为16进制字符串
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
     * 将byte[]数组转化为HEXDECIMAL
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
     * 将十进制数转化为十六进制数
     */
    public static String intNum2HexStr(int intNum){
    	return Integer.toString(intNum, 16);
    }
    
    /**
     * 将十六进制数转化为十进制数
     */
    public static String hexNum2IntStr(String hexNum){
    	return Integer.valueOf(hexNum,16).toString();
    }
    
    /**
     * 
     * @param ascii ascii 码的十六进制字符串
     * @return
     * @throws SandException
     */
    public static String ascii2str(String ascii){
    	StringBuffer sb = new StringBuffer();
    	if(ascii.length()%2 != 0 ) throw new RuntimeException("转化的ascii码字符串长度不是2的倍数");
    	for (int i = 0; i < ascii.length();i=i+2) {
    		String s = ascii.substring(i, i+2);
    		sb.append(""+(char)Integer.parseInt(hexNum2IntStr(s)));
		}
    	return sb.toString();
    }
    
    /**
     * @param bs 压缩后的字节 bcd hex
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
     * @param bs 未压缩的字节 Ascii
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
     * 将十六进制数转化为十进制数
     */
    public static String hexNum2IntegerStr(String hexNum){
    	return Integer.valueOf(hexNum,16).toString();
    }
	
	public static void main(String []args){
	}
	
}
