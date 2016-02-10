package demo.xxx.cn.mydemo.demo1;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import demo.xxx.cn.mydemo.utils.UIUtils;


/**
 * Created by Administrator on 2016/1/23.
 */
public class ClickManagerTest {

    //三个回调监听， 单击  双击  长按




    // 按下和抬起的时间
    private long[] times = {0, 0};
    private static Handler handler = new Handler(){};
    private long upTime;

    // 虚拟按钮 触摸
    public boolean clickMethod(View view, final MotionEvent event){

        if (event.getAction() == MotionEvent.ACTION_DOWN){

            long downTime = System.currentTimeMillis();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (times[1] - times[0] > 0L){
                        //是单击事件
//                        Log.e("NIU", "单击");
                        UIUtils.showToastSafe("单击");
                        //清空 handler
                        handler.removeCallbacksAndMessages(null);
                    }
                }
            }, 350L);

            if (downTime - times[0] < 300L){
                //是双击事件
//                Log.e("NIU", "双击");
                UIUtils.showToastSafe("双击");

                times[0] = -1;
                //清空 handler
                handler.removeCallbacksAndMessages(null);
            }

            // 每次记录按下的时间
            if (times[0] == -1){
                times[0] = 0;//过滤双击
            }else {
                times[0] = downTime;
            }

            //延时1000毫秒判断按下时间和抬起时间，判断是否是长按
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (times[1] - times[0] <= 0){
                        //是长按事件
//                        Log.e("NIU", "长按");
                        UIUtils.showToastSafe("长按");

                        //清空 handler
                        handler.removeCallbacksAndMessages(null);
                    }
                }
            }, 700L);

        }else if (event.getAction() == MotionEvent.ACTION_UP){
            //判断当前抬起的时间和之前记录的抬起时间，得到是否是双击事件
            upTime = System.currentTimeMillis();

            //记录抬起时间 (判断之前是否是长按事件)
            times[1] = upTime;
        }

        return true;
    }
}
