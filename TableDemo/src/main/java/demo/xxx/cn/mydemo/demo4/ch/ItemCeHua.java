package demo.xxx.cn.mydemo.demo4.ch;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Administrator on 2015/10/9.
 */
public class ItemCeHua extends FrameLayout {
    private ViewDragHelper viewDragHelper;
    private Context context;
    private ViewGroup itemVG;
    private ViewGroup buttonVG;
    private int itemHeight;
    private int itemWidth;
    private int maxX;

    //状态枚举 在CeHuaGroup中通过状态值判断条目状态，决定是否消费触摸事件
    public static enum ItemState{
        CLOSE, OPEN, DRAGING
    }
    //默认关闭
    private ItemState itemState = ItemState.CLOSE;
    //获取状态
    public ItemState getItemState() {
        return itemState;
    }
    //更新状态
    private void updateItemState(float percent) {
        if (percent == 0){
            itemState = ItemState.CLOSE;
        }else if (percent == 1){
            itemState = ItemState.OPEN;
        }else{
            itemState = ItemState.DRAGING;
        }
    }

    //初始化方法
    private void initView(Context context) {
        this.context = context;
        //创建View拖拽帮助对象
        viewDragHelper = ViewDragHelper.create(this, 1.0f, callback);
    }

    //ViewDragHelper的Callback
    ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;//子View都可以被拖拽
        }

        //返回大于0的值
        @Override
        public int getViewHorizontalDragRange(View child) {
            return maxX;
        }

        //水平方向移动的位置
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == itemVG) {
                left = fixLeft(left);
            }
            return left;
        }

        //释放View时
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if ((xvel == 0 && getLeft() > -maxX / 2) || xvel > 20) {
                //关闭buttonVG
                close();
            } else {
                //显示
                open();
            }
        }

        //View位置变更
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //屏蔽buttonVG的移动，添加到itemVG
            if (changedView == buttonVG) {
                buttonVG.layout(itemWidth - maxX, 0, itemWidth, itemHeight);
                int newLeft = itemVG.getLeft() + dx;
                newLeft = fixLeft(newLeft);
                itemVG.layout(newLeft, 0, itemWidth + newLeft, itemHeight);
            }

            //伴随动画
            animatorAndOther();

            //重绘
            invalidate();
        }
    };
    //打开和关闭
    private void close(){
        if (viewDragHelper.smoothSlideViewTo(itemVG, 0, 0)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    private void open(){
        if (viewDragHelper.smoothSlideViewTo(itemVG, -maxX, 0)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //View位置改变时的伴随动画，状态等
    private void animatorAndOther() {
        //分度值
        float percent = Math.abs(itemVG.getLeft()*1.0f/maxX);

        //更新状态值
        updateItemState(percent);

        //设置显示
        buttonVG.setVisibility(View.VISIBLE);

        //定制动画
        viewChangeAnim(percent);
    }

    //伴随动画
    private void viewChangeAnim(float percent) {
        //buttonVG的平移
        ViewHelper.setTranslationX(buttonVG, EvaluateUtils.evaluate(percent, itemWidth, itemWidth-maxX));
    }

    //修正lefg
    private int fixLeft(int left) {
        //修正
        if (left < -maxX) {
            left = -maxX;
        }
        if (left > 0) {
            left = 0;
        }
        return left;
    }

    //初始化两个子控件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new IllegalArgumentException("子View必须有两个以上");
        }
        if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("前两个子View必须是一个ViewGroup");
        }
        //获取两个子View对象
        buttonVG = (ViewGroup) getChildAt(0);
        itemVG = (ViewGroup) getChildAt(1);
    }

    //获取条目的宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight();
        //最大移动距离 按钮VG 的宽
        maxX = getChildAt(0).getMeasuredWidth();

       // System.out.println("Item:-----"+Utils.getStatusBarHeight(this));
    }

    //转交触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        requestDisallowInterceptTouchEvent(true);
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    //构造方法
    public ItemCeHua(Context context) {
        super(context);
        initView(context);
    }

    public ItemCeHua(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ItemCeHua(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
}
