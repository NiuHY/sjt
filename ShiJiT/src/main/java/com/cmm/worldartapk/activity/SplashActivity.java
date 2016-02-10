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
        View view = new View(UIUtils.getContext());
        view.setBackgroundResource(R.drawable.startimg);
        return view;
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
