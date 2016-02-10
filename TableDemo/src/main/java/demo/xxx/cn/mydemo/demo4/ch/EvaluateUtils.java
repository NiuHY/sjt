package demo.xxx.cn.mydemo.demo4.ch;

/**
 * Created by Administrator on 2015/10/8.
 */
public class EvaluateUtils {
    //求当前百分比
    public static Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
}
