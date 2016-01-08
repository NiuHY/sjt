package com.cmm.worldartapk.fragment;

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
import com.cmm.worldartapk.activity.LoadActivity;
import com.cmm.worldartapk.base.BaseFragment;
import com.cmm.worldartapk.base.UserInfo;
import com.cmm.worldartapk.bean.UserBean;
import com.cmm.worldartapk.bean.parser.UserInfoParser;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.net_2.MyNetWorkObject;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.net_volley_netroid.net_2.RequestMapData;
import com.cmm.worldartapk.utils.DrawableUtils;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2015/12/24.
 * 登陆页
 */
public class Load_Fragment extends BaseFragment {

    private static final String URL_SIGNIN = "api/signin/";

    private EditText mLoadEtEmail; //邮箱
    private EditText mLoadEtPwd; //密码
    private TextView mLoadBtnLoad; //登陆
    private CheckBox mLoadBtnUseYt; //忘记密码
    private TextView mLoadBtnRegist; //注册
    private ImageView mLoadIvWeibo; //新浪微博登陆
    private ImageView mLoadIvWechat; //微信登陆
    private View contentView;
    private int currentColor;
    private PAListener paListener;

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
        currentColor = ((LoadActivity)getActivity()).getCurrentColor();
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
                //登陆按钮变色
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
                    case R.id.load_btn_load: //登陆

                        //点击登陆按钮   判断邮箱格式，提交服务器邮箱和密码得到反馈判断是否登陆成功，成功保存对应信息
                        userLoad();

                        break;
                    case R.id.load_btn_regist: //注册
                        //点击注册，切换注册Fragment
                        //点击后设置当前页不可用
                        contentView.setVisibility(View.GONE);                      //.setCustomAnimations(R.anim.setg_next_in, R.anim.setg_next_out, R.anim.setg_pre_in, R.anim.setg_pre_out)
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.load_regist_content_fl, new RegistFragment()).addToBackStack(null).commit();
                        break;
                    case R.id.load_iv_weibo: //微博

                        //新浪微博登陆
                        Platform weibo = ShareSDK.getPlatform(getActivity(), SinaWeibo.NAME);
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

                        Platform wechat = ShareSDK.getPlatform(getActivity(), Wechat.NAME);
                        if (!wechat.isClientValid()){
                            UIUtils.showToastSafe("没有找到微信客户端，请检查");
                            return;
                        }
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

    /**
     * 用户登陆的方法
     */
    private void userLoad() {
        //抖动动画
        Animation anim = null;

        //获取邮箱
        String email = mLoadEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !email.matches("\\w{1,16}@\\w{1,7}.\\w{1,5}(.\\w{1,5})*")) {
            UIUtils.showToastSafe("检查邮箱格式");

            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtEmail.startAnimation(anim);
            return;
        }

        //获取密码
        String pwd = mLoadEtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            UIUtils.showToastSafe("密码不能为空");

            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtPwd.startAnimation(anim);
            return;
        }

        // 判断是否使用云图账号登陆，如果不使用，就用注册的账号登陆，如果使用更换请求url
        // 登陆url  http://h5.yuntoo.com/api/signin/
        String loadUrl = Const.BASE_URL;

        if (mLoadBtnUseYt.isChecked()){
            //选中 云图登陆
            loadUrl = "http://h5.yuntoo.com/api/signin/";
        }else{
            loadUrl = loadUrl + URL_SIGNIN;
        }


        //拿到邮箱和密码，请求登陆
        NetUtils.getDataByNet_POST(getActivity(), loadUrl, RequestMapData.params_load(email, pwd), new UserInfoParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                UserBean user = ((UserBean)data);
                boolean isSuccess = user.success.equals("1");

                if (isSuccess){
                    //保存用户登陆信息到 UserInfo 中
                    UserInfo.getUserInfo().USER_ID = user.data.user_id;
                    UserInfo.getUserInfo().SESSION_KEY = user.data.session_key;
                    UserInfo.getUserInfo().IS_BAND = user.data.is_band;

                    UIUtils.showToastSafe("登陆成功" + UserInfo.getUserInfo().USER_ID);

                    // 登陆成功就关闭登陆页
                    getActivity().finish();

                }else{
                    UIUtils.showToastSafe("登陆失败");
                }
            }

            @Override
            public void onError(String msg) {
                UIUtils.showToastSafe("登陆失败 ："+msg);
            }


        });
    }

    //第三方 新浪微信登陆 回调
    private class PAListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            UIUtils.showToastSafe("授权完成");

            //遍历hashMapa
            for(Map.Entry entry : hashMap.entrySet()){
                LogUtils.i("key == " + entry.getKey() + "  value == " + entry.getValue());
            }

//            //解析部分用户资料字段
//            String id,name,description,profile_image_url;
//            id=hashMap.get("id").toString();//ID
//            name=hashMap.get("name").toString();//用户名
//            description=hashMap.get("description").toString();//描述
//            profile_image_url=hashMap.get("profile_image_url").toString();//头像链接
//            String str="ID: "+id+";\n"+
//                    "用户名： "+name+";\n"+
//                    "描述："+description+";\n"+
//                    "用户头像地址："+profile_image_url;
//            System.out.println("用户资料: "+str);
//
//            System.out.println("============"+hashMap.toString());
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            UIUtils.showToastSafe("授权错误");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            UIUtils.showToastSafe("授权取消");
        }
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
        if (TextUtils.isEmpty(password)) {
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
        LoadActivity.UserTemp userTemp = ((LoadActivity)getActivity()).getUserTemp();
        if (null != userTemp){
            //有数据，回显邮箱和密码
            mLoadEtEmail.setText(userTemp.email);
            mLoadEtPwd.setText(userTemp.password);
        }
        contentView.setVisibility(View.VISIBLE);
    }
}