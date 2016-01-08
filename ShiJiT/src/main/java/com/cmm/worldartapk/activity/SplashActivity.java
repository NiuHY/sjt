package com.cmm.worldartapk.activity;

import android.os.Handler;
import android.view.View;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.utils.UIUtils;

/**
 * Created by Administrator on 2015/12/30.
 * 欢迎页
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void init() {

    }

    @Override
    protected View getContentView() {
        View contentView = View.inflate(UIUtils.getContext(), R.layout.splash_activity, null);
        return contentView;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setExitSwichLayout();
            }
        }, 2500L);
    }
}
