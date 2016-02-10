package demo.xxx.cn.mydemo.demo3_pinyin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 快速索引条
 * Created by Administrator on 2015/10/8.
 */
public class QuickIndexBar extends View {
    private Context context;

    //选择字母的回调
    public interface OnSelectLetterListener{
        public abstract void selectLetter(String letter);
    }
    private OnSelectLetterListener onSelectLetterListener;
    public void setOnSelectLetterListener(OnSelectLetterListener onSelectLetterListener) {
        this.onSelectLetterListener = onSelectLetterListener;
    }

    //字母集合
    private static final String[] letters = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"
    };

    //当前选中的位置
    private int selectIndex = -1;//默认-1

    //获取当前选中的 位置/字母
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getSelectIndex(y);
                break;
            case MotionEvent.ACTION_MOVE:
                getSelectIndex(y);
                break;
            case MotionEvent.ACTION_UP:
                //离开时重置为 -1
                selectIndex = -1;
                break;
            default:
                break;
        }
        //重绘
        invalidate();

        return false;
    }

    //得到触摸位置的索引/字母
    private void getSelectIndex(float y) {
        //用当前点击的位置(y值)除以字母块的高度得到索引，通过索引获得字母
        selectIndex = (int) (y / letterHeight);
        //屏蔽非法值
        if (selectIndex < 0) selectIndex = 0;
        if (selectIndex > letters.length - 1) selectIndex = letters.length - 1;
        //设置回调
        if (onSelectLetterListener != null){
            onSelectLetterListener.selectLetter(letters[selectIndex]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //遍历集合 画字母
        for (int i = 0; i < letters.length; i++) {
            String letter = letters[i];

            //计算文字所在矩形，可以得到宽高
            Rect bounds = new Rect();
            paint.getTextBounds(letter, 0, 1, bounds);


            //当前选中的字母变色
            paint.setColor(i == selectIndex ? Color.BLUE : Color.RED);

            //绘制的文本 以左下角为原点坐标
            canvas.drawText(letter, letterWidth / 2 - bounds.width() / 2, letterHeight / 2 + bounds.height() / 2 + letterHeight * i, paint);
        }
    }

    /**
     * 隐藏的时候就清空颜色
     */
    public void clearColor(){
        selectIndex = -1;//重置当前选择
        invalidate();
    }

    //测量得到每个字母块的大小
    private float letterWidth;
    private float letterHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        letterWidth = getMeasuredWidth();
        letterHeight = getMeasuredHeight() * 1.0f / letters.length;
        paint.setTextSize(letterHeight*0.8f);
        //paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 21, getResources().getDisplayMetrics()));
    }

    //画笔
    private Paint paint;

    //初始化方法
    private void initView(Context context) {
        this.context = context;

        paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        paint.setColor(Color.RED);//白色
        paint.setFakeBoldText(true);//加粗
        //设置字体
//        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "myfonts/cour.ttf"));
    }

    //构造方法
    public QuickIndexBar(Context context) { super(context); initView(context); }

    public QuickIndexBar(Context context, AttributeSet attrs) { super(context, attrs); initView(context); }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); initView(context); }
}
