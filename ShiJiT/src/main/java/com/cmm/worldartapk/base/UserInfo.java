package com.cmm.worldartapk.base;

/**
 * Created by Administrator on 2015/12/29.
 * 用户信息类，静态成员
 */
public class UserInfo {

    public static MyInfo getUserInfo() {
        if (userInfo == null){
            synchronized (UserInfo.class) {
                if (userInfo == null) {
                    userInfo = new MyInfo();
                }
            }

        }
        return userInfo;
    }

    public static void setUserInfo() {
        userInfo = null;
    }

    private static MyInfo userInfo;

    private UserInfo(){}

    public static class MyInfo{
        /**
         * 邮箱
         */
        public String EMAIL;
        /**
         * 密码
         */
        public String PASSWORD;
        /**
         * 昵称
         */
        public String NICKNAME;
        /**
         * 用户登陆后的session_key
         */
        public String SESSION_KEY;

        public String IS_PRO; // ???


        // ================== 第三方 ===============
        /**
         * 是否绑定
         */
        public String IS_BAND;
        /**
         * 访问令牌
         */
        public String ACCESSTOKEN;
        /**
         * 期限
         */
        public String EXPIRES_AT;
        /**
         * ？
         */
        public String AVATAR;
        /**
         * 三方平台名
         */
        public String PLATFORMNAME;
        /**
         * ？
         */
        public String PROFILEURL;
        /**
         * 用户名
         */
        public String USER_NAME;
        /**
         * 用户ID
         */
        public String USER_ID;

        /**
         * 云图用户信息
         */
        public String USER_INTRO;
        // ================== 第三方 ===============

        /**
         * 测试
         */
        public String test = "test";
    }
}
