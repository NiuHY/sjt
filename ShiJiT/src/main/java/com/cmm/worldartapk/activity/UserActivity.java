package com.cmm.worldartapk.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.publicinfo.ConstInfo;

/**
 * Created by Administrator on 2015/12/18.
 */
public class UserActivity extends BaseActivity {

    private View contentView;
    private int loadCategory;

    @Override
    protected void init() {
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
    }


    public void goLoad(View view){
        // 打开登陆注册页面
        Intent intent = new Intent(this, LoadActivity.class);
        intent.putExtra("loadCategory", loadCategory);
        startActivity(intent);

    }
}
