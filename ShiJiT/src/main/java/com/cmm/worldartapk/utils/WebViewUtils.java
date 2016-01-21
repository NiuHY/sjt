package com.cmm.worldartapk.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.InjectedChromeClient;
import com.cmm.worldartapk.SafeWebViewBridge.JsCallJava;
import com.cmm.worldartapk.SafeWebViewBridge.js.JsScope;
import com.cmm.worldartapk.base.BaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;


/**
 * Created by Administrator on 2015/12/11.
 * WebView 延展出的 工具类
 */
public class WebViewUtils {


    /**
     * WebView 基本设置
     *
     * @param webView
     * @param loadingPager
     * @param errorPager
     */
    public static void webViewBaseSet(final WebView webView, final View loadingPager, final View errorPager) {

//        webView.reload();

        final Context context = webView.getContext();

        final WebSettings settings = webView.getSettings();

        //不使用缓存
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

////        关闭硬件加速
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }

        //背景
        webView.setBackgroundColor(0x00000001);


        if (Build.VERSION.SDK_INT >= 16) {
            // 跨域
            settings.setAllowUniversalAccessFromFileURLs(true);
        }

        //图片不自动加载，在网页加载完成后才加载
        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        //JS可用
        settings.setJavaScriptEnabled(true);
        //给webView 关联 需要JS调用的类
        // 需要 new新的，每个WebView对应不同的MyWebChromeClient对象
        webView.setWebChromeClient(new MyWebChromeClient("ADS", JsScope.class));
        //可以通过触摸获取焦点
        webView.requestFocusFromTouch();
        //页面大小自适应
        settings.setLoadWithOverviewMode(true);
//        //背景透明
        webView.setBackgroundColor(Color.alpha(0x00111111));



        //在WebView中加载后续页面
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                UIUtils.startActivity(intent);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //加载完成开启图片自动加载
                if (!settings.getLoadsImagesAutomatically()) {
                    settings.setLoadsImagesAutomatically(true);
                }

                // 隐藏加载中页面
                if (loadingPager != null) {
                    loadingPager.setVisibility(View.GONE);
                    loadingPager.clearAnimation();
                }

                //得到WebView所在的Activity(BaseActivity)如果还在刷新就完成
                if (context instanceof BaseActivity){
                    PullToRefreshWebView currentPullToRefreshWebView = ((BaseActivity) context).getCurrentPullToRefreshWebView();
                    if (currentPullToRefreshWebView != null && currentPullToRefreshWebView.isRefreshing()){
                        currentPullToRefreshWebView.onRefreshComplete();
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

//                //隐藏加载错误页面
//                if (errorPager != null) {
//                    errorPager.setVisibility(View.GONE);
//                }

                //显示加载中页面
                if (loadingPager != null) {
                    View loadingAnim = loadingPager.findViewById(R.id.loading_anmi);
                    //旋转动画
                    RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(1234L);
                    rotateAnimation.setRepeatCount(-1);
                    rotateAnimation.setRepeatMode(Animation.RESTART);
                    rotateAnimation.setInterpolator(new LinearInterpolator());

                    //开启动画
                    loadingAnim.startAnimation(rotateAnimation);
                    loadingPager.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingPager.getVisibility() == View.VISIBLE){
                                loadingPager.setVisibility(View.GONE);
                                loadingPager.clearAnimation();
                                UIUtils.showToastSafe("资源初始化中...");
                            }
                        }
                    }, 6000L);
                }
            }

            //自定义出错界面
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                //显示加载错误页面，(重新加载按钮)
                if (errorPager != null) {
                    errorPager.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    // 自定义 WebChromeClient
    public static class MyWebChromeClient extends InjectedChromeClient {

        public MyWebChromeClient(String injectedName, Class injectedCls) {
            super(injectedName, injectedCls);
        }

        public MyWebChromeClient(JsCallJava jsCallJava) {
            super(jsCallJava);
        }


        // ============= 重写的三个方法
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            // TODO  code
            // ...
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // TODO  code
            // 当进度条开始走，显示正在加载，进度条走完，才显示webView
//            UIUtils.showToastSafe("当前加载进度" + newProgress);

        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            // TODO  code
            // ...
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    }
}
