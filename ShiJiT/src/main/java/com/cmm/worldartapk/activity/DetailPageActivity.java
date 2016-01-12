package com.cmm.worldartapk.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.Netroid;
import com.cmm.worldartapk.net_volley_netroid.net_2.MyNetWorkObject;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.net_volley_netroid.net_2.RequestMapData;
import com.cmm.worldartapk.publicinfo.ConstInfo;
import com.cmm.worldartapk.ui.ExtendedViewPager;
import com.cmm.worldartapk.ui.PullRefreshUtils;
import com.cmm.worldartapk.ui.TouchImageView;
import com.cmm.worldartapk.utils.FileUtils;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.share_package.OtherUtils;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;
import com.duowan.mobile.netroid.Listener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2015/12/11.
 */
public class DetailPageActivity extends BaseActivity {

    /**
     * 今坛
     */
//    private final static String DETAIL_INFORMATION = "http://cmm.yuntoo.com/html/detail_information.html";
    private final static String DETAIL_INFORMATION = "file:///android_asset/detail_information.html";
    /**
     * 展览
     */
//    private final static String DETAIL_EXHIBITION = "http://cmm.yuntoo.com/html/detail_exhibition.html";
    private final static String DETAIL_EXHIBITION = "file:///android_asset/detail_exhibition.html";
    /**
     * 艺术馆
     */
//    private final static String DETAIL_GALLERY = "http://cmm.yuntoo.com/html/detail_gallery.html";
    private final static String DETAIL_GALLERY = "file:///android_asset/detail_gallery.html";

    private View contentView;
    private Intent intent;
    private String loadUrl;
    private int loadCategory;
    private RelativeLayout shareLayout;
    private WindowManager windowManager;
    private View dfWindowView;
    private boolean isWindowViewShow;
    private ArrayList<ImagePreInfo> imagePathList;
    private View vp_ll_btViewnGroup;
    private Button saveBT;
    private Button addBT;
    private Button cancelBT;

    // 收藏分类
    private final String COLLECT_TYPE_GALLERY = "gallery";
    private final String COLLECT_TYPE_EXHIBITION = "exhibition";
    private final String COLLECT_TYPE_INFORMATION = "information";
    private final String COLLECT_TYPE_ARTWORK = "artwork";
    private PullToRefreshWebView mPullRefreshWebView;
    private View imagevp_btngroup;
    private String detailPagerUrl;

