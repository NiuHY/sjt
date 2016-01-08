package com.cmm.worldartapk.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import com.cmm.worldartapk.R;
import com.cmm.worldartapk.utils.ThreadManager;
import com.cmm.worldartapk.utils.UIUtils;

import java.util.List;


/**
 * Created by Administrator on 2015/11/10.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    /**
     * 初始Adapter时的List数据
     */
    private List<T> data;

    public MyBaseAdapter(List<T> data) {
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size() + 1;//+1加载更多条目
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //内部类对象  holder
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (MORE_VIEW_TYPE == getItemViewType(position)) {
                //初始化MoreView
                initMoreView(hasmore());
                convertView = moreView;
            } else {
                convertView = initView();
                convertView.setSelected(true);
            }
        }
        //给当前对应条目设置数据
        if (ITEM_VIEW_TYPE == getItemViewType(position)) {
            setData(convertView, data.get(position));
        }
        return convertView;
    }

    /**
     * 在无法复用时 给ViewHolder设置条目视图
     */
    public abstract View initView();

    /**
     * 可以复用时给当前条目视图对象设置数据
     *
     * @param view     当前条目视图对象
     * @param itemData view对应的数据对象
     */
    public abstract void setData(View view, T itemData);

    // 加载普通的数据类型
    private final int ITEM_VIEW_TYPE = 0;
    // 加载更多的数据类型
    private final int MORE_VIEW_TYPE = 1;

    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {//最后一条加载更多
            return MORE_VIEW_TYPE;
        } else {
            return ITEM_VIEW_TYPE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;//加上多出的类型
    }

    // 表示有更多的数据类型
    public static final int HAS_MORE = 1;
    // 表示没有更多的数据类型
    public static final int NO_MORE = 2;
    // 表示错误的数据类型
    public static final int ERROR = 3;

    /**
     * 是否需要加载更多，子类不需要加载更多时重写
     */
    protected boolean hasmore() {
        return true;
    }

    private View moreView;  //一个Adapter类对应一个MoreView
    private int More_VIEW_STATE = 1;  //MoreView的状态，默认可以加载更多，每次加载后修改，通过判断它的值显示相应的布局
    private RelativeLayout rl_more_loading;  //加载中
    private RelativeLayout rl_more_error;  //加载失败
    private RelativeLayout rl_more_nomore;  //没有更多数据

    /**
     * 每次刷新数据都要调用，但只有第一次才会进行初始操作，之后只是判断是否要加载数据
     */
    private void initMoreView(boolean hasmore) {
        //只有一条对象，第一次才初始化
        if (moreView == null) {
            //初始化moreView
            moreView = UIUtils.inflate(R.layout.list_more_loading);
            // 加载数据的view
            rl_more_loading = (RelativeLayout) moreView.findViewById(R.id.rl_more_loading);
            // 加载失败的view
            rl_more_error = (RelativeLayout) moreView.findViewById(R.id.rl_more_error);
            // 没有更多的view
            rl_more_nomore = (RelativeLayout) moreView.findViewById(R.id.rl_more_nomore);
            //初始化状态(加载更多或者没有更多)
            int tempState = hasmore ? HAS_MORE : NO_MORE;
            //更新状态
            refreshMoreView(tempState);
        }

        // 判断当前有没有更多数据
        if (More_VIEW_STATE == HAS_MORE) {
            loadMore();
        }
    }

    /**
     * 刷新更多条目的状态，显示不同的视图，在初始和每次更改moreViewState后调用
     *
     * @param moreViewState 更多条目的状态 (加载更多，加载失败，没有更多)
     */
    private void refreshMoreView(int moreViewState) {
        //通过判断 moreViewState的值，来显示相应的布局
        rl_more_loading.setVisibility(moreViewState == HAS_MORE ? View.VISIBLE : View.GONE);
        rl_more_error.setVisibility(moreViewState == ERROR ? View.VISIBLE : View.GONE);
        rl_more_nomore.setVisibility(moreViewState == NO_MORE ? View.VISIBLE : View.GONE);
    }

    /**
     * 加载更多的数据
     */
    public boolean is_loading = false;

    public void loadMore() {
        // 开启子线程去加载服务器发送过来的数据
        if (!is_loading) {
            is_loading = true;
            ThreadManager.getLongPool().execute(new Runnable() {

                @Override
                public void run() {
                    final List list = onLoadmore();
                    UIUtils.runInMainThread(new Runnable() {

                        @Override
                        public void run() {
                            if (null == list) {
                                refreshMoreView(More_VIEW_STATE = ERROR);
                            } else if (list.size() < 20) {
                                refreshMoreView(More_VIEW_STATE = NO_MORE);
                            } else {
                                refreshMoreView(More_VIEW_STATE = HAS_MORE);
                            }

                            // 把从服务器返回的数据追加到之前的集合里面
                            if (list != null) {
                                if (data != null) {
                                    data.addAll(list);
                                } else {
                                    data = list;
                                }
                            }
                            //刷新数据
                            notifyDataSetChanged();
                            is_loading = false;
                        }
                    });
                }
            });
        }
    }

    /**
     * 给子类重写的加载更多的方法
     */
    protected abstract List onLoadmore();
}