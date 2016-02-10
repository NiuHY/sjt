package demo.xxx.cn.mydemo.demo4;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

import demo.xxx.cn.mydemo.R;

/**
 * Created by Administrator on 2016/1/28.
 */
public class Utils {
    /**
     * 弹出一个提示框
     * @param activity 当前提示框所在的Activity
     */
    public static void showDialog(Activity activity){

        //内容布局
        View view = View.inflate(activity, R.layout.showdialog_imageview, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog clearDialog = builder.create();
        clearDialog.setView(view, 0, 0, 0, 0);

        //点击关闭
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDialog.dismiss();
            }
        });

        //显示
        clearDialog.show();

        //show之后才能设置
        //设置宽高
        clearDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
        clearDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
    }
}
