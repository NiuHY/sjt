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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.activity.LoginActivity;
import com.cmm.worldartapk.base.BaseFragment;
import com.cmm.worldartapk.bean.UserBean;
import com.cmm.worldartapk.bean.parser.UserInfoParser;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.net_2.MyNetWorkObject;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.net_volley_netroid.net_2.RequestMapData;
import com.cmm.worldartapk.utils.DrawableUtils;
import com.cmm.worldartapk.utils.UIUtils;

/**
 * Created by Administrator on 2015/12/29.
 */
public class RegistFragment extends BaseFragment {

    private static final String URL_SIGNUP = "api/signup/";

    private EditText mLoadEtEmail; //邮箱
    private EditText mLoadEtPwd; //密码
    private EditText mLoadEtPwd_2; //确认密码
    private EditText mLoadEtNickname; //昵称
    private TextView mLoadBtnRegist; //注册
    private CheckBox mRigestRadiobutton; //单选按钮

    @Override
    protected View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.load_fragment_regist, null);


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
        mLoadEtPwd_2 = (EditText) contentView.findViewById(R.id.load_et_pwd_2);
        mLoadEtNickname = (EditText) contentView.findViewById(R.id.load_et_nickname);
        mLoadBtnRegist = (TextView) contentView.findViewById(R.id.load_btn_regist);
        mRigestRadiobutton = (CheckBox) contentView.findViewById(R.id.rigest_radiobutton);
    }

    /**
     * 交互
     *
     * @param savedInstanceState
     */
    @Override
    protected void initViewData(Bundle savedInstanceState) {
        //设置点击监听
        setViewClickListener(mLoadBtnRegist);

        //给输入框设置变更监听
        setTextChangeL(mLoadEtEmail);
        setTextChangeL(mLoadEtPwd);
        setTextChangeL(mLoadEtPwd_2);
        setTextChangeL(mLoadEtNickname);


        //给选择框设置变更监听
        mRigestRadiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //每次变更状态，就去调用变色
                changeButtonBG();
            }
        });
    }


    //文本输入框标记
    private boolean et_email = false;
    private boolean et_pwd = false;
    private boolean et_nickname = false;
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
                    if (checkPassword(mLoadEtPwd, mLoadEtPwd_2)){
                        et_pwd = true;
                    }else{
                        et_pwd = false;
                    }
                } else if (editText.getId() == R.id.load_et_pwd_2) {
                    //密码
                    if (checkPassword(mLoadEtPwd, mLoadEtPwd_2)){
                        et_pwd = true;
                    }else{
                        et_pwd = false;
                    }
                } else if (editText.getId() == R.id.load_et_nickname) {
                    //昵称
                    if (!TextUtils.isEmpty(editText.getText().toString().trim())){
                        et_nickname = true;
                    }else{
                        et_nickname = false;
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
        if (et_email && et_pwd && et_nickname){
            mLoadBtnRegist.setBackground(DrawableUtils.createSelector(new ColorDrawable(((LoginActivity) getActivity()).getCurrentColor()), new ColorDrawable(0x55787878)));
        }else {
            mLoadBtnRegist.setBackgroundColor(0xffbbbbbb);
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
                    case R.id.load_btn_regist: //注册

                        //点击注册按钮  判断邮箱 密码 昵称 和 是否 同意注册信息
                        userRegist();

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 用户注册的方法
     */
    private void userRegist() {
        //抖动动画
        Animation anim = null;

        //获取邮箱
        final String email = mLoadEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !email.matches("\\w{1,16}@\\w{1,7}.\\w{1,5}(.\\w{1,5})*")) {
            UIUtils.showToastSafe("邮箱有误");

            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtEmail.startAnimation(anim);
            return;
        }

        //获取密码
        final String pwd = mLoadEtPwd.getText().toString().trim();
        if ( ! checkPassword(mLoadEtPwd, mLoadEtPwd_2)) {
            UIUtils.showToastSafe("密码输入错误");

            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtPwd.startAnimation(anim);
            mLoadEtPwd_2.startAnimation(anim);
            return;
        }

        //获取昵称
        String nickname = mLoadEtNickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)){
            UIUtils.showToastSafe("昵称为空");
            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mLoadEtNickname.startAnimation(anim);
            return;
        }

        //是否选中RadioButton
        if (!mRigestRadiobutton.isChecked()){
            UIUtils.showToastSafe("不同意");
            if (anim == null) {
                anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.shake_anim);
            }
            mRigestRadiobutton.startAnimation(anim);
            return;
        }

        // 登陆url
        String registUrl = Const.BASE_URL + URL_SIGNUP;

        //拿到邮箱 密码 昵称，请求注册
        NetUtils.getDataByNet_POST(getActivity(), registUrl, RequestMapData.params_regist(email, pwd, nickname), new UserInfoParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                //判断请求结果是否成功，如果成功就关闭这个页面，回显数据到登陆页面
                UserBean userTemp = ((UserBean)data);
                boolean isSuccess = userTemp.success.equals("1");

                if (isSuccess){
                    UIUtils.showToastSafe("注册成功");
                    //保存用户数据到Activity中
                    ((LoginActivity)getActivity()).setUserTemp(new LoginActivity.UserTemp(email, pwd, ""));
                    // 返回登陆页
                    ((LoginActivity)getActivity()).backLoadPager();
                }else{
                    UIUtils.showToastSafe("注册失败：" + userTemp.error_message);
                }
            }

            @Override
            public void onError(String msg) {
                UIUtils.showToastSafe("注册失败 ：" + msg);
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
    private boolean checkPassword(EditText editText, EditText editText2) {

        String password = editText.getText().toString().trim();
        String password2 = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2) || !TextUtils.equals(password, password2)) {
            return false;//错误
        } else {
            return true;
        }
    }
}
