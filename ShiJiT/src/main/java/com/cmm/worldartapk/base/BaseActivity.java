package com.cmm.worldartapk.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.activity.MainActivity;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;
import com.cmm.worldartapk.utils.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import cn.sharesdk.framework.ShareSDK;


/**
 * Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected SharedPreferences sp;


    // 静态的 可以得到前台Activity
    private static Activity mForegroundActivity;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //添加到已经打开的Activity集合
        UIUtils.getActivityList().add(this);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //注册广播接受者
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);

        //初始化sp
        sp = SJT_UI_Utils.getSharedPreferences();

        //初始化数据
        init();
        //设置内容页布局
        contentView = getContentView();
        setContentView(contentView);

        //进入动画
        setEnterSwichLayout();

        //初始化View
        initView();
    }

    /**
     * 初始化数据
     */
    protected abstract void init();

    /**
     * @return 返回Activity的内容View
     */
    protected abstract View getContentView();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    @Override
    protected void onResume() {
        super.onResume();
        this.mForegroundActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mForegroundActivity = null;
    }


    /**
     * @return 获取前台Activity
     */
    public static Activity getForegroundActivity() {
        return mForegroundActivity;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        if (myNetReceiver != null) {
            unregisterReceiver(myNetReceiver);
        }

        if (webView != null) {
            try {
                ViewUtils.removeSelfFromParent(webView);
                webView.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //从Activity集合中移除
        UIUtils.getActivityList().remove(this);
        //如果
        if (UIUtils.getActivityList().isEmpty()){
            SJT_UI_Utils.getSharedPreferences().edit().putString("uif", "").apply();
        }
    }

    // 接收WebView对象
    protected WebView webView;

    public WebView getWebView() {
        return webView;
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
    }

    // 初始化双击时间
    long currentTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // WebView返回上一页
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView != null && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        //如果当前Activity是 MainActivity双击返回键退出
        else if (keyCode == KeyEvent.KEYCODE_BACK && this instanceof MainActivity) {
            //双击
            if (currentTime < (SystemClock.currentThreadTimeMillis() - 250L)) {
                //两次点击间隔小于500毫秒，Toast
                UIUtils.showToastSafe("再次点击返回键退出");

                currentTime = SystemClock.currentThreadTimeMillis();
            } else {
                //在500毫秒内再次点击了返回键，退出
                currentTime = 0;
                //TODO 退出
//                setExitSwichLayout();
                //让应用退出虚拟机
                //停止分享，释放资源
                ShareSDK.stopSDK(this);

                //清空登录信息
                SJT_UI_Utils.getSharedPreferences().edit().putString("uif", "").apply();

                System.exit(0);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setExitSwichLayout();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //进入动画
    public void setEnterSwichLayout() {
        overridePendingTransition(R.anim.setg_next_in, R.anim.setg_next_out);
    }

    //退出动画
    public void setExitSwichLayout() {
        finish();
        overridePendingTransition(R.anim.setg_pre_in, R.anim.setg_pre_out);
    }


    //网络监听的广播接收者
    private ConnectivityManager mConnectivityManager;
    private NetworkInfo netInfo;
    private int inCount = 0;
    protected boolean isRefush = false;
    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ++inCount;

                if (inCount > 1) {
                    mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    netInfo = mConnectivityManager.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.isAvailable()) {

                        /////////////网络连接
                        String name = netInfo.getTypeName();

                        UIUtils.showToastSafe("网络已经连接");
//                        if (isRefush){
//                            isRefush = false;
//                            //关闭当前页面再次打开
//                            setExitSwichLayout();
//                            startActivity(new Intent(mForegroundActivity, MainActivity.class));
//                        }
                        //重新加载

                        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            /////WiFi网络

                        } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                            /////有线网络

                        } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            /////////3g网络

                        }
                    } else {
                        ////////网络断开
                        UIUtils.showToastSafe("网络已经断开");
                    }
                }


            }

        }
    };


    protected PullToRefreshWebView currentPullToRefreshWebView;

    /**
     * 给外界使用 用来完成刷新
     *
     * @return
     */
    public PullToRefreshWebView getCurrentPullToRefreshWebView() {
        return currentPullToRefreshWebView;
    }

    public void setCurrentPullToRefreshWebView(PullToRefreshWebView currentPullToRefreshWebView) {
        this.currentPullToRefreshWebView = currentPullToRefreshWebView;
    }
}
