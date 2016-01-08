package com.cmm.worldartapk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.base.BaseApplication;


public class UIUtils {

	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	public static Thread getMainThread() {
		return BaseApplication.getMainThread();
	}

	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}

	/**
	 * dip -> px
	 */
	public static int dip2Px(int dp){
		//获取屏幕显示规格密度
		float scale = getContext().getResources().getDisplayMetrics().density;
		int px = (int) (dp * scale + 0.5);
		return px;
	}

	/**
	 * px -> dip
	 */
	public static int px2Dip(int px){
		//获取屏幕显示规格密度
		float scale = getContext().getResources().getDisplayMetrics().density;
		int dp = (int) (px / scale + 0.5);
		return dp;
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return BaseApplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		System.out.println(".......haha" + getContext() == null ? true : false);
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择器 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}

	// 判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	public static void startActivity(Intent intent) {
		BaseActivity activity = (BaseActivity) BaseActivity
				.getForegroundActivity();
		if (activity != null) {
			activity.startActivity(intent);
			// 开启动画
			activity.overridePendingTransition(R.anim.setg_next_in, R.anim.setg_next_out);
		} else {
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getContext().startActivity(intent);
		}

	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}

	private static Toast myToast;
	private static void showToast(String str) {
//		BaseActivity frontActivity = (BaseActivity) BaseActivity
//				.getForegroundActivity();
//		if (frontActivity != null) {
//			if (myToast == null) {
//				myToast = Toast.makeText(frontActivity, str, Toast.LENGTH_SHORT);
//
//			}else{
//				myToast.setText(str);
//			}
//			myToast.show();
//		}
		if (myToast == null) {
			myToast = Toast.makeText(BaseApplication.getApplication(), str, Toast.LENGTH_SHORT);

		}else{
			myToast.setText(str);
		}
		myToast.show();
	}

	//隐藏Toast
	public static void hintToast(){
		if (myToast != null){
			myToast.cancel();
		}
	}

	/**
	 * 在当前Activity上弹出窗口，通过传入view 和 windowManager 弹出，在外界控制显示
	 * @param windowManager 当前Activity对应的 windowManager
	 * @param contentView 当前窗口要显示的内容
	 */
	public static void showWindow(WindowManager windowManager, View contentView){

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
		windowManager.addView(contentView, params);
	}
}
