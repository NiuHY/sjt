package com.cmm.worldartapk.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;

public class DrawableUtils {

    /**
     * 创建一个图片
     *
     * @param contentColor 内部填充颜色
     * @param strokeColor  描边颜色
     * @param radius       圆角
     */
    public static GradientDrawable createDrawable(int contentColor, int strokeColor, int radius) {
        GradientDrawable drawable = new GradientDrawable(); // 生成Shape
        drawable.setGradientType(GradientDrawable.RECTANGLE); // 设置矩形
        drawable.setColor(contentColor);// 内容区域的颜色
        drawable.setStroke(1, strokeColor); // 四周描边,描边后四角真正为圆角，不会出现黑色阴影。如果父窗体是可以滑动的，需要把父View设置setScrollCache(false)
        drawable.setCornerRadius(radius); // 设置四角都为圆角
        return drawable;
    }

    /**
     * 创建一个图片选择器
     *
     * @param normalState  普通状态的图片
     * @param pressedState 按压状态的图片
     */
    public static StateListDrawable createSelector(Drawable normalState, Drawable pressedState) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressedState);
        bg.addState(new int[]{android.R.attr.state_enabled}, normalState);
        bg.addState(new int[]{}, normalState);
        return bg;
    }

    /**
     * 获取图片的大小
     */
    public static int getDrawableSize(Drawable drawable) {
        if (drawable == null) {
            return 0;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }


    /**
     * 获取当前WebView截图
     *
     * @return
     */
    public static Bitmap getScreenshot(WebView webView) {

        View view = ((View)webView.getParent());

        view.setDrawingCacheEnabled(true);

        Bitmap bitmap = null;

        try {

            if (null != view.getDrawingCache()) {

                bitmap = Bitmap.createScaledBitmap(view.getDrawingCache(), view.getMeasuredWidth(), view.getMeasuredHeight(), true);
                Bitmap newBitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                return newBitmap;
            } else {

                return null;
            }

        } catch (OutOfMemoryError e) {

            e.printStackTrace();

        } finally {
            // 回收
            if (bitmap != null){
                bitmap.recycle();
            }
            System.gc();

            view.setDrawingCacheEnabled(false);

            view.destroyDrawingCache();


        }

        return null;
    }
}
