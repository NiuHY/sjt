package demo.xxx.cn.mydemo.demo0;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/14.
 */
public class YearUtils {

    //是否可以执行设置listView位置的方法，在每次获取ListView时初始化一次
    private static boolean isExecute = false;

    /**
     * 代码
     *  1 初始化ListView
     *  2 设置适配器
     *      getCount返回足够大的年份
     *      getView
     *          初始化一个TextView(ListView条目)
     *          TextView设置内容就是position(ListView的位置)
     *  3 ListView首先显示的位置设置(根据当前年份)
     *  4 得到返回的ListView 你可以用把他放到popupWindow中  popupWindow.setContentView( 这里 )
     *
     * @param context
     * @return 选择年份的ListView
     */
    public static ListView getYearsListView(final Context context){

        final ListView listView = new ListView(context);//初始化ListView  你可以替换自己的View.inflate()
        isExecute = true;

        //设置适配器
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 30 + 1 + 30;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

//                position = (Calendar.getInstance().get(Calendar.YEAR) - 2000) - position;

                if (convertView == null) {
                    convertView = new TextView(context);//初始化条目 TextView 替换自己的布局 View.inflate()

                    ((TextView) convertView).setGravity(Gravity.CENTER);

                    ((TextView) convertView).setPadding(0, 20, 0, 20);
                }
                //从0开始
                ((TextView) convertView).setText(position + Calendar.getInstance().get(Calendar.YEAR) - 30 + "");// 从2000年开始 到2100年结束

                return convertView;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }
        });


        //在这里得到 显示的第一个条目和最后一个条目位置
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //得到显示出来的条目数 应该+1才合适
                int itemCount = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition() +1;

                //得到后去设置
                setListViewSelectionItem(listView, itemCount);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    //不可信
                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });


        return listView;
    }

    /**
     * 给ListView选择位置，只执行一次
     * @param listView
     * @param itemCount
     */
    private static void setListViewSelectionItem(ListView listView, int itemCount) {
        if (isExecute){
            synchronized (YearUtils.class) {
                if (isExecute) {
                    //得到当前的年份，选择listView 中对应条目的位置
                    listView.setSelection(31 - itemCount / 2);
                    isExecute = false;
                }
            }

        }
    }
}
