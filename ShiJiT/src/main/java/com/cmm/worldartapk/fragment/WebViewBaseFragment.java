package com.cmm.worldartapk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.activity.LoginActivity;
import com.cmm.worldartapk.activity.SearchActivity;
import com.cmm.worldartapk.activity.UserActivity;
import com.cmm.worldartapk.base.BaseFragment;
import com.cmm.worldartapk.ui.PullRefreshUtils;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * Created by Administrator on 2015/12/8.
 */
public abstract class WebViewBaseFragment extends BaseFragment {

    //加载完毕的监听
    public interface OnFinishListener{
        public void onFinish();
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    private OnFinishListener onFinishListener;

    //被缓存的View对象
    /**
     * 每个页面对应的WebView
     */
    private WebView webView;
    private PullToRefreshWebView mPullRefreshWebView;
    private ImageView btn_more;
    private ImageView btn_search;
    private ImageView btn_user;

    /**
     * 是否是第一次加载
     */
    private boolean isFristLoad = true;
    private View btn_group_view;
    private TextView title_text;
    private View title_group;

    /**
     * 给子类重写，需要返回这个Fragment的内容布局
     * @return Fragment的内容布局
     */
//    protected abstract View initFragmentView();

    //初始化WebView
    @Override
    protected View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View contentView = inflater.inflate(R.layout.home_fragment_layout, null);
        mPullRefreshWebView = (PullToRefreshWebView) contentView.findViewById(R.id.pull_refresh_webview);
        // 初始化WebView 传入其父控件（内容布局）
        webView = PullRefreshUtils.setListener_PRWebView(mPullRefreshWebView);

//        LogUtils.e(webView.toString());

        //通过子类重写，加载对应的url
        if (isFristLoad){
            setUrl(webView);
//            LogUtils.e("load-------------");
//            isFristLoad = false;
        }

        // 给WebView之上添加的标题栏，默认隐藏，之后注册给MainActivity
        title_group = View.inflate(UIUtils.getContext(), R.layout.title_layout, null);
        //标题默认隐藏，注册给MainActivity
        title_group.setVisibility(View.GONE);


        // 给WebView 之上添加的按钮
        btn_group_view = View.inflate(UIUtils.getContext(), R.layout.home_btngroup_layout, null);
        //按钮周围不响应触摸事件
        btn_group_view.findViewById(R.id.button_pack).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //初始化标题和按钮
        initTitle(getPagerIndex());

        //添加标题(首先)和按钮
        ((ViewGroup)webView.getParent()).addView(title_group);
        ((ViewGroup)webView.getParent()).addView(btn_group_view);

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //判断如果按钮是显示状态，就隐藏
                if (isShow_btn){
                    hint_btn();
                }
                return false;
            }
        });

        //数据初始化完成
        if (onFinishListener != null){
            onFinishListener.onFinish();
        }

        return contentView;
    }



    protected abstract int getPagerIndex();

    /**
     * 左上角按钮的状态
     */
    private boolean isShow_btn = false;

    /**
     * 初始化标题
     * @param pagetIndex 页面索引
     */
    private void initTitle(int pagetIndex) {

        //标题背景
        final View title_view = title_group.findViewById(R.id.title_view);
        title_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //标题文本
        title_text = (TextView) title_group.findViewById(R.id.title_text);
        title_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击后调用JS代码 返回顶部
                if (webView != null) {
                    webView.loadUrl("javascript:goTop()");
                }
            }
        });

        //查找三个按钮
        btn_more = (ImageView) btn_group_view.findViewById(R.id.home_btn_more);
        btn_more.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭这个按钮的硬件加速

        btn_search = (ImageView) btn_group_view.findViewById(R.id.home_btn_search);
        btn_user = (ImageView) btn_group_view.findViewById(R.id.home_btn_user);

        //更改背景颜色
        switch (pagetIndex){
            case 1:
                title_view.setBackgroundColor(0xbdffad42);
                title_text.setText("中华世纪坛");

                btn_more.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                btn_search.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                btn_user.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                break;
            case 2:
                title_view.setBackgroundColor(0xbdff2942);
                title_text.setText("世界艺展");

                btn_more.setBackgroundResource(R.drawable.icon_back_bg_red);
                btn_search.setBackgroundResource(R.drawable.icon_back_bg_red);
                btn_user.setBackgroundResource(R.drawable.icon_back_bg_red);
                break;
            case 3:
                title_view.setBackgroundColor(0xbd3ebdff);
                title_text.setText("全球艺馆");

                btn_more.setBackgroundResource(R.drawable.icon_back_bg_blue);
                btn_search.setBackgroundResource(R.drawable.icon_back_bg_blue);
                btn_user.setBackgroundResource(R.drawable.icon_back_bg_blue);
                break;
            default:
                break;
        }

        // 初始化按钮状态
        isShow_btn = btn_search.getVisibility() == View.VISIBLE;

        // 点击更多按钮，显示其他两个
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果显示就隐藏，如果隐藏就显示
                if (isShow_btn) {
                    hint_btn();//关闭
                } else {
                    show_btn();//打开
                }
            }
        });

        //搜索按钮点击
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //打开搜索页
                startActivity(new Intent(getActivity(), SearchActivity.class));

                //清空动画，隐藏俩按钮
                quickHint();
            }
        });
        //个人中心按钮点击
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //没有登录就去登录页，登录就去个人中心，去登录页时带标记
                if (SJT_UI_Utils.userState()) {

                    //打开个人中心页，记录从哪个页面打开
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    intent.putExtra("loadCategory", getPagerIndex());
                    //给JS保存
                    ConstJS_F.loadCategory = getPagerIndex() + "";
                    startActivity(intent);

                } else {
                    UIUtils.showToastSafe("请登录...");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("userActivity", true);
                    intent.putExtra("loadCategory", getPagerIndex());
                    //给JS保存
                    ConstJS_F.loadCategory = getPagerIndex() + "";
                    startActivity(intent);
                }


                //隐藏按钮
                quickHint();
            }
        });

    }

    /**
     * 快速隐藏按钮
     */
    private void quickHint() {
        isShow_btn = false;
        btn_more.clearAnimation();
        btn_more.setRotation(0f);
        btn_search.setVisibility(View.GONE);
        btn_user.setVisibility(View.GONE);

        //
    }


    /**
     * 显示按钮
     */
    private void show_btn() {
        //刷新状态
        isShow_btn = true;

        //自己旋转
//        ObjectAnimator.ofFloat(btn_more, "rotation", 0f, 45f).setDuration(250L).start();
//        btn_more.invalidate();

        final RotateAnimation rotateAnimation = new RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250L);
        rotateAnimation.setFillAfter(true);
        btn_more.startAnimation(rotateAnimation);
