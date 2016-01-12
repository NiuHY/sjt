package com.cmm.worldartapk.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SearchTabView extends LinearLayout {

    private Context context;

    //TextView 集合
    private List<TabItem> tabItems;
    //tab条目对象集合
    public class TabItem{
        public TextView tv_count; //计数的TextView
        public TextView tv_content; //内容TextView
    }

    public SearchTabView(Context context) {
        super(context);
        initView(context);
    }

    public SearchTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context) {
        this.context = context;

        tabItems = new ArrayList<>();


        String[] textArr = {"展讯", "艺术馆", "艺术品"};

        //添加三个TextView
        for (int i = 0; i < textArr.length; i++) {
            TextView textView = new TextView(UIUtils.getContext());
//            textView.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            textView.setText(textArr[i]);
            int padding = UIUtils.dip2Px(11);
            textView.setPadding(0, padding, 0, padding);
            textView.setTextColor(0xff999999);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.search_tab_selector);
            //默认第一个选中
            if (i == 0){
                textView.setSelected(true);
            }

            //这里TextView初始化完毕， 创建一个ViewGroup把TextView放进去，然后把ViewGroup放到当前布局
            FrameLayout viewGroup_item = new FrameLayout(UIUtils.getContext());
            viewGroup_item.setLayoutParams(new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            viewGroup_item.addView(textView);

//            // 添加一个计数 TextView
//            TextView tv_count = new TextView(UIUtils.getContext());
//            FrameLayout.LayoutParams tv_count_layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            tv_count_layoutParams.gravity = Gravity.BOTTOM + Gravity.RIGHT;
//            tv_count_layoutParams.rightMargin = UIUtils.dip2Px(3);
//            tv_count.setLayoutParams(tv_count_layoutParams);
//            tv_count.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
//            tv_count.setTextColor(0xffff0000);
//            viewGroup_item.addView(tv_count);

            addView(viewGroup_item); // 添加到布局

            // 把条目对应数据放到集合中
            TabItem tabItem = new TabItem();
            tabItem.tv_content = textView;
//            tabItem.tv_count = tv_count;//计数
            tabItems.add(tabItem); // 添加到选择集合

            // 设置点击回调
            textViewSetOnClick(textView);


            if (i < textArr.length-1){
                //如果不是最后一个，就添加分割线
                //分割线
                View fgx = new View(UIUtils.getContext());
                fgx.setLayoutParams(new LayoutParams(UIUtils.dip2Px(1)/2, ViewGroup.LayoutParams.MATCH_PARENT));
                fgx.setBackgroundColor(0xffbbbbbb);
                addView(fgx);
            }
        }
    }

    // 点击回调
    public interface OnItemClicklistener{
        public void onClick(View view, int index);
    }
    private OnItemClicklistener onItemClicklistener;
    public void setOnItemClicklistener(OnItemClicklistener onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;
    }

    // 默认选中
    private int selectedIndex = 0;

    private void textViewSetOnClick(final TextView textView) {
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                tabItems.get(selectedIndex).tv_content.setSelected(false);
                //改变状态
                v.setSelected(true);
                //更新 selectedIndex
                updateSelectedIndex(v);

                if (onItemClicklistener != null){
                    onItemClicklistener.onClick(v, selectedIndex);
                }
            }
        });
    }

    /**
     * 设置选择当前的条目
     * @param index
     */
    public void setCurrentSelectedItem(int index){
        tabItems.get(selectedIndex).tv_content.setSelected(false);
        //改变状态
        tabItems.get(index).tv_content.setSelected(true);

        selectedIndex = index;
    }

    /**
     * @return 返回TextView集合
     */
    public List<TabItem> getDataList(){
        return tabItems;
    }

    /**
     * 获取当前选中条目的索引
     * @return
     */
    public int getSelectedIndex(){
        return selectedIndex;
    }

    private void updateSelectedIndex(View view){
        //遍历查找
        for (int i = 0; i < tabItems.size(); i++) {
            if (tabItems.get(i).tv_content == view){
                selectedIndex = i;
                break;
            }
        }
    }

}
