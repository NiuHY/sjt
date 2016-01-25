package com.cmm.worldartapk.base;

import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/1/18.
 *
 * 可以通过右划手势关闭Activity
 */
public abstract class BaseGestureActivity extends BaseActivity {

    //手势监听
    protected GestureDetector gDetector;
    private String className;

    @Override
    protected void init() {

        //当前类名
        className = this.getClass().getSimpleName();

        //初始化手势监听
        //手势检测
        gDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                //判断错误的手势
                //x轴移动太慢 取绝对值
                if (Math.abs(velocityX) < 200) {
//                    Log.i("GestureDetector", "x轴速度移动慢");
                    return true;
                }
                //y轴移动距离过长
                //Returns the original raw Y coordinate of this event
                if (Math.abs(e1.getRawY()-e2.getRawY()) > 120) {
//                    Log.i("GestureDetector", "y轴移动距离过长");
                    return true;
                }
//                //前进
//                if ((e1.getRawX()-e2.getRawX()) > 40) {
//                    //下一页
//                    nextPage();
//                    return true;
//                }
                //后退
                if ((e2.getRawX()-e1.getRawX()) > 180) {
                    //上一页
//                    prePage();

                    setExitSwichLayout();//关闭当前页
                    return true;
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //接收 触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手势检测 --> 检测触摸事件
        gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (TextUtils.isEmpty(className)){
            className = this.getClass().getSimpleName();
        }
        MobclickAgent.onPageEnd(className);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(className)){
            className = this.getClass().getSimpleName();
        }
        MobclickAgent.onPageStart(className);
        MobclickAgent.onResume(this);
    }
}
