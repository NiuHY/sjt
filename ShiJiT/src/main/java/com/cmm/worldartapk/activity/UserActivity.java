package com.cmm.worldartapk.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseGestureActivity;
import com.cmm.worldartapk.publicinfo.ConstInfo;
import com.cmm.worldartapk.ui.PullToRefreshWebViewUtils;
import com.cmm.worldartapk.utils.PreviewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * Created by Administrator on 2015/12/18.
 */
public class UserActivity extends BaseGestureActivity {

    private View contentView;
    private int loadCategory;
    //用户中心对应的webView url
    private String USER_URL = "file:///android_asset/user_center.html";
    private PreviewUtils previewUtils;

    @Override
    protected void init() {

        //初始化手势检测器
        super.init();

        //
        Intent intent = getIntent();
        //获取从哪里打开的个人中心
        loadCategory = intent.getIntExtra("loadCategory", ConstInfo.JINTAN);
    }

    @Override
    protected View getContentView() {
        contentView = View.inflate(this, R.layout.user_activity, null);
        return contentView;
    }

    @Override
    protected void initView() {
        //返回按钮
        ImageButton myBack = (ImageButton) findViewById(R.id.bt_back);
        //设置按钮
        ImageButton mySetting = (ImageButton) findViewById(R.id.bt_setting);
        //变背景
        switch (loadCategory) {
            case 1:
                myBack.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                mySetting.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                break;
            case 2:
                myBack.setBackgroundResource(R.drawable.icon_back_bg_red);
                mySetting.setBackgroundResource(R.drawable.icon_back_bg_red);
                break;
            case 3:
                myBack.setBackgroundResource(R.drawable.icon_back_bg_blue);
                mySetting.setBackgroundResource(R.drawable.icon_back_bg_blue);
                break;
            default:
                break;
        }
        //关闭
        myBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                setExitSwichLayout();
//                overridePendingTransition(R.anim.setg_pre_in, R.anim.setg_pre_out);
            }
        });
        //设置
        mySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, SettingActivity.class));
            }
        });


        //内容WebView
        final PullToRefreshWebView pullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id.user_webview_fl);
        // 初始化 webView
        webView = PullToRefreshWebViewUtils.setListener_PRWebView(pullToRefreshWebView);
        //给当前Activity注册下拉刷新View
        setCurrentPullToRefreshWebView(pullToRefreshWebView);

        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WebView>() {
            @Override
            public void onPullDownToRefresh(final PullToRefreshBase<WebView> refreshView) {
                webView.reload();
            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<WebView> refreshView) {

                webView.loadUrl("javascript:waitADS().listLoad()");
//                超时
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullToRefreshWebView.isRefreshing()) {
                            pullToRefreshWebView.onRefreshComplete();
                        }
                    }
                }, 4999L);
            }
        });
        // 当前Activity绑定的webView
        setWebView(webView);

        //往右划关闭当前页
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gDetector.onTouchEvent(event);

                return false;
            }
        });

        //加载 url
        webView.loadUrl(USER_URL);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在按返回键时判断是否打开了窗口，如果打开了就先关闭它
        if (keyCode == KeyEvent.KEYCODE_BACK && previewUtils != null &&  previewUtils.isWindowViewShow()) {
            previewUtils.removeView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (previewUtils != null && previewUtils.isWindowViewShow()) {
            previewUtils.removeView();
        }
    }
    /**
     * 分享
     *
     * @param imgsJson
     * @param index
     */
    public void showVPWindow(String imgsJson, int index) {
        previewUtils = new PreviewUtils(this, loadCategory);
        previewUtils.showVPWindow(imgsJson, index);
    }


//    public void goLoad(View view){
//
////        String json = JsScope.getStringBySp(null, "information");
////        LogUtils.e(json);
//
////        // 打开登录注册页面
////        Intent intent = new Intent(this, LoginActivity.class);
////        intent.putExtra("loadCategory", loadCategory);
////        startActivity(intent);
//
////        //图片预览
////        PreviewUtils previewUtils = new PreviewUtils(this, loadCategory);
////
////        previewUtils.showVPWindow(ConstJS_F.json, 0);
//
//        UIUtils.showToastSafe(UserInfo.getUserInfo().SESSION_KEY);
//    }
}
