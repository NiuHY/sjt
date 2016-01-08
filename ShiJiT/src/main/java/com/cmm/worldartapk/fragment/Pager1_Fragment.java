package com.cmm.worldartapk.fragment;

import android.os.Bundle;
import android.webkit.WebView;

import com.cmm.worldartapk.publicinfo.ConstInfo;


/**
 * Created by Administrator on 2015/12/8.
 */
public class Pager1_Fragment extends WebViewBaseFragment {
    private static final String TAG = "Pager1_Fragment";

    /**
     * 第一页，内容
     */
//    private static final String CONTENT_URL = "http://cmm.yuntoo.com/html/index_information.html";
    private static final String CONTENT_URL = "file:///android_asset/index_information.html";


    @Override
    protected int getPagerIndex() {
        return ConstInfo.JINTAN;
    }

    @Override
    protected void setUrl(WebView webView) {
        webView.loadUrl(CONTENT_URL);
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {

    }
}
