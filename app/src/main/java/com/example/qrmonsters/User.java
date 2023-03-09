package com.example.qrmonsters;

import static java.lang.Character.isDigit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class User {
    String host_name;


    public static String getSha256Str(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    public static int getScore(String str) {
        int i = 1;
        int sum = 0;
        int count = 1;

        for(i = 1; i<str.length();i++) {
            if (str.charAt(i)==str.charAt(i-1)) {
                count ++;
            }else{
                if(count == 1) {
                    continue;
                }
                if(isDigit(str.charAt(i-1))) {
                    int x = str.charAt(i-1)-'0';
                    sum += Math.pow(x,(count-1));
                }else{
                    int x = str.charAt(i-1)-'a'+10;
                    sum += Math.pow(x,(count-1));
                }
                count = 1;

            }
        }
        if (count != 1) {
            if(isDigit(str.charAt(i-1))) {
                int x = str.charAt(i-1)-'0';
                sum += Math.pow(x,(count-1));
            }else{
                int x = str.charAt(i-1)-'a'+10;
                sum += Math.pow(x,(count-1));
            }
        }
        return sum;
    }
    /**
     * sha256加密 将byte转为16进制
     *
     * @param bytes 字节码
     * @return 加密后的字符串
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