    @Override
    protected void init() {

        UIUtils.showToastSafe("开始加载详情");

        //得到 打开这个详情页的 Intent
        intent = getIntent();

        //这是哪一个板块的详情页
        loadCategory = intent.getIntExtra("loadCategory", ConstInfo.JINTAN);
    }

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }

    @Override
    protected View getContentView() {
        contentView = View.inflate(this, R.layout.detailpage_activity, null);
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

        // 下拉刷新
        mPullRefreshWebView = (PullToRefreshWebView) contentView.findViewById(R.id.pull_refresh_webview);
        // 初始化 webView
        webView = PullRefreshUtils.setListener_PRWebView(mPullRefreshWebView);


        //给webView 添加一个透明头 用了触发分享
        View detailPageHead = new View(UIUtils.getContext());

        detailPageHead.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                //判断sp中 share 对应的 id值是否和当前详情页一致
                //获取JSON串
//                JsScope.setSP(null, "share", "test");
                String shareJson = sp.getString("share", "");

                LogUtils.e(shareJson);

                if (!TextUtils.isEmpty(shareJson)){
                    //获取当前页id
                    String currentId = ConstJS_F.detailId;
                    //解析json 比较id是否一致
                    try {
                        JSONObject jsonObject = new JSONObject(shareJson);

                        String jsonId = jsonObject.getString("id");
                        if (jsonId != null && TextUtils.equals(currentId, jsonId)){

                            //打开分享页
                            showShareWindow();

                            //得到其他数据  标题，信息，图片，url
                            final String title = jsonObject.getString("title");
                            final String info = jsonObject.getString("intro");
                            String imageUrl = jsonObject.getString("cover");
                            final String url = jsonObject.getString("href");

                            // 复制链接
                            detailPagerUrl = url;

//                            LogUtils.e(imageUrl);

//                            //TODO share Bitmap
//                                NetUtils.getByteByNet("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg", new Listener<byte[]>() {
//                                    @Override
//                                    public void onSuccess(byte[] response) {
//
//                                        BitmapFactory.Options options = new BitmapFactory.Options();
//
//                                        Bitmap imageData = BitmapFactory.decodeByteArray(response, 0, response.length);
//
//                                        //设置缩略图
//                                        //请求成功
////                                        OtherUtils.setShareData(DetailPageActivity.this, title, url, info, ThumbnailUtils.extractThumbnail(imageData, 500, 500, ThumbnailUtils.OPTIONS_RECYCLE_INPUT));
//                                        OtherUtils.setShareData(DetailPageActivity.this, title, url, info, imageData);
//
//                                    }
//                                });

                            OtherUtils.setShareData(DetailPageActivity.this, title, url, info, imageUrl);

                        }else{
                            UIUtils.showToastSafe("分享数据准备中...");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    UIUtils.showToastSafe("分享数据准备中...");
                }

                return false;
            }
        });
        //添加头部分享
        webView.removeAllViews();
        webView.addView(detailPageHead, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dip2Px(275)));

        // 返回按钮 背景，加载不同内容
        switch (loadCategory) {
            case 1:
                myBack.setBackgroundResource(R.drawable.icon_back_bg_yellow);
                //加载url
                webView.loadUrl(DETAIL_INFORMATION);
                break;
            case 2:
                myBack.setBackgroundResource(R.drawable.icon_back_bg_red);
                //加载url
                webView.loadUrl(DETAIL_EXHIBITION);
                break;
            case 3:
                myBack.setBackgroundResource(R.drawable.icon_back_bg_blue);
                //加载url
                webView.loadUrl(DETAIL_GALLERY);
                break;
            default:
                break;
        }

    }

    //添加一个窗体来  分享
    private void showShareWindow() {
        //初始化窗体
        if(windowManager == null){
            windowManager = getWindowManager();
        }

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

        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(150L);

        share_qq.startAnimation(scaleAnimation);
        share_weibo.startAnimation(scaleAnimation);
        share_wenxin.startAnimation(scaleAnimation);
        share_friend.startAnimation(scaleAnimation);
        share_copyLink.startAnimation(scaleAnimation);
        share_collect.startAnimation(scaleAnimation);

        //设置点击监听
        shareClick(share_qq);
        shareClick(share_weibo);
        shareClick(share_wenxin);
        shareClick(share_friend);
        shareClick(share_copyLink);
        shareClick(share_collect);

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

                String shareName = "";

                switch (v.getId()) {
                    //四个分享
                    case R.id.share_btn_qq:
                        //点击调用QQ分享
                        shareName = QQ.NAME;
                        break;
                    case R.id.share_btn_weibo:
                        //新浪微博
                        shareName = SinaWeibo.NAME;
                        break;
                    case R.id.share_btn_wenxin:
                        //微信好友
                        shareName = Wechat.NAME;
                        break;
                    case R.id.share_btn_friend:
                        //微信朋友圈
                        shareName = WechatMoments.NAME;
                        break;

                    case R.id.share_btn_copyLink:
                        //获取剪贴板管理器，复制到剪贴板
                        ClipboardManager jtb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        //TODO
//                        ClipData clipData = new ClipData();
//                        jtb.setPrimaryClip(clipData);
                        if (TextUtils.isEmpty(detailPagerUrl)){
                            SJT_UI_Utils.showDialog(DetailPageActivity.this, "复制失败", false);
                        }else{
                            jtb.setText(detailPagerUrl);
                            SJT_UI_Utils.showDialog(DetailPageActivity.this, "复制成功", true);
                        }
                        break;
                    case R.id.share_btn_collect:

                        //点击后判断是否登陆，如果登陆就收藏成功，没有登陆就跳到登陆页面
                        //TODO 收藏 连
                        if (SJT_UI_Utils.userState()) {
                            if (!TextUtils.isEmpty(ConstJS_F.detailId)){
                                collect(ConstJS_F.detailId, COLLECT_TYPE_GALLERY);
                            }else {
                                SJT_UI_Utils.showDialog(DetailPageActivity.this, "收藏失败", false);
                            }

                        } else {
                            UIUtils.showToastSafe("请登陆...");
                            startActivity(new Intent(DetailPageActivity.this, LoadActivity.class));
                        }

                        break;
                    default:
                        break;
                }

                if (!TextUtils.isEmpty(shareName)){
                    OtherUtils.shareMethod(shareName);
                }

                //点击后关闭分享
                if (isWindowViewShow) {
                    windowManager.removeView(dfWindowView);
                    isWindowViewShow = false;
                }

                //提示点击成功
                UIUtils.showToastSafe("正在启动分享...");
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
        //在按返回键时判断是否打开了窗口，如果打开了就先关闭它
        if (keyCode == KeyEvent.KEYCODE_BACK && isWindowViewShow) {
            windowManager.removeView(dfWindowView);
            isWindowViewShow = false;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //图片类
    private class ImagePreInfo{
        /**
         * 图片url
         */
        public String url;
        /**
         * 收藏图片需要的id
         */
        public String id;
        /**
         * 图片位置
         */
        public String index;
        /**
         * 图片描述信息
         */
        public String imageDescription;
    }

    /**
     * 添加一个窗体来  图片预览
     * @param imgsJson
     * @param index
     */
    private long evpClickTime;//ViewPager 点击延时
    public void showVPWindow(String imgsJson, int index) {
        //把Json解析成url集合  imagePathList
        imagePathList = new ArrayList<ImagePreInfo>();
        try {
            JSONArray jsonArray = new JSONArray(imgsJson);
            jsonArray.length();
            for (int i = 0; i < jsonArray.length(); i++) {
                ImagePreInfo imagePreInfo = new ImagePreInfo();
                imagePreInfo.url = jsonArray.getJSONObject(i).getString("url");
                imagePreInfo.id = jsonArray.getJSONObject(i).getString("resourceId");
//                imagePreInfo.index = jsonArray.getJSONObject(i).getString("imgIndex");
                imagePreInfo.imageDescription = jsonArray.getJSONObject(i).getString("ImageDescription");

                imagePathList.add(imagePreInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //初始化窗体
        if(windowManager == null){
            windowManager = getWindowManager();
        }

        dfWindowView = View.inflate(UIUtils.getContext(), R.layout.imagepreview_window, null);


        //预览ViewPager
        ExtendedViewPager evp = (ExtendedViewPager) dfWindowView.findViewById(R.id.evp);

        //隐藏的保存收藏布局
        vp_ll_btViewnGroup = dfWindowView.findViewById(R.id.imagevp_btn_ll);

        //图片描述 信息和按钮
        final TextView imageInfo_tv = (TextView) dfWindowView.findViewById(R.id.imagepre_vp_imageinfo);
        final View imageInfo_bt = dfWindowView.findViewById(R.id.imagepre_vp_imageinfo_btn);
        //点击图片描述 隐藏它，显示按钮，点击按钮，隐藏它，显示图片描述，在切换页面时切换对应的图片描述
        imageInfo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo_tv.setVisibility(View.GONE);
                imageInfo_bt.setVisibility(View.VISIBLE);
            }
        });
        imageInfo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo_tv.setVisibility(View.VISIBLE);
                imageInfo_bt.setVisibility(View.GONE);
            }
        });
        //设置对应描述信息
        imageInfo_tv.setText(imagePathList.get(index).imageDescription);
        //ViewPager切换页面改变信息
        evp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imageInfo_tv.setText(imagePathList.get(position).imageDescription);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //三个按钮
        saveBT = (Button) dfWindowView.findViewById(R.id.imagevp_save);
        addBT = (Button) dfWindowView.findViewById(R.id.imagevp_add);
        cancelBT = (Button) dfWindowView.findViewById(R.id.imagevp_cancel);

        //button组
        imagevp_btngroup = dfWindowView.findViewById(R.id.imagevp_btngroup);

        //点击消失
        View blankView = dfWindowView.findViewById(R.id.imagepreview_blank);
        blankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vp_ll_btViewnGroup != null){
                    vp_ll_btViewnGroup.setVisibility(View.GONE);
                }
            }
        });

        //如果是第三页才显示 收藏按钮
        if (loadCategory == 3){
            addBT.setVisibility(View.VISIBLE);
        }else{
            addBT.setVisibility(View.GONE);
        }

        // 给预览页 设置点击监听，如果点击后500ms中没有再次点击 就关闭预览
        dfWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (evpClickTime > 0){
                    if ((SystemClock.currentThreadTimeMillis() - evpClickTime) < 500L){
                        // 关闭预览
                        if (isWindowViewShow) {
                            windowManager.removeView(dfWindowView);
                            isWindowViewShow = false;
                            return;
                        }
                    }
                }
                evpClickTime = SystemClock.currentThreadTimeMillis();
            }
        });



        //给ViewPager设置适配器
        evp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imagePathList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                //通过路径加载图片
                final String imagePath = imagePathList.get(position).url;

                //图片的id
                String imageId = imagePathList.get(position).id;

                if (imagePath.endsWith(".gif")) {

                    //如果是 gif图片就换显示方法
                    final GifImageView gifImageView = new GifImageView(UIUtils.getContext());

                    gifImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 关闭预览
                            if (isWindowViewShow) {
                                windowManager.removeView(dfWindowView);
                                isWindowViewShow = false;
                                return;
                            }
                        }
                    });

                    //load
                    gifImageView.setImageResource(R.drawable.ic_launcher);

                    //通过imagePath判断本地是否有缓存，有就从本地加载，没有才从网络加载
                    if (FileUtils.findGifFile(imagePath)){
                        //存在，直接从本地缓存加载
                        try {
                            GifDrawable gifDrawable = new GifDrawable(FileUtils.getGifpath(imagePath));
                            gifImageView.setImageDrawable(gifDrawable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        // 不存在，从网络获取，然后保存
                        NetUtils.getByteByNet("http://pic.joke01.com/uppic/13-05/30/30215236.gif", new Listener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] response) {
                                try {

                                    GifDrawable gifDrawable = new GifDrawable(response);
                                    gifImageView.setImageDrawable(gifDrawable);

                                    //保存到本地
                                    FileUtils.saveGif(response, imagePath);
                                } catch (IOException e) {
                                    //加载失败，显示错误图片
                                    gifImageView.setImageResource(R.drawable.load_failed);
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    showSaveImageWindow(gifImageView, imageId);//保存
                    container.addView(gifImageView);
                    return gifImageView;
                } else {
                    TouchImageView touchImageView = new TouchImageView(UIUtils.getContext());

                    touchImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 关闭预览
                            if (isWindowViewShow) {
                                windowManager.removeView(dfWindowView);
                                isWindowViewShow = false;
                                return;
                            }
                        }
                    });

                    Netroid.displayImage(imagePath, touchImageView);
                    showSaveImageWindow(touchImageView, imageId);//保存
                    container.addView(touchImageView);
                    return touchImageView;
                }

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        evp.setCurrentItem(index);

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

    // 长按图片 保存和收藏
    private void showSaveImageWindow(final ImageView imageView, final String imageId){
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //dfWindowView
                //显示
                vp_ll_btViewnGroup.setVisibility(View.VISIBLE);
//                UIUtils.showToastSafe(""+dfWindowView.getHeight()+"\n"+vp_ll_btViewnGroup.getHeight());
//                //弹出时的平移动画
                TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                ta.setDuration(300L);
                imagevp_btngroup.startAnimation(ta);

                // 三个按钮
                //保存本地，添加收藏，取消
                cancelBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //隐藏
                        vp_ll_btViewnGroup.setVisibility(View.GONE);
                    }
                });

                addBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //隐藏
                        vp_ll_btViewnGroup.setVisibility(View.GONE);

                        //去 收藏，传 id 和收藏类型
                        // TODO 图片的 id
                        if (SJT_UI_Utils.userState()) {
                            if (!TextUtils.isEmpty(ConstJS_F.detailId)){
                                collect(imageId, COLLECT_TYPE_ARTWORK);
                            }else {
                                SJT_UI_Utils.showDialog(DetailPageActivity.this, "收藏失败", false);
                            }

                        } else {
                            UIUtils.showToastSafe("请登陆...");
                            startActivity(new Intent(DetailPageActivity.this, LoadActivity.class));
                        }
                    }
                });

                saveBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //隐藏
                        vp_ll_btViewnGroup.setVisibility(View.GONE);

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        FileUtils.saveBitmap(bitmapDrawable.getBitmap());
                        UIUtils.showToastSafe("已保存");
                        // TODO 保存图片
                        SJT_UI_Utils.showDialog(DetailPageActivity.this, "已保存", true);
                    }
                });


                return true;
            }
        });
    }

    /**
     * 收藏的方法
     * @param artworkID 收藏目标的 id
     * @param collectType 收藏目标的类型
     */
    private void collect(String artworkID, String collectType) {
        //拼接请求地址
        String url = "";
        try {
            url = Const.BASE_URL + "api/" + collectType + "/" + URLEncoder.encode(artworkID, "UTF-8") + "/collect/";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //请求服务器传数据
        NetUtils.pushStringByNet_POST(url, RequestMapData.params_artworkCollect(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                SJT_UI_Utils.showDialog(DetailPageActivity.this, "收藏成功", true);
                LogUtils.e("收藏成功" + data);
            }

            @Override
            public void onError(String msg) {
                //请求失败，隐藏
                UIUtils.showToastSafe("收藏失败 ：" + msg);
            }
        });
    }
}
