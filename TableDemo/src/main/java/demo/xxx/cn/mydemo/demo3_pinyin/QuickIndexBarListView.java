package demo.xxx.cn.mydemo.demo3_pinyin;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demo.xxx.cn.mydemo.R;

/**
 * Created by Administrator on 2016/1/27.
 *
 * 创建一个该类对象，调用getListView方法得到一个带快速索引条的ListView
 */
public class QuickIndexBarListView extends FrameLayout {
    private Context context;
    private ListView lv;
    private TextView tvLetter;
    private QuickIndexBar quickIndexBar;
    private List<NameItem> nameItems;
    private List<String> data;
    private Handler handler;
    private MyAdapter myAdapter;
    private Handler handler_bar;
    private View barParent;

    public QuickIndexBarListView(Context context, List<String> data) {
        super(context);

        this.data = data;
        initView(context);
    }

    public QuickIndexBarListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    public QuickIndexBarListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    /**
     * 初始化
     * @param context
     */
    private void initView(Context context){
        this.context = context;
        handler = new Handler();

        //隐藏相关的handler
        handler_bar = new Handler();

        //关联布局
        View.inflate(context, R.layout.quick_listview, this);

        // ListView 提示框 索引条
        lv = (ListView) findViewById(R.id.lv);
        tvLetter = (TextView) findViewById(R.id.letter_tv);
        quickIndexBar = (QuickIndexBar) findViewById(R.id.quickindexbar);

        handler_bar.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideBar();
            }
        }, 2000L);

        //设置监听
        quickIndexBar.setOnSelectLetterListener(new QuickIndexBar.OnSelectLetterListener() {
            @Override
            public void selectLetter(String letter) {

                //标记
                boolean flag = false;
                //查找当前选中的字母在集合中对应对象第一次出现的位置，设置ListView跳转到这个位置
                for (int i = 0; i < nameItems.size(); i++) {
                    String l = "" + nameItems.get(i).getPinyin().charAt(0);
                    if (TextUtils.equals(l, letter)) {
                        //相同，改变位置，跳出循环
//                        lv.smoothScrollToPosition(i);//平滑滚动到指定位置
                        lv.setSelection(i);//选择
                        flag = true;
                        break;
                    }
                }
                //能找到就提示，找不到就提示 无
                if (flag) {
                    showLetterInfo(letter);//显示提示
                } else {
                    showLetterInfo("无");
                }
            }
        });

        // 快速索引条的隐藏
        barParent = ((View)quickIndexBar.getParent());
        barParent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //如果接受到了触摸事件，就显示出来然后2秒后再隐藏，


                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        //先清空消息
                        handler_bar.removeCallbacksAndMessages(null);
                        //显示
                        if (quickIndexBar.getVisibility() != VISIBLE){
                            showBar();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        event.offsetLocation(0, -(barParent.getMeasuredHeight() - quickIndexBar.getMeasuredHeight()) / 2);
                        quickIndexBar.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_UP:

                        handler_bar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // 隐藏
                                hideBar();
                            }
                        }, 2000L);

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("NIU", scrollState+"");
                //如果滚动 就显示
                //1 滚动
                if (scrollState == 1 && quickIndexBar.getVisibility() == GONE){

                    //先清空消息
                    handler_bar.removeCallbacksAndMessages(null);
                    //显示
                    if (quickIndexBar.getVisibility() != VISIBLE){
                        showBar();
                    }
                }else {
                    //
                    handler_bar.removeCallbacksAndMessages(null);
                    handler_bar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 隐藏
                            hideBar();
                        }
                    }, 2000L);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        //初始化集合
        initList();

        //给ListView设置适配器
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);
    }


    //是否开启隐藏状态
    private boolean hideState = false;

    /**
     * 设置是否开启自动隐藏
     * @param hideState
     */
    public void setHideState(boolean hideState) {
        this.hideState = hideState;
    }

    /**
     * 隐藏
     */
    private void hideBar(){

        if (hideState){
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(150L);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    quickIndexBar.setVisibility(GONE);
                    quickIndexBar.clearColor();
                    barParent.setAlpha(0.6f);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            quickIndexBar.startAnimation(translateAnimation);
        }

    }

    /**
     * 显示
     */
    private void showBar(){
        if (hideState){
            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(150L);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    quickIndexBar.setVisibility(VISIBLE);
                    barParent.setAlpha(1f);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            quickIndexBar.startAnimation(translateAnimation);
        }

    }

    /**
     * 设置数据集合并且更新
     * @param data 数据集合
     */
    public void setData(List<String> data){
        this.data = data;
        initList();//重置数据
        if (myAdapter != null){
            myAdapter.notifyDataSetChanged();
        }else {
            myAdapter = new MyAdapter();
            lv.setAdapter(myAdapter);
        }
    }

    /**
     * 设置ListView条目的点击监听
     * @param onItemClickListener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        if (lv != null){
            lv.setOnItemClickListener(onItemClickListener);
        }else {
            Log.e("eeeeeee", "没有初始化???");
        }
    }

    /**
     * 获取ListView
     * @return
     */
    public ListView getContentListView(){

        return lv;
    }

    /**
     * 获取拼音名字对象集合
     * @return
     */
    public List<NameItem> getNameItems(){
        return nameItems;
    }


    /**
     * 刷新数据
     */
    public void notifyDataSetChanged(){
        if (myAdapter != null){
            myAdapter.notifyDataSetChanged();
        }else {
            myAdapter = new MyAdapter();
            lv.setAdapter(myAdapter);
        }
    }

    /**
     * 初始化集合，把集合转换成带拼音的对象集合
     */
    private void initList() {
        if (nameItems != null){
            nameItems.clear();
        }else {
            nameItems = new ArrayList<NameItem>();
        }

        if (data != null){
            for (int i = 0; i < data.size(); i++) {
                nameItems.add(new NameItem(data.get(i)));
            }
        }
        //排序
        Collections.sort(nameItems);
    }


    /**
     * ListView适配器
     */
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nameItems.size();
        }

        /**
         * ViewHolder
         */
        private class ViewHolder{
            TextView itemLetter;
            TextView itemName;

            public ViewHolder(TextView itemLetter, TextView itemName) {
                this.itemLetter = itemLetter;
                this.itemName = itemName;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.lv_item, null);
                //字母和名字
                viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.item_letter), (TextView) convertView.findViewById(R.id.item_name));
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            NameItem td = nameItems.get(position);
            //获取首字母
            String currentLetter = "" + td.getPinyin().charAt(0);
            //第一条直接显示，不是第一条就判断
            if (position != 0) {

                //判断前一个条目的首字母和当前条目是否一样，一样就设置不显示itemLetter
                Boolean bool = TextUtils.equals(currentLetter, "" + nameItems.get(position - 1).getPinyin().charAt(0));
                viewHolder.itemLetter.setVisibility(bool ? View.GONE : View.VISIBLE);
            }else{
                viewHolder.itemLetter.setVisibility(View.VISIBLE);
            }
            //不变的
            //字母
            viewHolder.itemLetter.setText(currentLetter);
            //设置姓名
            viewHolder.itemName.setText(td.getName());

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return nameItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    /**
     * 在屏幕中间显示字母提示
     * @param letter 提示的文本
     */
    private void showLetterInfo(String letter) {
        //显示tvLetter，设置文本，延时2s后消失
        tvLetter.setText(letter);
        tvLetter.setVisibility(View.VISIBLE);

        //在发送延时方法时，先清空handler
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvLetter.setVisibility(View.INVISIBLE);
            }
        }, 2000L);
    }
}
