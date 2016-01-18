package demo.xxx.cn.mydemo.demo_recycleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import demo.xxx.cn.mydemo.R;

/**
 * Created by Administrator on 2016/1/14.
 */
public class RecyclerViewTest {

    /**
     * 创建一个RecyclerView
     * @param context
     * @return
     */
    public static RecyclerView createRecyclerView(final Context context){

        //得到 RecyclerView
        RecyclerView mRecyclerViewTest = (RecyclerView) View.inflate(context, R.layout.recycleview_activity, null);
        //设置布局管理器 (线性)
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); //水平
//        mRecyclerViewTest.setLayoutManager(linearLayoutManager);
//        //添加分割线 垂直
//        mRecyclerViewTest.addItemDecoration(new RecyclerViewDividerLinearItemDecoration(context, LinearLayoutManager.HORIZONTAL));

        //设置布局管理器 (格子)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerViewTest.setLayoutManager(gridLayoutManager);
        //添加分割线
        mRecyclerViewTest.addItemDecoration(new RecyclerViewDividerGridItemDecoration(context));

        //设置数据适配器
        mRecyclerViewTest.setAdapter(new MyRecyclerViewAdapter(context));

        return mRecyclerViewTest;
    }


    /**
     * RecyclerView的 适配器
     */
    private static class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>{

        private Context context;

        public MyRecyclerViewAdapter(Context context) {
            this.context = context;
        }

        //创建ViewHolder绑定条目
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(context, R.layout.recyclerview_item_linear, null));
        }

        //条目数据绑定
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(((int)(Math.random()*100))+"");
        }

        //条目个数
        @Override
        public int getItemCount() {
            return 100;
        }

        //继承RecyclerView.ViewHolder的ViewHolder类
        public static class ViewHolder extends RecyclerView.ViewHolder {

            //条目
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.linear_tv);
            }
        }
    }
}
