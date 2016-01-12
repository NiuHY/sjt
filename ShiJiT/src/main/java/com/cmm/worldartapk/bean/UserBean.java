package com.cmm.worldartapk.bean;

/**
 * Created by Administrator on 2015/12/29.
 * 用户 Bean
 */
public class UserBean {
    public String success;
    public UserData data;

    public String error_code;
    public String error_message;

    public static class UserData{
        public String user_id;
        public String session_key;
        public String is_band;

        public String is_pro;
        public String user_avatar;
        public String user_email;
        public String user_intro;
        public String user_nickname;

        @Override
        public String toString() {
            return "UserData{" +
                    "user_id='" + user_id + '\'' +
                    ", session_key='" + session_key + '\'' +
                    ", is_band='" + is_band + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "success='" + success + '\'' +
                ", data=" + data +
                ", error_code='" + error_code + '\'' +
                ", error_message='" + error_message + '\'' +
                '}';
    }
}
