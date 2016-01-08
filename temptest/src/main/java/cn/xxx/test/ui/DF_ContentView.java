package cn.xxx.test.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2015/12/31.
 */
public class DF_ContentView extends FrameLayout {
    private Context context;

    public DF_ContentView(Context context) {
        super(context);
        initView(context);
    }

    public DF_ContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DF_ContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context) {
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        Paint mPaint = new Paint();
//        mPaint.setColor(0xff000099);
//        mPaint.setAntiAlias(true);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
//
//        Path path = new Path();
//        path.moveTo(0, 5);
//        path.lineTo(0, getMeasuredHeight());
//        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
//        path.lineTo(getMeasuredWidth(), getMeasuredWidth() / 10 +5);
//        path.close();
//
//        canvas.drawPath(path, mPaint);
    }

    @Override
    public void draw(final Canvas canvas) {

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, getMeasuredHeight());
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path.lineTo(getMeasuredWidth(), getMeasuredWidth() / 10);
        path.close();

        canvas.clipPath(path); //这样不能抗锯齿 需要画笔配合

        super.draw(canvas);
    }
}
