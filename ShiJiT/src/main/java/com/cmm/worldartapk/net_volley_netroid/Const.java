package com.cmm.worldartapk.net_volley_netroid;

/**
 * Created by Administrator on 2015/12/10.
 */
public class Const {
    // http parameters
    public static final int HTTP_MEMORY_CACHE_SIZE = 2 * 1024 * 1024; // 2MB
    public static final int HTTP_DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB
    public static final String HTTP_DISK_CACHE_DIR_NAME = "yt_sjt";
    public static final String USER_AGENT = "netroid.cn";

    //编码
    public static final String UTF_8 = "UTF-8";
    // 基本url
    public static final String BASE_URL = "http://cmm.yuntoo.com/";

    //三个主色
    public static final int PAGER1_COLOR = 0xffffad42;
    public static final int PAGER2_COLOR = 0xffff2942;
    public static final int PAGER3_COLOR = 0xff3ebdff;

    //是否在非WIFI下 加载数据
    public static boolean isWifi = false;
    //是否在判断wifi状态
    private static boolean judgeWifiState = false;

    //同步方法 get/set
    public static synchronized boolean getJudgeWifiState(){
        return judgeWifiState;
    }
    public static synchronized void setJudgeWifiState(boolean flag){
        Const.judgeWifiState = flag;
    }
}
