package com.cmm.worldartapk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.activity.LoginActivity;
import com.cmm.worldartapk.base.UserInfo;
import com.cmm.worldartapk.publicinfo.ConstInfo;

/**
 * Created by Administrator on 2016/1/4.
 * 世纪坛特有的
 */
public class SJT_UI_Utils {

    /**
     * 弹出一个提示框
     * @param activity 当前提示框所在的Activity
     * @param info 提示信息
     */
    public static void showDialog(Activity activity, String info, boolean isSuccess, int loadCategory){
        View view = View.inflate(UIUtils.getContext(), R.layout.setting_clear_dialog, null);

        //图片 和 文本，先根据页面变更颜色，然后设置不同内容
        ImageView imageView = (ImageView) view.findViewById(R.id.dialog_img);
        TextView textView = (TextView) view.findViewById(R.id.dialog_info);

        //颜色
        switch (loadCategory){
            case ConstInfo.JINTAN:
                imageView.setImageResource(R.drawable.dialog_info_selector_yellow);
                textView.setTextColor(UIUtils.getColor(R.color.information_color));
                break;
            case ConstInfo.ZHANLAN:
                imageView.setImageResource(R.drawable.dialog_info_selector_red);
                textView.setTextColor(UIUtils.getColor(R.color.exhibition_color));
                break;
            case ConstInfo.YISHUGUAN:
                imageView.setImageResource(R.drawable.dialog_info_selector_blue);
                textView.setTextColor(UIUtils.getColor(R.color.gallery_color));
                break;
            default:
                break;
        }


        imageView.setSelected(isSuccess); // true 是成功
        textView.setText(info); //文本简述

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog clearDialog = builder.create();
        clearDialog.setView(view, 0, 0, 0, 0);

        clearDialog.show();
        //设置宽高
        clearDialog.getWindow().setLayout(UIUtils.dip2Px(230), ViewGroup.LayoutParams.WRAP_CONTENT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clearDialog.dismiss();
            }
        }, 1500L);
    }

    /**
     * 判断用户是否登陆
     * @return 返回是否有 SESSION_KEY
     */
    public static boolean userState(){
        if (TextUtils.isEmpty(UserInfo.getUserInfo().SESSION_KEY)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 判断用户登陆状态，如没登陆就跳转到登陆页
     */
    public static void isOpenLoadPage(Activity activity){
        if (!userState()){
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
    }

    /**
     * sp
     * @return
     */
    private static SharedPreferences sp;
    public static SharedPreferences getSharedPreferences() {
        if (sp == null){
            sp = UIUtils.getContext().getSharedPreferences("shijitan", Context.MODE_PRIVATE);
        }
        return sp;
    }
}
