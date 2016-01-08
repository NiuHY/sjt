package com.cmm.worldartapk.bean.parser;

import com.cmm.worldartapk.bean.UserBean;
import com.cmm.worldartapk.net_volley_netroid.net_2.BaseParser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/12/11.
 */
public class UserInfoParser extends BaseParser<UserBean> {
    @Override
    public UserBean parserJson(String json) {
        //解析JavaBean
        Gson gson = new Gson();
        UserBean userInfo = gson.fromJson(json, UserBean.class);
        return userInfo;
    }
}
