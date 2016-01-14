package com.cmm.worldartapk.SafeWebViewBridge.js;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.webkit.WebView;

import com.cmm.worldartapk.activity.DetailPageActivity;
import com.cmm.worldartapk.activity.MainActivity;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.base.UserInfo;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * Created by Administrator on 2015/12/11.
 * JS 调用  public static，且必须包含WebView(第一个参数位置)
 */
public class JsScope {

    /**
     * 系统弹出提示框
     *
     * @param webView 浏览器
     * @param message 提示信息
     */
    public static void alert(WebView webView, String message) {
        // 构建一个Builder来显示网页中的alert对话框  通过前台Activity
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.getForegroundActivity());
        AlertDialog alertDialog = builder.create();
        alertDialog.setMessage(message);

        alertDialog.show();
    }

    /**
     * 获取用户系统版本大小
     *
     * @param webView 浏览器
     * @return 安卓SDK版本
     */
    public static int getOsSdk(WebView webView) {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @param webView 浏览器
     * @return 测试字符串
     */
    public static String getTestString(WebView webView) {
        return "返回字符串";
    }

    /**
     * Toast提醒
     *
     * @param webView 浏览器
     * @param message 提示信息
     */
    public static void toast(WebView webView, String message) {
        UIUtils.showToastSafe(message);
    }

    /**
     * 开启详情页
     *
     * @param webView  浏览器
     * @param category 从哪里打开 1 2 3
     */
    public static void startDetailActivity(WebView webView, String detailId, int category) {
//        UIUtils.showToastSafe(webView.getContext().toString());
        //保存 detailId
        ConstJS_F.detailId = detailId;
        //开启详情页
        start(webView, category, DetailPageActivity.class);
    }

    //get/set
    public static String getDetailId(WebView webView) {
        return ConstJS_F.detailId;
    }
    public static void setDetailId(WebView webView, String detailId) {
        ConstJS_F.detailId = detailId;
    }

    /**
     * 用Activity的名字开启它
     *
     * @param webView      浏览器
     * @param category     从哪里打开 1 2 3
     * @param activityName 要开启的Activity类名
     */
    public static void startActivity(WebView webView, int category, String activityName) {
        //拼接包名
        String className = "com.cmm.worldartapk.activity." + activityName;
        //得到字节码
        try {
            Class cla = Class.forName(className);
            //开启详情页
            start(webView, category, cla);

        } catch (ClassNotFoundException e) {
            UIUtils.showToastSafe("页面为空");
            e.printStackTrace();
        }
    }


    //开启一个Activity
    private static void start(WebView webView, int category, Class cla) {
        // 获取 webView 所在的 Activity 通过它打开详情页，在详情页中加载对于url
        Context context = webView.getContext();
        if (context instanceof Activity) {
            Intent intent = new Intent(context, cla);
            //传url
//            intent.putExtra("loadUrl", url);
            //起始页分类
            intent.putExtra("loadCategory", category);
//            context.startActivity(intent);
            UIUtils.startActivity(intent);//TODO JsScope startActivity
        }
    }


    private static SharedPreferences sp;

    /**
     * 往sp中保存数据
     *
     * @param webView 浏览器
     * @param key     保存到sp中的名字  键
     * @param str     boolean int 不同类型的参数重载 保存到sp中的内容  值
     */
    public static void setSP(WebView webView, String key, String str) {
        if (sp == null){
            sp = SJT_UI_Utils.getSharedPreferences();
        }
        sp.edit().putString(key, str).apply();
    }

    /**
     * 从sp中获取数据，需要设置没有获取到数据的默认值
     *
     * @param webView 浏览器
     * @param key     保存到sp中的名字  键
     * @param str     没有获取到数据的默认值
     * @return 通过key(键)获取sp中保存的值
     */
    public static String getSp(WebView webView, String key, String str) {
        return sp.getString(key, str);
    }

    /**
     * 已经有默认值的获取方法，String ""
     *
     * @param webView
     * @param key
     * @return
     */
    public static String getStringBySp(WebView webView, String key) {
        return sp.getString(key, "");
    }

    /**
     * @param webView
     * @param imgsJson 预览图片 json
     * @param index 图片index
     */
    public static void startImgPreview(WebView webView, String imgsJson, int index) {
        Context context = webView.getContext();
        if (context instanceof DetailPageActivity) {
//            UIUtils.showToastSafe(imgsJson);
//            System.out.println("================== " + imgsJson);
              ((DetailPageActivity) context).showVPWindow(imgsJson, index);
        }
    }

    /**
     *
     * @param webView
     */
    public static void contentLoad(WebView webView){
        UIUtils.showToastSafe("加载完毕");
    }


    /**
     * 显示TitleView
     * @param webView
     */
    public static void showTitleView(WebView webView){
        Context context = webView.getContext();
        if (context instanceof MainActivity) {
            ((MainActivity) context).showTitleView();
        }
    }

    /**
     * 隐藏TitleView
     * @param webView
     */
    public static void hideTitleView(WebView webView){
        Context context = webView.getContext();
        if (context instanceof MainActivity) {
            ((MainActivity) context).hideTitleView();
        }
    }


    /**
     * 得到用户信息
     * @return 返回用户信息
     */
    public static String getUserInfo(WebView webView, String info){
        String userInfo = "";
        switch (info){
            case "test":
                userInfo = UserInfo.getUserInfo().test;
                break;
            case "user_id":
                userInfo = UserInfo.getUserInfo().USER_ID;
                break;
            case "session_key": //sessionKey
                userInfo = UserInfo.getUserInfo().SESSION_KEY;
                break;
            case "user_intro": //云图用户信息
                userInfo = UserInfo.getUserInfo().USER_INTRO;
                break;
            default:
                break;
        }
        return userInfo;
    }

    /**
     * 刷新完成
     * @param webView
     */
    public static void onRefreshComplete(WebView webView){
        Context context = webView.getContext();
        if (context instanceof BaseActivity) {
            PullToRefreshWebView currentPullToRefreshWebView = ((BaseActivity) context).getCurrentPullToRefreshWebView();
            if (currentPullToRefreshWebView != null && currentPullToRefreshWebView.isRefreshing()){
                currentPullToRefreshWebView.onRefreshComplete();
            }
        }
    }
}
