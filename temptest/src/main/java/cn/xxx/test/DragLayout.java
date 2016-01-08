package cn.xxx.test;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2015/12/31.
 */
public class DragLayout extends FrameLayout {
    private Context context;
    private ViewDragHelper mViewDragHelper;

    //子View
    private ViewGroup mTitleView;
    private ViewGroup mMainContent;
    private int mTitleViewHeight;
    private int mMainContentHeight;
    private int mRange;
    private int mWidth;

    public DragLayout(Context context) {
        super(context);
        initView(context);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        this.context = context;
        //ViewDragHelper
        mViewDragHelper = ViewDragHelper.create(this, cb);
    }

    private ViewDragHelper.Callback cb = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //TitleView 禁止直接移动
            return child == mMainContent;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mTitleViewHeight;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //边界处理
            top = fixTop(top);
            return top;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            //在抬起手指时调用 当没有滑动到顶部时才返回
            if (releasedChild == mMainContent && !isTop) {
                mViewDragHelper.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                //重绘
                invalidate();
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.e("NIU___", " " + left + "  " + top + "  " + dx + " " + dy + "  ");

            //dy
            //当内容布局移动时 给Title布局传递一定比例的 位移
            if (changedView == mMainContent) {
                int titleTop = mTitleView.getTop() + (int) (dy / 1.5f);
                titleTop = fixTitleTop(titleTop);
                //移动
                mTitleView.layout(0, titleTop, mWidth, mTitleViewHeight + titleTop);
                invalidate();
            }
        }
    };

    private int fixTitleTop(int titleTop) {

        if (titleTop < 0) {
            return titleTop;
        } else {
            return 0;
        }
    }

    private boolean isTop = false;
    /**
     * 边界处理
     *
     * @param top 需要修正的top
     * @return 返回修正好的top
     */
    private int fixTop(int top) {
        //判断当前位置是否移动到顶部，如果移动到顶部 变更当前位置状态
        if (top < 0){
            isTop = true;
        }
        //当子View中的ListView顶部拉动到极限时设置其为false;

        if (top < 0) {
            return 0;
        } else if (top > mRange) {
            return mRange;
        }

        return top;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            Log.e("NIU", "DSSSSSSSSSSSSSSSSSS");
            invalidate();
        }
    }


    //contentView的原始点
    private Point mAutoBackOriginPos = new Point();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mMainContent.layout(left, mRange, right, bottom + mRange);

        mAutoBackOriginPos.x = mMainContent.getLeft();
        mAutoBackOriginPos.y = mMainContent.getTop();
    }

    //交付事件处理
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isTop){
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    //当ViewGroup填充完成时调用 得到View对象
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitleView = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);


        if (getChildCount() < 2) {
            throw new IllegalStateException("子View至少两个");
        }

        if (!(mTitleView instanceof ViewGroup) || !(mMainContent instanceof ViewGroup)) {
            throw new IllegalStateException("子View必须是ViewGroup的子类！");
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //标题高
        mTitleViewHeight = mTitleView.getMeasuredHeight();
        //内容布局高
        mMainContentHeight = mMainContent.getMeasuredHeight();

        //宽
        mWidth = mTitleView.getMeasuredWidth();

        //移动范围
        mRange = (int) (mTitleViewHeight * 0.8);
    }
}
