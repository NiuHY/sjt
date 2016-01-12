package com.cmm.worldartapk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;
import com.cmm.worldartapk.utils.share_package.OtherUtils;

import java.util.List;

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
    private String downloadUrl;

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


        //应用下载地址
        downloadUrl = "http://event.yuntoo.com/yishushiji";

        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OtherUtils.setShareData(SettingActivity.this, "邀请你参观「艺术世纪」的世界艺术馆", downloadUrl, "掌控未来美学", "https://mmbiz.qlogo.cn/mmbiz/KFj6wBflVBuZVeySJnBuPo9PPfPu2XkBgBw0ChZUVbZ5l9Y3VicKmdiaIqn6kwS35ojg3vibfNmFSkP5PMBNSYicUQ/0?wx_fmt=png");
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

                //判断是否登陆，如果登陆才退出
                if (SJT_UI_Utils.userState()){
                    //获得view对象
                    View view = View.inflate(SettingActivity.this, R.layout.setting_exit_dialog, null);

                    //创建对话框(设置边框都为0)，指定view对象
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
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
//                        LogUtils.e("退出登陆前" + UserInfo.getUserInfo().SESSION_KEY);
                            //清除用户信息
                            UserInfo.setUserInfo();//设置为null
//                        LogUtils.e("退出登陆后" + UserInfo.getUserInfo().SESSION_KEY);

                            //点击按钮后 关闭对话框
                            dialog.dismiss();

                            //遍历Activity集合，返回主页
                            List<Activity> allActivity = UIUtils.getActivityList();
                            for (int i = allActivity.size()-1; i >= 0; i--) {
                                Activity activity = allActivity.get(i);
                                if (!(activity instanceof MainActivity)){
                                    activity.finish();
                                }else {
                                    break;
                                }
                            }
                            //单一任务栈方式 待
//                        startActivity(new Intent(SettingActivity.this, MainActivity.class));

                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //取消  点击按钮后 关闭对话框
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else {
                    //还没登陆
                    SJT_UI_Utils.showDialog(SettingActivity.this, "还没登陆", false);
                }


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
                        jtb.setText(downloadUrl);
                        SJT_UI_Utils.showDialog(SettingActivity.this, "复制成功", true);
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
