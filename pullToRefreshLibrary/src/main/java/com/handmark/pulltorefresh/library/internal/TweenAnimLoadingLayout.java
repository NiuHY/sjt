package com.handmark.pulltorefresh.library.internal;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

/**
 * @date 2015/1/8
 * @author wuwenjie
 * @desc 帧动画加载布局
 */
public class TweenAnimLoadingLayout extends LoadingLayout {

    private AnimationDrawable animationDrawable;
//    private final ObjectAnimator rotation;
    private final RotateAnimation rotateAnimation;
    private final ObjectAnimator oa;
    private final ObjectAnimator oax;
    private final TranslateAnimation translateAnimation;
    private final View view1;
    private final View view2;
    private final View view3;
    private final View view4;

    public TweenAnimLoadingLayout(Context context, Mode mode,
                                  Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        // 初始化
//        mHeaderImage.setImageResource(R.drawable.load_anim);
//        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();

//        rotation = ObjectAnimator.ofFloat(mHeaderImage, "rotation", 0f, 360f);
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setRepeatMode(Animation.REVERSE);

        oa = ObjectAnimator.ofFloat(mHeaderImage, "rotationY", 0f, 360f);

        //属性动画属性
        oa.setDuration(2000L);
        oa.setRepeatCount(-1);
        oa.setRepeatMode(Animation.RESTART);

        oax = ObjectAnimator.ofFloat(mHeaderImage, "scaleX", 0.2f, 1.5f);

        //属性动画属性
        oax.setDuration(3000L);
        oax.setRepeatCount(-1);
        oax.setRepeatMode(Animation.REVERSE);

        translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 5f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setRepeatCount(-1);

        view1 = findViewById(R.id.loading_view_1);
        view2 = findViewById(R.id.loading_view_2);
        view3 = findViewById(R.id.loading_view_3);
        view4 = findViewById(R.id.loading_view_4);

    }

    //播放动画
    private void showMyAnim(View view, float left, float right){

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, left, Animation.RELATIVE_TO_SELF, right, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(700);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setRepeatCount(-1);

        view.startAnimation(translateAnimation);
    }

    // 默认图片
    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.load_anim;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        // NO-OP
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
    }
    // 下拉以刷新
    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
    }
    // 正在刷新时回调
    @Override
    protected void refreshingImpl() {

        // 播放动画
        showMyAnim(view1, 0f, 5f);
        showMyAnim(view2, 0f, 1.5f);
        showMyAnim(view3, 0f, -1.5f);
        showMyAnim(view4, 0f, -5f);

    }
    // 释放以刷新
    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }
    // 重新设置
    @Override
    protected void resetImpl() {

        // 停止动画
        try {
            view1.clearAnimation();
            view2.clearAnimation();
            view3.clearAnimation();
            view4.clearAnimation();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
