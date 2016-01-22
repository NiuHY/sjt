package com.cmm.worldartapk.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.base.BaseGestureActivity;
import com.cmm.worldartapk.fragment.Load_Fragment;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.publicinfo.ConstInfo;

/**
 * Created by Administrator on 2015/12/23.
 * <p/>
 * 登陆页
 */
public class LoginActivity extends BaseGestureActivity {

    private View contentView;
    private int loadCategory;
    private FrameLayout frameLayout;
    private Load_Fragment loadFragment;


    @Override
    protected void init() {

        super.init();

        Intent intent = getIntent();
        //获取从哪里打开的登陆注册 (按钮颜色)
        loadCategory = intent.getIntExtra("loadCategory", Integer.parseInt(ConstJS_F.loadCategory));
        if (loadCategory == -1){
            loadCategory = ConstInfo.JINTAN;
        }
    }

    @Override
    protected View getContentView() {
        contentView = View.inflate(this, R.layout.load_activity, null);

        return contentView;
    }

    @Override
    protected void initView() {

        // 返回按钮和设置按钮初始化
        initTitleBtn();

        // 找到Framelayout 添加注册页
//        frameLayout = (FrameLayout) findViewById(R.id.load_regist_content_fl);

        // 第一次打开，加载登陆页
        loadFragment = new Load_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.load_regist_content_fl, loadFragment).commit();
    }

    /**
     * 返回按钮和设置按钮初始化
     */
    private void initTitleBtn() {
        //返回按钮
        ImageButton myBack = (ImageButton) findViewById(R.id.bt_back);
        //设置按钮
        ImageButton mySetting = (ImageButton) findViewById(R.id.bt_setting);
        //变背景 记录当前颜色
        switch (loadCategory) {
            case 1:
                currentColor = Const.PAGER1_COLOR;
                myBack.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                mySetting.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                break;
            case 2:
                currentColor = Const.PAGER2_COLOR;
                myBack.setBackgroundResource(R.drawable.icon_back_bg_red);
                mySetting.setBackgroundResource(R.drawable.icon_back_bg_red);
                break;
            case 3:
                currentColor = Const.PAGER3_COLOR;
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
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    //有注册页，先返回登陆页
                    backLoadPager();

                } else {
                    setExitSwichLayout();
                }

            }
        });
        //设置
        mySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SettingActivity.class));
            }
        });
    }

    //返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                //有注册页，先返回登陆页
                backLoadPager();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回登陆页的方法
     */
    public void backLoadPager(){
        //关闭注册页
        getSupportFragmentManager().popBackStack();
        try {
            loadFragment.setEnable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来注册后临时存取用户信息
     * @param userTemp
     */
    private UserTemp userTemp;
    public UserTemp getUserTemp() {
        return userTemp;
    }
    public void setUserTemp(UserTemp userTemp) {
        this.userTemp = userTemp;
    }

    //临时用户信息
    public static class UserTemp{
        public UserTemp(String email, String password, String nickname) {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
        }

        //邮箱 密码 用户名
        public String email;
        public String password;
        public String nickname;
    }


    /**
     * 获取当前页面主色
     * @return
     */
    private int currentColor;
    public int getCurrentColor(){
        return currentColor;
    }
    public int getLoadCategory(){
        return loadCategory;
    }
}
