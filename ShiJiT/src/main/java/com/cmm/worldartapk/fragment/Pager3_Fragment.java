package com.cmm.worldartapk.fragment;

import android.os.Bundle;
import android.webkit.WebView;

import com.cmm.worldartapk.publicinfo.ConstInfo;


/**
 * Created by Administrator on 2015/12/8.
 */
public class Pager3_Fragment extends WebViewBaseFragment {
    private static final String TAG = "Pager3_Fragment";

    /**
     * 第三页，内容
     */
    private static final String CONTENT_URL = "file:///android_asset/index_gallery.html";
//    private static final String CONTENT_URL = "http://cmm.yuntoo.com/html/index_gallery.html";

    @Override
    protected int getPagerIndex() {
        return ConstInfo.YISHUGUAN;
    }

    @Override
    protected void setUrl(WebView webView) {
//        webView.loadUrl("file:///android_asset/test.html");
        webView.loadUrl(CONTENT_URL);
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {

    }
}
