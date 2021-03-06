package com.cmm.worldartapk.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.activity.LoginActivity;
import com.cmm.worldartapk.activity.UserActivity;
import com.cmm.worldartapk.base.BaseApplication;
import com.cmm.worldartapk.base.BaseFragment;
import com.cmm.worldartapk.bean.UserBean;
import com.cmm.worldartapk.bean.parser.UserInfoParser;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.net_2.MyNetWorkObject;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.net_volley_netroid.net_2.RequestMapData;
import com.cmm.worldartapk.utils.DrawableUtils;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2015/12/24.
 * 登录页
 */
public class Load_Fragment extends BaseFragment {

    private static final String URL_SIGNIN = "api/signin/";
    private static final String URL_OTHERLOGIN = "api/other_login/";

    private EditText mLoadEtEmail; //邮箱
    private EditText mLoadEtPwd; //密码
    private TextView mLoadBtnLoad; //登录
    private CheckBox mLoadBtnUseYt; //忘记密码
    private TextView mLoadBtnRegist; //注册
    private ImageView mLoadIvWeibo; //新浪微博登录
    private ImageView mLoadIvWechat; //微信登录
    private View contentView;
    private int currentColor;
    private PAListener paListener;
    private String yuntoo_user_intro;

    @Override
    protected View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.load_fragment_load, null);


        initView(contentView);


        return contentView;
    }

    /**
     * 初始化布局
     *
     * @param contentView
     */
    private void initView(View contentView) {
        mLoadEtEmail = (EditText) contentView.findViewById(R.id.load_et_email);
        mLoadEtPwd = (EditText) contentView.findViewById(R.id.load_et_pwd);
        mLoadBtnLoad = (TextView) contentView.findViewById(R.id.load_btn_load);
        mLoadBtnUseYt = (CheckBox) contentView.findViewById(R.id.load_btn_ytload);
        mLoadBtnRegist = (TextView) contentView.findViewById(R.id.load_btn_regist);
        mLoadIvWeibo = (ImageView) contentView.findViewById(R.id.load_iv_weibo);
        mLoadIvWechat = (ImageView) contentView.findViewById(R.id.load_iv_wechat);

        //根据进来的页面变更 mLoadBtnUseYt  mLoadBtnRegist 两个按钮的颜色
        //把文本颜色设置成当前颜色
        currentColor = ((LoginActivity)getActivity()).getCurrentColor();
        mLoadBtnUseYt.setTextColor(currentColor);
        mLoadBtnRegist.setTextColor(currentColor);

        switch (currentColor){
            case Const.PAGER1_COLOR:
                mLoadBtnUseYt.setButtonDrawable(R.drawable.checkbox_selector_1_load);
                break;
            case Const.PAGER2_COLOR:
                mLoadBtnUseYt.setButtonDrawable(R.drawable.checkbox_selector_2_load);
                break;
            case Const.PAGER3_COLOR:
                mLoadBtnUseYt.setButtonDrawable(R.drawable.checkbox_selector_3_load);
                break;
            default:
                break;
        }
    }

    /**
     * 交互
     *
     * @param savedInstanceState
     */
    @Override
    protected void initViewData(Bundle savedInstanceState) {
        //设置点击监听
        setViewClickListener(mLoadBtnLoad);
        setViewClickListener(mLoadBtnRegist);
        setViewClickListener(mLoadIvWeibo);
        setViewClickListener(mLoadIvWechat);

        //给两个输入框设置变更监听
        setTextChangeL(mLoadEtEmail);
        setTextChangeL(mLoadEtPwd);
    }


    //文本输入框标记
    private boolean et_email = false;
    private boolean et_pwd = false;
    /**
     * 给文本输入框设置文本变更监听
     *
     * @param editText
     */
    private void setTextChangeL(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getId() == R.id.load_et_email) {
                    //邮箱
                    if (checkEmail(mLoadEtEmail)){
                        et_email = true;
                    }else{
                        et_email = false;
                    }
                } else if (editText.getId() == R.id.load_et_pwd) {
                    //密码
                    if (checkPassword(mLoadEtPwd)){
                        et_pwd = true;
                    }else{
                        et_pwd = false;
                    }
                }
                //登录按钮变色
                changeButtonBG();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void changeButtonBG(){
        //如果两个标记都正确才设置
        if (et_email && et_pwd){
            mLoadBtnLoad.setBackground(DrawableUtils.createSelector(new ColorDrawable(currentColor), new ColorDrawable(0x55787878)));
        }else {
            mLoadBtnLoad.setBackgroundColor(0xffbbbbbb);
        }
    }


    /**
     * 点击方法
     *
     * @param view 要设置点击监听的View
     */
    private void setViewClickListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                switch (v.getId()) {
                    case R.id.load_btn_load: //登录

                        //点击登录按钮   判断邮箱格式，提交服务器邮箱和密码得到反馈判断是否登录成功，成功保存对应信息
                        userLoad();

                        break;
                    case R.id.load_btn_regist: //注册
                        //点击注册，切换注册Fragment
                        //点击后设置当前页不可用
                        contentView.setVisibility(View.GONE);                      //.setCustomAnimations(R.anim.setg_next_in, R.anim.setg_next_out, R.anim.setg_pre_in, R.anim.setg_pre_out)
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.load_regist_content_fl, new RegistFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.load_iv_weibo: //微博
                        UIUtils.showToastSafe("正在打开请求授权信息页面，请稍后...");
                        //新浪微博登录
                        Platform weibo = ShareSDK.getPlatform(getActivity(), SinaWeibo.NAME);

                        if (!weibo.isClientValid()){
                            UIUtils.showToastSafe("没有找到新浪微博客户端，请检查");
                            return;
                        }

                        //暂时不可点击 在遮挡后才可以再次点击
                        v.setEnabled(false);

                        //如果授权就移除授权信息
                        if (weibo.isValid()){
                            weibo.removeAccount();
                        }
                        weibo.showUser(null);
                        if (paListener == null){
                            paListener = new PAListener();
                        }
                        weibo.setPlatformActionListener(paListener);
                        weibo.authorize();

                        break;
                    case R.id.load_iv_wechat: //微信
                        UIUtils.showToastSafe("正在打开请求授权信息页面，请稍后...");
                        Platform wechat = ShareSDK.getPlatform(getActivity(), Wechat.NAME);
                        if (!wechat.isClientValid()){
                            UIUtils.showToastSafe("没有找到微信客户端，请检查");
                            return;
                        }

                        //暂时不可点击 在遮挡后才可以再次点击
                        v.setEnabled(false);

                        if (wechat.isValid()){
                            wechat.removeAccount();
                        }
                        wechat.showUser(null);
                        if (paListener == null){
                            paListener = new PAListener();
                        }
                        wechat.setPlatformActionListener(paListener);
                        wechat.authorize();

                        break;
                    default:
                        break;
                }
            }
        });
    }



    @Override
    public void onPause() {
        super.onPause();

        if (mLoadIvWechat != null){
            mLoadIvWechat.setEnabled(true);
        }
        if (mLoadIvWeibo != null){
            mLoadIvWeibo.setEnabled(true);
        }
    }

    /**
     * 用户登录的方法
     */
    private void userLoad() {
        //抖动动画
        Animation anim = null;

        //获取邮箱
        String email = mLoadEtEmail.getText().toString().trim();
        if (!checkEmail(mLoadEtEmail)) {
            UIUtils.showToastSafe("检查邮箱格式");

            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtEmail.startAnimation(anim);
            return;
        }

        //获取密码
        final String pwd = mLoadEtPwd.getText().toString().trim();
        if (!checkPassword(mLoadEtPwd)) {
            UIUtils.showToastSafe("密码至少6位");

            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtPwd.startAnimation(anim);
            return;
        }

        // 判断是否使用云图账号登录，如果不使用，就用注册的账号登录，如果使用更换请求url
        // 登录url  http://h5.yuntoo.com/api/signin/
        String loginUrl = Const.BASE_URL;

        if (mLoadBtnUseYt.isChecked()){
            //选中 云图登录
            loginUrl = "http://h5.yuntoo.com/api/signin/";
        }else{
            loginUrl = loginUrl + URL_SIGNIN;
        }


        //拿到邮箱和密码，请求登录
        NetUtils.getDataByNet_POST(getActivity(), loginUrl, RequestMapData.params_load(email, pwd), new UserInfoParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                UserBean user = ((UserBean)data);
                boolean isSuccess = user.success.equals("1");

                if (isSuccess){

                    //如果用云图账号，就继续请求第三方登录
                    if (mLoadBtnUseYt.isChecked()){
                        //得到信息
//                        // 云图用户专有的 介绍什么的
//                        UserInfo.getUserInfo().USER_INTRO = user.data.user_intro; // 用户介绍
//                        UserInfo.getUserInfo().NICKNAME = user.data.user_nickname; // 昵称
//                        UserInfo.getUserInfo().AVATAR = user.data.user_avatar; // 用户头像
//                        UserInfo.getUserInfo().EMAIL = user.data.user_email; // 用户邮箱
//                        UserInfo.getUserInfo().IS_PRO = user.data.is_pro; // ???

                        //第三方请求需要的参数
                        HashMap<String, String> otherLoginParams = new HashMap<>();

                        otherLoginParams.put("email", user.data.user_email); // 邮箱
                        otherLoginParams.put("password", pwd); // 密码
                        otherLoginParams.put("nickname", user.data.user_nickname); // 昵称
                        otherLoginParams.put("accesstoken", ""); // 令牌
                        otherLoginParams.put("expires_at", ""); // 有效期
                        otherLoginParams.put("avatar", user.data.user_avatar); // 用户头像
                        otherLoginParams.put("platformname", "yuntoo"); // 平台  wxsession、sina、yuntoo
                        otherLoginParams.put("profileURL", user.data.is_pro); // 简介？
                        otherLoginParams.put("user_name", user.data.user_email); // 用户名
                        otherLoginParams.put("user_id", user.data.user_id); // 用户id
                        otherLoginParams.put("user_intro", user.data.user_intro); // 用户介绍

                        //用户介绍保存一份，用来在登录成功时记录
                        yuntoo_user_intro = user.data.user_intro;


                        //请求第三方登录接口
                        load_3_Request(otherLoginParams);
                    }else {
                        //不是云图账号，登录
//                        //先清空数据
//                        UserInfo.setUserInfo();
//
//                        //保存用户登录信息到 UserInfo 中
//                        UserInfo.getUserInfo().USER_ID = user.data.user_id;
//                        UserInfo.getUserInfo().SESSION_KEY = user.data.session_key;
//                        //保存用户信息到sp
//                        String uif = CodeUtils.encodeByBase64(new Gson().toJson(UserInfo.getUserInfo()));
//                        SJT_UI_Utils.getSharedPreferences().edit().putString("uif", uif).apply();

                        //保存用户信息
                        BaseApplication.setUserInfo();
                        BaseApplication.getUserInfo().USER_ID = user.data.user_id;
                        BaseApplication.getUserInfo().SESSION_KEY = user.data.session_key;

                        SJT_UI_Utils.showDialog(getActivity(), "登录成功", true, ((LoginActivity) getActivity()).getLoadCategory());

                        //判断是否是要进入个人中心，如果进入就在这里打开个人中心
                        Intent intent = getActivity().getIntent();
                        // 登录成功就关闭登录页
                        getActivity().finish();
                        //同时打开个人中心
                        if (intent != null && intent.getBooleanExtra("userActivity", false)){
                            int loadCategory = intent.getIntExtra("loadCategory", -1);
                            Intent userIntent = new Intent(getActivity(), UserActivity.class);
                            userIntent.putExtra("loadCategory", loadCategory);
                            //给JS保存
                            ConstJS_F.loadCategory = loadCategory + "";
                            startActivity(userIntent);
                        }
                    }

                }else{
                    SJT_UI_Utils.showDialog(getActivity(), "登录失败", false, ((LoginActivity) getActivity()).getLoadCategory());
//                    UIUtils.showToastSafe(user.error_message);
                }
            }

            @Override
            public void onError(String msg) {
                SJT_UI_Utils.showDialog(getActivity(), "登录失败", false, ((LoginActivity) getActivity()).getLoadCategory());
                UIUtils.showToastSafe("连接异常");
            }
        });
    }

    //第三方 新浪微信登录 回调
    private class PAListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

