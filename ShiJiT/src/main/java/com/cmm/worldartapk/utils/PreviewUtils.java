package com.cmm.worldartapk.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.activity.LoginActivity;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.Netroid;
import com.cmm.worldartapk.net_volley_netroid.net_2.MyNetWorkObject;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.net_volley_netroid.net_2.RequestMapData;
import com.cmm.worldartapk.ui.ExtendedViewPager;
import com.cmm.worldartapk.ui.TouchImageView;
import com.duowan.mobile.netroid.Listener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/1/18.
 */
public class PreviewUtils {

    private Activity activity;

    private ArrayList<ImagePreInfo> imagePathList;
    private ViewManager windowManager;
    private View dfWindowView;
    private Button saveBT;
    private Button addBT;
    private Button cancelBT;
    private View imagevp_btngroup;
    private View vp_ll_btViewnGroup;
    private boolean isWindowViewShow;
    private int loadCategory;

    public PreviewUtils(Activity activity, int loadCategory) {
        this.activity = activity;
        this.loadCategory = loadCategory;
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
            windowManager = activity.getWindowManager();
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
        // TODO 是否需要收藏
        if (3 == 3){
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
                                collect(imageId, "artwork");
                            }else {
                                SJT_UI_Utils.showDialog(activity, "收藏失败", false, loadCategory);
                            }

                        } else {
                            UIUtils.showToastSafe("请登陆...");
                            activity.startActivity(new Intent(activity, LoginActivity.class));
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
//                        UIUtils.showToastSafe("已保存");
                        // TODO 保存图片
                        SJT_UI_Utils.showDialog(activity, "已保存", true, loadCategory);
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
                SJT_UI_Utils.showDialog(activity, "收藏成功", true, loadCategory);
            }

            @Override
            public void onError(String msg) {
                //请求失败，隐藏
                UIUtils.showToastSafe("收藏失败 ：" + msg);
            }
        });
    }
}