//        btn_more.setImageResource(R.drawable.icon_more_45);
//        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                //结束动画后重新设置图片，避免锯齿
//                rotateAnimation.setFillAfter(false);
//                btn_more.setImageResource(R.drawable.icon_more_45);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });


        //显示动画
        btn_search.setVisibility(View.VISIBLE);
        btn_user.setVisibility(View.VISIBLE);
        showAnim(-1f, 220L, btn_search);
        showAnim(-2.5f, 280L, btn_user);
    }

    /**
     * 隐藏按钮
     */
    private void hint_btn() {
        //刷新状态
        isShow_btn = false;

        //自己旋转
//        ObjectAnimator.ofFloat(btn_more, "rotation", 45f, 0f).setDuration(250L).start();
//        btn_more.invalidate();

        RotateAnimation rotateAnimation = new RotateAnimation(45f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(250L);
        rotateAnimation.setFillAfter(true);
        btn_more.startAnimation(rotateAnimation);
//        btn_more.setImageResource(R.drawable.icon_more);
//        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                btn_more.setImageResource(R.drawable.icon_more);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });


        //隐藏动画
        hintAnim(-1f, 220L, btn_search);
        hintAnim(-2.5f, 280L, btn_user);
    }


    /**
     * 显示动画监听
     * @param offset   偏移
     * @param duration 时间
     * @param view
     */
    private void showAnim(float offset, long duration, final View view){

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());

        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, offset, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        //透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);

        //动画时长
        animationSet.setDuration(duration);

        //开始动画集
        view.startAnimation(animationSet);
        //监听
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btn_more.setEnabled(false);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_more.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

     /**
      * 隐藏动画监听
     * @param offset
     * @param duration
     * @param view
     */
    private void hintAnim(float offset, long duration, final View view){

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setInterpolator(new LinearInterpolator());

        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, offset, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        //透明度
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f, 0f);

        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);

        //动画时长
        animationSet.setDuration(duration);
        //保留
//        animationSet.setFillAfter(true);

        //开始动画集
        view.startAnimation(animationSet);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btn_more.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btn_more.setEnabled(true);
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 子类通过重写这个方法设置不同的url
     * @param webView 当前页面的 webView
     */
    protected abstract void setUrl(WebView webView);

    /**
     * @return 给外界获取 webview
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     * @return 给外界获取titleView
     */
    public View getTitleView(){
        return title_group;
    }

    /**
     * @return 当前Fragment对应的刷新控件
     */
    public PullToRefreshWebView getFragmentPullRefreshWebView(){
        return mPullRefreshWebView;
    }

}
