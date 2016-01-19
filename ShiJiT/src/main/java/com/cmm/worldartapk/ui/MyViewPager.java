package com.cmm.worldartapk.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/1/15.
 *
 * 可以禁止左右滑动
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @return 返回ViewPager是否可以左右滚动
     */
    public boolean isCanScroll() {
        return isCanScroll;
    }

    /**
     * 设置ViewPager是否可以左右滚动
     * @param isCanScroll true可以滚动
     */
    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    //是否可以左右滑动
    private boolean isCanScroll = true;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 魅族 待
        if (isCanScroll){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }

//        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 魅族 待
        if (isCanScroll){
            return super.onInterceptTouchEvent(ev);
        }else{
            return false;
        }
    }
}
