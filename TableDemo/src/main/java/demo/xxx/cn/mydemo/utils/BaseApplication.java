package demo.xxx.cn.mydemo.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class BaseApplication extends Application {

    //上下文
    private static BaseApplication mContext;
    //主线程的Handler
    private static Handler mMainThreadHandler;
    //主线程
    private static Thread mMainThread;
    //主线程ID
    private static int mMainThreadId;
    //Looper
    private static Looper mLooper;

    //
    private static boolean initFlag = false;
    private static int a = 1;

    //记录打开的Activity 的集合
    private static List<Activity> allActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        //重写系统的异常处理器
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                //出现异常时调用
                //把异常信息输出到sd卡对应的文件中
                StringBuilder sb = new StringBuilder();
                //时间
                sb.append("time: " + Formatter.formatFileSize(BaseApplication.this, System.currentTimeMillis()));
                //编译信息
                Field[] fields = Build.class.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        sb.append(field.getName() + " = " + field.get(null) + "\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //异常信息
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                sb.append(sw.toString());

                //把sb中的数据输出到 文件中
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(getExternalFilesDir(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/TestDemo/exception.info"));
                    bw.write(sb.toString());
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //自杀
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        if (! initFlag){
            init();
            initFlag = true;
//            UIUtils.showToastSafe("++"+a++);
        }
    }

    // 初始化
    private void init() {

        //初始化Fresco
        Fresco.initialize(this);

        //给成员变量初始化
        this.mContext = this;
        this.mMainThreadHandler = new Handler();
        this.mMainThread = Thread.currentThread();
        //Returns the identifier of the calling thread, which be used with setThreadPriority(int, int).
        this.mMainThreadId = android.os.Process.myTid();
        this.mLooper = getMainLooper();

        //初始化记录Activity的集合
        allActivity = new ArrayList<>();
    }

    //获取这个集合的方法
    public static List<Activity> getActivityList(){
        return allActivity;
    }


    public static BaseApplication getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Looper getLooper() {
        return mLooper;
    }
}
