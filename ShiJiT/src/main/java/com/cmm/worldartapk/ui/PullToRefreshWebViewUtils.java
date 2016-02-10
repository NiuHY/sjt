package com.cmm.worldartapk.ui;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.activity.MainActivity;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.utils.UIUtils;
import com.cmm.worldartapk.utils.WebViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * 下拉刷新的一些设置类
 * Created by Administrator on 2015/12/22.
 */
public class PullToRefreshWebViewUtils {
    private static MyViewPager viewPager;

//    private static WebView webView;

    public static WebView setListener_PRWebView(final PullToRefreshWebView pullToRefreshWebView) {

        final WebView webView = pullToRefreshWebView.getRefreshableView();

        // 添加错误页面
        final View errorPagerView = View.inflate(UIUtils.getContext(), R.layout.webview_errorpager, null);
        ((ViewGroup) webView.getParent()).addView(errorPagerView);

        //重写errerPagerView的onTounch方法
        errorPagerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // 添加加载页面
        final View loadingPagerView = View.inflate(UIUtils.getContext(), R.layout.loading_layout, null);
        ((ViewGroup) webView.getParent()).addView(loadingPagerView);

        //重写loadingPagerView的onTounch方法
        loadingPagerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // 设置监听 webView 初始化
        WebViewUtils.webViewBaseSet(webView, loadingPagerView, errorPagerView);

        // 设置监听
        pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WebView>() {
            @Override
            public void onPullDownToRefresh(final PullToRefreshBase<WebView> refreshView) {
                webView.reload();



                //屏蔽左右滑动
                if (BaseActivity.getForegroundActivity() instanceof MainActivity) {
                    if (viewPager == null){
                        viewPager = ((MainActivity) BaseActivity.getForegroundActivity()).getViewPager();
                    }

                    viewPager.setIsCanScroll(false);
                }

                // 超时
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullToRefreshWebView.isRefreshing()) {
                            pullToRefreshWebView.onRefreshComplete();
                            if (viewPager != null) {
                                viewPager.setIsCanScroll(true);
                            }
                        }
                    }
                }, 3999L);

            }

            @Override
            public void onPullUpToRefresh(final PullToRefreshBase<WebView> refreshView) {

                webView.loadUrl("javascript:listLoad()");

                //屏蔽左右滑动
                if (BaseActivity.getForegroundActivity() instanceof MainActivity) {
                    if (viewPager == null){
                        viewPager = ((MainActivity) BaseActivity.getForegroundActivity()).getViewPager();
                    }

                    viewPager.setIsCanScroll(false);
                }

//                超时
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pullToRefreshWebView.isRefreshing()) {
                            pullToRefreshWebView.onRefreshComplete();
                            if (viewPager != null) {
                                viewPager.setIsCanScroll(true);
                            }
                        }
                    }
                }, 4999L);
            }
        });

        return webView;
    }
}
