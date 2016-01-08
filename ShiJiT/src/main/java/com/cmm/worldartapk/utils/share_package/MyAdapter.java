package com.cmm.worldartapk.utils.share_package;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * Created by Administrator on 2016/1/6.
 * 隐藏标题栏右部的ShareSDK Logo
 */
public class MyAdapter extends AuthorizeAdapter {
    public void onCreate() {
        // 隐藏标题栏右部的ShareSDK Logo
        hideShareSDKLogo();
    }
}
