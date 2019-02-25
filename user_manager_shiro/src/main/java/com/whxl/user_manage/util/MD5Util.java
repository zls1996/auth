package com.whxl.user_manage.util;

import java.security.MessageDigest;

/**
 * MD5加密工具
 *
 */
public class MD5Util {
    public static final String[] hexDigIts = {
            "0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"
    };

    /**
     * 字符串加密
     * @param str
     * @return
     */
    public static String encode(String str, String charsetname){
        String resultString = null;
        try{
            resultString = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if(null == charsetname || "".equals(charsetname)){
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }else{
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        }catch (Exception e){
        }
        return resultString;

    }

    /**
     * 将字节数组转换成16进制
     * @param b
     * @return
     */
    public static String byteArrayToHexString(byte b[]){
        StringBuffer resultSb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }

    public static void main(String[] args) {
        String str = "123456";
        String result = encode(str, "UTF-8");
        System.out.println(result);
    }

}
