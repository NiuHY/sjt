package com.cmm.worldartapk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.activity.DetailPageActivity;
import com.cmm.worldartapk.publicinfo.ConstInfo;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;


/**
 * Created by Administrator on 2015/12/8.
 */
public class Pager2_Fragment extends WebViewBaseFragment {

    private static final String TAG = "Pager2_Fragment";

    /**
     * 第二页，内容
     */
    private static final String CONTENT_URL = "file:///android_asset/index_exhibition.html";
//    private static final String CONTENT_URL = "http://cmm.yuntoo.com/html/index_exhibition.html";

    @Override
    protected int getPagerIndex() {
        return ConstInfo.ZHANLAN;
    }

    @Override
    protected void setUrl(WebView webView) {
        //给webView 添加一个透明头 用了触发分享
        View detailPageHead = new View(UIUtils.getContext());

        //
        detailPageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempId = SJT_UI_Utils.getSharedPreferences().getString("exhibitionTitleId", "");
                if (!TextUtils.isEmpty(tempId)){
                    ConstJS_F.detailId = tempId;
                    Intent intent = new Intent(getActivity(), DetailPageActivity.class);
                    intent.putExtra("loadCategory", getPagerIndex());
                    UIUtils.startActivity(intent);
                }else {
                    UIUtils.showToastSafe("数据准备中...");
                }
            }
        });

        //添加头部分享
        webView.removeAllViews();
        webView.addView(detailPageHead, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2Px(275)));


        webView.loadUrl(CONTENT_URL);
    }

    @Override
    protected void initViewData(Bundle savedInstanceState) {

    }
}
