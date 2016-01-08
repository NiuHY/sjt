package com.cmm.worldartapk.activity;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.bean.SearchTest;
import com.cmm.worldartapk.net_volley_netroid.Netroid;
import com.cmm.worldartapk.ui.ExtendedViewPager;
import com.cmm.worldartapk.ui.TouchImageView;
import com.cmm.worldartapk.utils.UIUtils;
import com.duowan.mobile.netroid.image.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class TestActivity extends BaseActivity {

    private GridView gridView;
    private SearchTest searchTest;
    private List<String> imagePathList;

    @Override
    protected View getContentView() {
        View contentView = View.inflate(UIUtils.getContext(), R.layout.test_layout, null);

        TouchImageView touchImageView = new TouchImageView(this);

        return touchImageView;
    }

    @Override
    protected void initView() {

        ExtendedViewPager evp = (ExtendedViewPager) findViewById(R.id.evp);
        evp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imagePathList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TouchImageView touchImageView = new TouchImageView(UIUtils.getContext());
                Netroid.displayImage(imagePathList.get(position), touchImageView);
                container.addView(touchImageView);
                return touchImageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });


//        final TextView textView = (TextView) findViewById(R.id.test_tv);
//        gridView = (GridView) findViewById(R.id.test_gv);
//
//
//        // 请求JSON数据
//        NetUtils.getDataByNet(this, Const.BASE_URL, RequestMapData.setSearchParams("0", "1", "xx"), new SearchTestParser(), new MyNetWorkObject.SuccessListener() {
//            @Override
//            public void onSuccess(Object data) {
//                searchTest = ((SearchTest) data);
//                // 请求成功加载数据
//                gridView.setAdapter(new MyAdapter());
//            }
//        });

    }

    @Override
    protected void init() {

        //初始化图片路径集合
        imagePathList = new ArrayList<String>();
        for (int i = 0; i <= 12; i++) {
            imagePathList.add("assets://test/img" + i + ".jpg");
        }

    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return searchTest.data.size();
        }

        @Override
        public Object getItem(int position) {
            return searchTest.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(UIUtils.getContext(), R.layout.test_item, null);
            }
            NetworkImageView networkImageView = (NetworkImageView) convertView.findViewById(R.id.test_item_niv);
            networkImageView.setImageUrl(searchTest.data.get(position).exhibition_image, Netroid.getmImageLoader());
            networkImageView.setDefaultImageResId(R.drawable.ic_error_page);
            return convertView;
        }
    }

    // GridView的Adapter
//    private class MyAdapter extends MyBaseAdapter<SearchTest.SearchData> {
//
//        private NetworkImageView imageView;
//        private View view;
//
//        public MyAdapter(List<SearchTest.SearchData> data) {
//            super(data);
//        }
//
//        @Override
//        public View initView() {
//            imageView = new NetworkImageView(UIUtils.getContext());
////            view = View.inflate(UIUtils.getContext(), R.layout.test_item, null);
////            imageView = (NetworkImageView) view.findViewById(R.id.test_item_niv);
//            return imageView;
//        }
//
//        @Override
//        public void setData(View view, SearchTest.SearchData itemData) {
////            this.view = view;
//            this.imageView.setImageUrl(itemData.exhibition_image, Netroid.getmImageLoader());
//        }
//
//        @Override
//        protected List onLoadmore() {
//            return null;
//        }
//    }



}
