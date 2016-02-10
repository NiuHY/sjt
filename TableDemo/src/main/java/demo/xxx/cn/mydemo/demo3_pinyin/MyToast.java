package demo.xxx.cn.mydemo.demo3_pinyin;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/10/8.
 */
public class MyToast {
    private static Toast toast;
    public static void showMyToast(Context context, String text){
        if (toast == null){
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
        }
        toast.show();
    }
}
