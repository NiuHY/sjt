package com.cmm.worldartapk.utils;

import android.util.Base64;

/**
 * Created by Administrator on 2016/1/13.
 */
public class CodeUtils {

    // ===================================== BASE64 ================================= 待
    /**
     * 加密 String --> String
     * @param str 要加密的字符串
     * @return
     */
    public static String encodeByBase64(String str){
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }

    /**
     * 解密 String --> String
     * @param str 要解码的字符串
     * @return
     */
    public static String decodeByBase64(String str){
        return new String(Base64.decode(str, Base64.NO_WRAP));
    }


    /**
     * 加密 byte[] --> byte[]
     * @param byteArray 要加密的字节数组(图片什么的)
     * @return
     */
    public static byte[] encodeByBase64ToByteArray(byte[] byteArray){
        return Base64.encode(byteArray, Base64.DEFAULT);
    }

    /**
     * 解密 byte[] --> byte[]
     * @param byteArray 要解码的字节数组
     * @return
     */
    public static byte[] decodeByBase64FromByteArray(byte[] byteArray){
        return Base64.decode(byteArray, Base64.DEFAULT);
    }

}
