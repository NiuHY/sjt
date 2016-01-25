package com.cmm.worldartapk.net_volley_netroid.net_2;

import android.os.SystemClock;
import android.text.TextUtils;

import com.cmm.worldartapk.base.UserInfo;
import com.cmm.worldartapk.utils.PackageUtils;
import com.cmm.worldartapk.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/11.
 * <p/>
 * 设置请求参数的工具类，返回Map集合
 */
public class RequestMapData {


    /**
     * 搜索请求参数
     * @param limit  如果-1不加 每页多少
     * @param offset 如果-1不加 从哪开始
     * @return 搜索Map
     */
    public static Map<String, String> setSearchParams(String limit, String offset) {
        HashMap<String, String> paramsMap = new HashMap<String, String>(baseParamsMap());

        if (!TextUtils.equals(limit, "-1")) {
            paramsMap.put("limit", limit);
        }
        if (!TextUtils.equals(offset, "-1")) {
            paramsMap.put("offset", offset);
        }

//        paramsMap.put("offset", offset);
//        paramsMap.put("limit", limit);
//        paramsMap.put("key", key);

        return paramsMap;
    }

    /**
     * @param client_type    客户端类型 Android 5
     * @param uuid           设备id
     * @param client_version 客户端版本
     * @param session_key    登录用户session
     * @return 基本请求参数
     */
    public static Map<String, String> baseParamsMap() {
        HashMap<String, String> paramsMap = new HashMap<String, String>();
        //客户端类型 5  设备id  客户端版本号  session_key
        paramsMap.put("client_type", "5");
        paramsMap.put("uuid", SystemUtils.getIMEI());
        paramsMap.put("client_version", PackageUtils.getVersionCode() +"");
        paramsMap.put("session_key", UserInfo.getUserInfo().SESSION_KEY);

        return paramsMap;
    }

    /**
     * @return 收藏作品 请求参数
     */
    public static Map<String, String> params_artworkCollect() {

        HashMap<String, String> paramsMap = new HashMap<String, String>(baseParamsMap());

        return paramsMap;
    }

    /**
     * 登录请求参数
     * @return
     */
    public static Map<String, String> params_load(String email, String password) {

        HashMap<String, String> paramsMap = new HashMap<String, String>(baseParamsMap());

        //不需要session_key
        paramsMap.remove("session_key");

        paramsMap.put("email", email);
        paramsMap.put("password", password);

        return paramsMap;
    }

    /**
     * 注册请求参数
     * @return
     */
    public static Map<String, String> params_regist(String email, String password, String nickname) {

        HashMap<String, String> paramsMap = new HashMap<String, String>(baseParamsMap());

        //不需要session_key
        paramsMap.remove("session_key");

        paramsMap.put("email", email);
        paramsMap.put("password", password);
        paramsMap.put("nickname", nickname);

        return paramsMap;
    }


    public static Map<String, String> setCollectString(String loadUrl) {

        HashMap<String, String> paramsMap = new HashMap<String, String>(baseParamsMap());

        paramsMap.put("collectUrl", loadUrl);

        return paramsMap;
    }

    /**
     * 第三方登录
     * @param params 参数 map集合
     * @return
     */
    public static Map<String, String> params_otherLogin(HashMap<String, String> params) {

        params.putAll(baseParamsMap());

        return params;
    }
}
