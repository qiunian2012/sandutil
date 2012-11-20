package com.sand.sandutil.tool;


public class Base64Encoder {

    public static final char BaseTable[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/' };


    private static byte getByte(int i) {
        return (byte) BaseTable[i];
    };


    private static byte[] encode(byte A, byte B, byte C) {
        byte[] output = new byte[4];
        output[0] = getByte(((A & 0xFC) >> 2));
        output[1] = getByte((((A & 0x03) << 4) | ((B & 0xF0) >> 4)));
        output[2] = getByte((((B & 0x0F) << 2) | ((C & 0xC0) >> 6)));
        output[3] = getByte((C & 0x3F));
        return output;
    }


    private static byte[] encode(byte A, byte B) {
        byte[] output = new byte[4];
        output[0] = getByte(((A & 0xFC) >> 2));
        output[1] = getByte((((A & 0x03) << 4) | ((B & 0xF0) >> 4)));
        output[2] = getByte((((B & 0x0F) << 2)));
        output[3] = (byte) '=';
        return output;
    }


    private static byte[] encode(byte A) {
        byte[] output = new byte[4];
        output[0] = getByte(((A & 0xFC) >> 2));
        output[1] = getByte((((A & 0x03) << 4)));
        output[2] = (byte) '=';
        output[3] = (byte) '=';
        return output;
    }


    public static String encode(byte[] b) {
        int len = b.length;
        int i;
        byte[] encByte; 
        String encString = "";

        if (len == 0) {
            return "";
        }
  
        for (i = 0; i < len - 3; i += 3) {
            encByte = encode(b[i], b[i + 1], b[i + 2]);
            encString += new String(encByte);
        }
   
        switch (len - i) {
        case 1:
            encByte = encode(b[i]);
            encString += new String(encByte);
            break;
        case 2:
            encByte = encode(b[i], b[i + 1]);
            encString += new String(encByte);
            break;
        case 3:
            encByte = encode(b[i], b[i + 1], b[i + 2]);
            encString += new String(encByte);
            break;
        }

        return encString;
    }
}