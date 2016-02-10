package com.cmm.worldartapk.utils.share_package;

import android.app.Activity;
import android.graphics.Bitmap;

import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by Administrator on 2015/12/30.
 * 杂项
 */
public class OtherUtils {

    private static String title;
    private static String url;
    private static Bitmap image;
    private static String info;
    private static String imageUrl;
    private static int loadCategory;//从哪里打开

    private static Activity activity;

    /**
     * 设置分享数据
     *
     * @param title 分享标题
     * @param url   点击url
     * @param info  内容
     * @param image 图片
     */
    public static void setShareData(Activity activity, String title, String url, String info, Bitmap image, int loadCategory) {

        OtherUtils.activity = activity;

        OtherUtils.title = title;
        OtherUtils.url = url;
        OtherUtils.info = info;
        OtherUtils.image = image;
        OtherUtils.loadCategory = loadCategory;
    }

    /**
     * 设置分享数据
     *
     * @param title    分享标题
     * @param url      点击url
     * @param info     内容
     * @param imageUrl 图片
     */
    public static void setShareData(Activity activity, String title, String url, String info, String imageUrl, int loadCategory) {

        OtherUtils.activity = activity;

        OtherUtils.title = title;
        OtherUtils.url = url;
        OtherUtils.info = info;
        OtherUtils.imageUrl = imageUrl;
        OtherUtils.loadCategory = loadCategory;
    }

    /**
     * 分享方法  QQ分享  新浪微博  微信好友  微信朋友圈
     *
     * @param target 平台名 例：QQ.NAME
     */
    public static void shareMethod(String target) {
        //公共方法
        Platform.ShareParams shareParams = new Platform.ShareParams();


        //分享的标题(链接)，文本，图片。。。

        if (target.equals(SinaWeibo.NAME)) {
            //新浪微博的参数
            shareParams.setText(title + " " + url);
            shareParams.setImageUrl(imageUrl);
        } else {
            shareParams.setTitle(title);
            shareParams.setText(info);
            shareParams.setImageUrl(imageUrl);
            shareParams.setTitleUrl(url);
            shareParams.setUrl(url);
            shareParams.setShareType(Platform.SHARE_WEBPAGE);
        }
        //专题封面图
//        shareParams.setImageData(image);
//        shareParams.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");


        //根据目标设置回调
        Platform platform = ShareSDK.getPlatform(target);

        if (!platform.isClientValid()) {
            SJT_UI_Utils.showDialog(activity, "分享失败", false, loadCategory);

            UIUtils.showToastSafe("没有找到客户端，请检查");
//            Toast.makeText(activity, "没有找到客户端，请检查", Toast.LENGTH_SHORT).show();
//            UIUtils.hintToast();
            return;
        }


//        platform.SSOSetting(false);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                platform.removeAccount(true);
                SJT_UI_Utils.showDialog(activity, "分享成功", true, loadCategory);
//                LogUtils.e("分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                platform.removeAccount(true);
                SJT_UI_Utils.showDialog(activity, "分享失败", false, loadCategory);
                LogUtils.e(platform.toString(), throwable);
                LogUtils.e("分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                platform.removeAccount(true);
//                LogUtils.e(platform.toString());
                SJT_UI_Utils.showDialog(activity, "取消分享", false, loadCategory);
//                LogUtils.e("取消分享");
            }
        });

        //分享
        platform.share(shareParams);





    }
}
