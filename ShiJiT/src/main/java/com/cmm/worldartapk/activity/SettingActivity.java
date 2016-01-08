package com.cmm.worldartapk.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.base.UserInfo;
import com.cmm.worldartapk.utils.DataCleanManager;
import com.cmm.worldartapk.utils.share_package.OtherUtils;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2015/12/18.
 */
public class SettingActivity extends BaseActivity {

    private View contentView;

    //分享添加的窗体
    private WindowManager windowManager;
    private View dfWindowView;
    private boolean isWindowViewShow;

    @Override
    protected void init() {

    }

    @Override
    protected View getContentView() {
        contentView = View.inflate(SettingActivity.this, R.layout.settging_activity, null);
        return contentView;
    }

    @Override
    protected void initView() {
        //返回按钮
        ImageButton myBack = (ImageButton) findViewById(R.id.bt_back);
        //关闭
        myBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                setExitSwichLayout();
//                overridePendingTransition(R.anim.setg_pre_in, R.anim.setg_pre_out);
            }
        });

        //三个点击
        View shareView = findViewById(R.id.setting_share);
        View trashView = findViewById(R.id.setting_trash);
        View logoutView = findViewById(R.id.setting_logout);

        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtils.setShareData(SettingActivity.this, "应用名称", "应用地址", "应用信息", "http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                showShareWindow();
            }
        });

        trashView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //清空应用缓存
                DataCleanManager.clearAllCache(UIUtils.getContext());

                SJT_UI_Utils.showDialog(SettingActivity.this, "清理成功", true);

            }
        });

        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获得view对象
                View view = View.inflate(SettingActivity.this, R.layout.setting_exit_dialog, null);

                //创建对话框(设置边框都为0)，指定view对象
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                final AlertDialog dialog = builder.create();
                dialog.setView(view, 0, 0, 0, 0);


                //初始化 控件
                TextView exit = (TextView) view.findViewById(R.id.setting_bt_exit);
                TextView cancel = (TextView) view.findViewById(R.id.setting_bt_cancel);

                //设置点击事件
                exit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //确认退出
                        //清空 UserInfo类中保存的信息 清空sp中保存的 userInfo键
                        UserInfo.setUserInfo();//设置为null

//                        SJT_UI_Utils.getSharedPreferences().

                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                        //点击按钮后 关闭对话框
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消  点击按钮后 关闭对话框
                        dialog.dismiss();
                    }
                });

//                //默认
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
//                builder.setMessage("确认退出吗？");
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //确认退出
//                        UIUtils.showToastSafe("退出登陆");
//                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
//                    }
//                });
//                builder.setNegativeButton("取消", null);
//
//                AlertDialog dialog = builder.create();
//
//                Window dialogWindow = dialog.getWindow();
//
//                // 获取dialog中的view 设置字体颜色
////                View dialogView = dialogWindow.getDecorView();
////                setViewFontColor(dialogView, 0xffff0000);
//
//                //显示在下边
//                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                lp.y = UIUtils.dip2Px(150); // 新位置Y坐标
//
//
//// dialog.onWindowAttributesChanged(lp);
////(当Window的Attributes改变时系统会调用此函数)
//                dialogWindow .setAttributes(lp);
////                dialogWindow.setGravity(Gravity.BOTTOM);
//                dialogWindow.setBackgroundDrawable(new ColorDrawable(0xaa666666));

                dialog.show();
            }
        });
    }


    /**
     * 遍历dialog中的View 设置其中TextView的字体颜色
     *
     * @param view  dialog根布局
     * @param color 字体颜色
     */
    private void setViewFontColor(View view, int color) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                setViewFontColor(parent.getChildAt(i), color);
            }
        } else if (view instanceof TextView) {
            TextView textview = (TextView) view;
            textview.setTextColor(color);
        }
    }


    //添加一个窗体来  分享
    private void showShareWindow() {
        windowManager = getWindowManager();

        dfWindowView = View.inflate(this, R.layout.sharepage_window, null);
        //设置布局，初始化6个分享按钮
        initShareButton();

        // 设置布局参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //宽高填充
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //全屏 要有WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，好像因为这个才能相应返回键？
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //透明窗体
        params.format = PixelFormat.TRANSPARENT;
        //普通应用程序窗口
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;

        //添加
        windowManager.addView(dfWindowView, params);
        //设置打开标记
        isWindowViewShow = true;
    }

    // 初始化分享按钮
    private void initShareButton() {

        //六个分享按钮
        ImageButton share_qq = (ImageButton) dfWindowView.findViewById(R.id.share_btn_qq); //qq
        ImageButton share_weibo = (ImageButton) dfWindowView.findViewById(R.id.share_btn_weibo);//微博
        ImageButton share_wenxin = (ImageButton) dfWindowView.findViewById(R.id.share_btn_wenxin);//微信
        ImageButton share_friend = (ImageButton) dfWindowView.findViewById(R.id.share_btn_friend);//朋友圈

        ImageButton share_copyLink = (ImageButton) dfWindowView.findViewById(R.id.share_btn_copyLink);//剪贴板
        ImageButton share_collect = (ImageButton) dfWindowView.findViewById(R.id.share_btn_collect);//收藏
        //这里不需要收藏按钮  隐藏
        share_collect.setVisibility(View.GONE);

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(150L);

        share_qq.startAnimation(scaleAnimation);
        share_weibo.startAnimation(scaleAnimation);
        share_wenxin.startAnimation(scaleAnimation);
        share_friend.startAnimation(scaleAnimation);
        share_copyLink.startAnimation(scaleAnimation);

        //设置点击监听
        shareClick(share_qq);
        shareClick(share_weibo);
        shareClick(share_wenxin);
        shareClick(share_friend);
        shareClick(share_copyLink);


        // 给窗体布局设置点击关闭
        dfWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWindowViewShow) {
                    windowManager.removeView(dfWindowView);
                    isWindowViewShow = false;
                }
            }
        });

    }

    // 六个分享按钮的点击监听
    private void shareClick(ImageButton ib) {
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    //四个分享
                    case R.id.share_btn_qq:
                        //点击调用QQ分享
                        OtherUtils.shareMethod(QQ.NAME);
                        break;
                    case R.id.share_btn_weibo:
                        //新浪微博
                        OtherUtils.shareMethod(SinaWeibo.NAME);
                        break;
                    case R.id.share_btn_wenxin:
                        //微信好友
                        OtherUtils.shareMethod(Wechat.NAME);
                        break;
                    case R.id.share_btn_friend:
                        //微信朋友圈
                        OtherUtils.shareMethod(WechatMoments.NAME);
                        break;

                    case R.id.share_btn_copyLink:
                        //获取剪贴板管理器，复制到剪贴板
                        ClipboardManager jtb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        //TODO
//                        ClipData clipData = new ClipData();
//                        jtb.setPrimaryClip(clipData);
                        jtb.setText("应用下载地址");
                        UIUtils.showToastSafe(jtb.getText().toString().trim());
                        break;
                    default:
                        break;
                }

                //点击后关闭分享
                if (isWindowViewShow) {
                    windowManager.removeView(dfWindowView);
                    isWindowViewShow = false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isWindowViewShow) {
            windowManager.removeView(dfWindowView);
            isWindowViewShow = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isWindowViewShow) {
            windowManager.removeView(dfWindowView);
            isWindowViewShow = false;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
