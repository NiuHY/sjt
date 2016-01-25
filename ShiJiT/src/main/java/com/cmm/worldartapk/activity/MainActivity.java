package com.cmm.worldartapk.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.fragment.FragmentFactory;
import com.cmm.worldartapk.fragment.WebViewBaseFragment;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.publicinfo.ConstInfo;
import com.cmm.worldartapk.ui.MyViewPager;
import com.cmm.worldartapk.utils.DrawableUtils;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.UIUtils;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class MainActivity extends BaseActivity {

    //三页
    private static String[] pagerName = {"pager1", "pager2", "pager3"};


    private View contentView;
    private MyViewPager viewPager;
    private View loadingPagerView;
    private MyFragmentAdapter myFragmentAdapter;


    /**
     * @return 返回内容页View 如果有XWalkView就通过父类的方法设置
     */
    @Override
    protected View getContentView() {
        //返回内容View，初始化XWalkView
        contentView = View.inflate(UIUtils.getContext(), R.layout.activity_main, null);

        //添加 正在加载的布局
        // 添加加载页面
        loadingPagerView = new View(UIUtils.getContext());
        loadingPagerView.setVisibility(View.GONE);
        loadingPagerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        loadingPagerView.setBackgroundColor(0xffcdcdcd);
        ((ViewGroup) contentView).addView(loadingPagerView);

//        /**
//         * 切换页面的旋转动画
//         */
//        View loadingAnim = loadingPagerView.findViewById(R.id.loading_anmi);
//        loadingAnim.setVisibility(View.GONE);
//        //旋转动画
//        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        rotateAnimation.setDuration(1234L);
//        rotateAnimation.setRepeatCount(-1);
//        rotateAnimation.setRepeatMode(Animation.RESTART);
//        rotateAnimation.setInterpolator(new LinearInterpolator());
//
//        //开启动画 (默认隐藏状态)
//        loadingAnim.startAnimation(rotateAnimation);


        // 如果有XWalkView 要在这里初始化
//        xWalkView = contentView.findViewById(R.id.xwalkview_main);
        return contentView;
    }

    /**
     * setContentView之后
     * 这里findViewById等
     */
    @Override
    protected void initView() {


        // ViewPager
        viewPager = (MyViewPager) findViewById(R.id.main_viewpager);
        mainActivityViewPager = viewPager;


        //初始化首页截图load集合
        mLoadingPagerView_BG_List = new ArrayList<Drawable>();
        for (int i = 0; i < pagerName.length; i++) {
            // TODO 默认背景
            mLoadingPagerView_BG_List.add(DrawableUtils.createDrawable(0xffcccccc, 0, 0));
        }

        //初始化首页ViewPager
        intiHomeVP();

        // 判断是否已经打开了应用，如果打开就不再显示欢迎；页
        if (!ConstInfo.isOpenApp) {
            startActivity(new Intent(this, SplashActivity.class));
            ConstInfo.isOpenApp = true;
        }

        //判断是否有网络没有网络就弹出对话框退出
        if (!NetUtils.hasConnectedNetwork()) {
            NetUtils.openNetworkSetting(this);
            //记录刚刚进行了联网设置
            isRefush = true;
        }
    }

    private void intiHomeVP() {

        //预加载
        viewPager.setOffscreenPageLimit(2);
        //三个页面用 Fragment填充(容易控制)
        // 设置适配器
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentAdapter);

        //进入时是第二页
        viewPager.setCurrentItem(1);
        ((WebViewBaseFragment) myFragmentAdapter.getItem(1)).setOnFinishListener(new WebViewBaseFragment.OnFinishListener() {
            @Override
            public void onFinish() {
                initRegistWebView(1);
            }
        });


        //ViewPager 切换动画
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.9f;
            private static final float MIN_ALPHA = 0.6f;


            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();


                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    //透明度变化
//                    view.setAlpha(MIN_ALPHA);

                } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
                { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//                    if (position < 0)
//                    {
//                        view.setTranslationX(horzMargin - vertMargin / 2);
//                    } else
//                    {
//                        view.setTranslationX(-horzMargin + vertMargin / 2);
//                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    //透明度变化
//                    view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
//                            / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    //透明度变化
                    // view.setAlpha(MIN_ALPHA);
                }
            }
        });

        //设置页面切换监听，关联webView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PullToRefreshWebView currentPullToRefreshWebView = getCurrentPullToRefreshWebView();
                if (currentPullToRefreshWebView != null && currentPullToRefreshWebView.isRefreshing()) {
                    //currentPullToRefreshWebView.onRefreshComplete();

                }
            }

            @Override
            public void onPageSelected(final int position) {
                //给Activity的当前的Fragment 注册webView
                initRegistWebView(position);

                //显示加载动画的条件之一 1/2
                isSelected = true; //满足一个条件，但这里不显示动画
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                LogUtils.e(" state-------- " + state);

                if (mVP_CurrentWebView != null && state == 0) {
                    //显示加载动画的条件之一 1/2
                    isIdle = true; //满足一个条件

                    //截图，保存到load集合
                    Bitmap bitmap = DrawableUtils.getScreenshot(mVP_CurrentWebView);
                    if (bitmap != null) {
                        mLoadingPagerView_BG_List.set(currentPosition, new BitmapDrawable(bitmap));
                    }
                    //加载动画 魅族待
//                    showLoadingAnim();

                }
            }
        });


    }


    //每一页的截图背景
    private List<Drawable> mLoadingPagerView_BG_List;

    //两种情况
    private boolean isSelected = false;
    private boolean isIdle = false;

    private int currentPosition = -1;

    //显示加载动画
    private void showLoadingAnim() {
        LogUtils.e(mLoadingPagerView_BG_List.toString());
        //选择页面后，空闲状态，两种情况都满足 才显示
        if (isSelected && isIdle) {
            //更改背景，显示截图集合中对应页面的背景，有默认
            if (Build.VERSION.SDK_INT <= 16) {
                loadingPagerView.setBackgroundDrawable(mLoadingPagerView_BG_List.get(currentPosition));
            } else {
                loadingPagerView.setBackground(mLoadingPagerView_BG_List.get(currentPosition));
            }

            //显示加载
            loadingPagerView.setVisibility(View.VISIBLE);
            // 200ms 后隐藏
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingPagerView.setVisibility(View.GONE);
                }
            }, 250L);

            //显示后重置
            isSelected = false;
            isIdle = false;
        }

    }

    /**
     * 当前ViewPager页中的 WebView
     */
    private WebView mVP_CurrentWebView;

    /**
     * setContentView之前
     * 先各种初始化，后初始化界面
     */
    @Override
    protected void init() {

//        //判断是否有网络，没有网络就进网络设置页
//        if ( !NetUtils.hasConnectedNetwork()){
//            NetUtils.openNetworkSetting(this);
//        }


    }

    /**
     * 初始化WebView和Activity的关联
     *
     * @param position
     */
    private void initRegistWebView(int position) {

        //初始化注册等
        if (myFragmentAdapter != null) {
            final WebViewBaseFragment currentFragment = (WebViewBaseFragment) myFragmentAdapter.getItem(position);

            //当前TitleView
            titleView = currentFragment.getTitleView();
            //当前WebView
            mVP_CurrentWebView = currentFragment.getWebView();

            //当前WebView对应的 刷新控件 注册给Activity
            setCurrentPullToRefreshWebView(currentFragment.getFragmentPullRefreshWebView());
            currentPosition = position;
            setWebView(mVP_CurrentWebView);
        }

    }


    //ViewPager的 Fragment适配器
    private class MyFragmentAdapter extends FragmentStatePagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // 通过工厂类创建Fragment
            return FragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            return pagerName.length;
        }
    }

    /**
     * 当前页面的titleView
     */
    private View titleView;

    /**
     * 显示TitleView
     */
    public void showTitleView() {
        titleView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏TitleView
     */
    public void hideTitleView() {
        titleView.setVisibility(View.GONE);
    }

//    // 下拉刷新操作
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();// 获取控件margin属性
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                moveStart = event.getY();// 记录起始移动位置
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveStop = event.getY() - moveStart;// 记录移动了多少尺寸
//                if (moveStop > 1) {// 屏蔽只点击不滑动
//                    params.topMargin = (int) moveStop / 3;// 移动尺寸缩小
//
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                params.topMargin = 0;// 将控件还原
//                break;
//        }
//        viewPager.setLayoutParams(params);// 设置margin属性
//        return super.onTouchEvent(event);
//    }


    private static MyViewPager mainActivityViewPager;

    public static MyViewPager getViewPager() {
        return mainActivityViewPager;
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