//            platform.removeAccount(true);

            UIUtils.showToastSafe("授权完成");

            //第三方请求需要的参数
            HashMap<String, String> otherLoginParams = new HashMap<>();

            otherLoginParams.put("email", ""); // 邮箱
            otherLoginParams.put("password", ""); // 密码
            otherLoginParams.put("nickname", ""); // 昵称
            otherLoginParams.put("accesstoken", ""); // 令牌
            otherLoginParams.put("expires_at", ""); // 有效期
            otherLoginParams.put("avatar", ""); // 用户头像
            otherLoginParams.put("platformname", ""); // 平台  wxsession、sina、yuntoo
            otherLoginParams.put("profileURL", ""); // 简介？
            otherLoginParams.put("user_name", ""); // 用户名
            otherLoginParams.put("user_id", ""); // 用户id
            otherLoginParams.put("user_intro", ""); // 用户介绍


            //遍历查看hashMapa
//            for(Map.Entry entry : hashMap.entrySet()){
//                LogUtils.i("  " + platform.getName() + "    key == " + entry.getKey() + "  value == " + entry.getValue());
//            }

               switch (platform.getName()){
                    case "SinaWeibo": // 新浪微博

                        otherLoginParams.put("nickname", hashMap.get("name").toString());
                        otherLoginParams.put("avatar", hashMap.get("avatar_large").toString());
                        otherLoginParams.put("platformname", "sina");
                        otherLoginParams.put("profileURL", hashMap.get("profile_url").toString());
                        otherLoginParams.put("user_name", hashMap.get("name").toString());
                        otherLoginParams.put("user_id", hashMap.get("id").toString());

                        break;
                    case "Wechat": //微信

                        otherLoginParams.put("nickname", hashMap.get("nickname").toString());
                        otherLoginParams.put("avatar", hashMap.get("headimgurl").toString());
                        otherLoginParams.put("platformname", "wxsession");
                        otherLoginParams.put("user_name", hashMap.get("nickname").toString());
                        otherLoginParams.put("user_id", hashMap.get("unionid").toString());

                        break;
                    default:
                        break;
                }


            //得到对应参数，第三方登录请求
            load_3_Request(otherLoginParams);

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            platform.removeAccount(true);
            LogUtils.e("错误", throwable);
            UIUtils.showToastSafe("授权错误");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            platform.removeAccount(true);
            UIUtils.showToastSafe("授权取消");
        }
    }

    /**
     * 第三方登录请求，需要信息参数
     * @param params  请求参数
     */
    private void load_3_Request(final HashMap<String, String> params) {

        String requestUrl = Const.BASE_URL + URL_OTHERLOGIN;

        NetUtils.getDataByNet_POST(getActivity(), requestUrl, RequestMapData.params_otherLogin(params), new UserInfoParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {

                UserBean user = ((UserBean)data);
                boolean isSuccess = user.success.equals("1");

                if (isSuccess){
//                    //先清空数据
//                    UserInfo.setUserInfo();
//
//                    //保存用户登录信息到 UserInfo 中
//                    UserInfo.getUserInfo().USER_ID = user.data.user_id;
//                    UserInfo.getUserInfo().SESSION_KEY = user.data.session_key;
//                    UserInfo.getUserInfo().IS_BAND = user.data.is_band;
//                    UserInfo.getUserInfo().AVATAR = params.get("avatar");
//
//
//                    //判断是否是云图登录，如果是就保存其用户简介
//                    if (mLoadBtnUseYt.isChecked() && !TextUtils.isEmpty(yuntoo_user_intro)){
//                        UserInfo.getUserInfo().USER_INTRO = yuntoo_user_intro;
//                    }
//                    //保存用户信息到sp
//                    String uif = CodeUtils.encodeByBase64(new Gson().toJson(UserInfo.getUserInfo()));
//                    SJT_UI_Utils.getSharedPreferences().edit().putString("uif", uif).apply();


                    //保存用户信息
                    BaseApplication.setUserInfo();

                    BaseApplication.getUserInfo().USER_ID = user.data.user_id;
                    BaseApplication.getUserInfo().SESSION_KEY = user.data.session_key;
                    BaseApplication.getUserInfo().IS_BAND = user.data.is_band;
                    BaseApplication.getUserInfo().AVATAR = params.get("avatar");


                    //判断是否是云图登录，如果是就保存其用户简介
                    if (mLoadBtnUseYt.isChecked() && !TextUtils.isEmpty(yuntoo_user_intro)){
                        BaseApplication.getUserInfo().USER_INTRO = yuntoo_user_intro;
                    }

                    SJT_UI_Utils.showDialog(getActivity(), "登录成功", true, ((LoginActivity) getActivity()).getLoadCategory());


                    //判断是否是要进入个人中心，如果进入就在这里打开个人中心
                    Intent intent = getActivity().getIntent();
                    // 登录成功就关闭登录页
                    getActivity().finish();
                    //同时打开个人中心
                    if (intent != null && intent.getBooleanExtra("userActivity", false)){
                        int loadCategory = intent.getIntExtra("loadCategory", -1);
                        Intent userIntent = new Intent(getActivity(), UserActivity.class);
                        userIntent.putExtra("loadCategory", loadCategory);
                        //给JS保存
                        ConstJS_F.loadCategory = loadCategory + "";
                        startActivity(userIntent);
                    }


                }else {
                    SJT_UI_Utils.showDialog(getActivity(), "登录失败", false, ((LoginActivity) getActivity()).getLoadCategory());
                    UIUtils.showToastSafe(user.error_message);
                }
            }

            @Override
            public void onError(String msg) {
                SJT_UI_Utils.showDialog(getActivity(), "登录失败", false, ((LoginActivity) getActivity()).getLoadCategory());
                LogUtils.e("第三方登录失败");
            }
        });
    }

    /**
     * 校验邮箱
     * @param editText
     * @return
     */
    private boolean checkEmail(EditText editText) {

        String email = editText.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !email.matches("\\w{1,16}@\\w{1,7}.\\w{1,5}(.\\w{1,5})*")) {
            return false;//错误
        } else {
            return true;
        }
    }

    /**
     * 校验密码
     * @param editText
     * @return
     */
    private boolean checkPassword(EditText editText) {

        String password = editText.getText().toString().trim();
        if (TextUtils.isEmpty(password) || !password.matches("\\w{6,16}")) {
            return false;//错误
        } else {
            return true;
        }
    }

    /**
     * 退出注册页 调用
     */
    public void setEnable(){
        //在从注册页过来时判断是否有用户信息，如果有就回显
        LoginActivity.UserTemp userTemp = ((LoginActivity)getActivity()).getUserTemp();
        if (null != userTemp){
            //有数据，回显邮箱和密码
            mLoadEtEmail.setText(userTemp.email);
            mLoadEtPwd.setText(userTemp.password);
            //取消勾选艺术云图
            mLoadBtnUseYt.setChecked(false);
        }
        contentView.setVisibility(View.VISIBLE);
    }
}