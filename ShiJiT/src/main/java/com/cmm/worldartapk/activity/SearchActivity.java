package com.cmm.worldartapk.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmm.worldartapk.R;
import com.cmm.worldartapk.SafeWebViewBridge.js.ConstJS_F;
import com.cmm.worldartapk.base.BaseActivity;
import com.cmm.worldartapk.bean.SearchBean_Artwork;
import com.cmm.worldartapk.bean.SearchBean_Exhibition;
import com.cmm.worldartapk.bean.SearchBean_Gallery;
import com.cmm.worldartapk.bean.parser.SearchArtworkParser;
import com.cmm.worldartapk.bean.parser.SearchExhibitionParser;
import com.cmm.worldartapk.bean.parser.SearchGalleryParser;
import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.Netroid;
import com.cmm.worldartapk.net_volley_netroid.net_2.MyNetWorkObject;
import com.cmm.worldartapk.net_volley_netroid.net_2.NetUtils;
import com.cmm.worldartapk.net_volley_netroid.net_2.RequestMapData;
import com.cmm.worldartapk.ui.SearchTabView;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.UIUtils;
import com.duowan.mobile.netroid.image.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class SearchActivity extends BaseActivity {

    /**
     * 展览搜索
     */
    private final String EXHIBITION_URL = "api/search/exhibition/";
    private final String GALLERY_URL = "api/search/gallery/";
    private final String ARTWORK_URL = "api/search/artwork/";

    private SearchBean_Exhibition exhibitionData; // 展览搜索结果
    private SearchBean_Gallery galleryData; // 艺术馆搜索结果
    private SearchBean_Artwork artworkData; // 艺术馆搜索结果

    private FrameLayout frameLayout; // 搜索内容 容器
    private EditText editText; // 文本编辑框
    private TextView textView; // 搜索按钮

    //搜索按钮判断，true是搜索按钮
    private boolean isSearchButton = false;
    private View searchContentView;
    private List<SearchTabView.TabItem> pagerList;
    private List<String> imagePathList;
    private GridViewAdapter mGridViewAdapter_e;
    private GridViewAdapter mGridViewAdapter_g;
    private GridViewAdapter mGridViewAdapter_a;
    private ViewPager viewPager;
    private View loadingPagerView;
    private InputMethodManager imm;
    private String searchKey;
    private int limit;
    private List<PullToRefreshGridView> searchGridView_ptrs;

    @Override
    protected void init() {

    }

    @Override
    protected View getContentView() {
        View contentView = View.inflate(UIUtils.getContext(), R.layout.search_activity, null);

        return contentView;
    }

    @Override
    protected void initView() {
        //软键盘 控制
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        //文本编辑框，搜索按钮，frameLayout
        editText = (EditText) findViewById(R.id.search_et);
        textView = (TextView) findViewById(R.id.search_tv);
        frameLayout = (FrameLayout) findViewById(R.id.search_fl);

        // 给文本编辑框设置变化监听， 当有内容改变时，设置搜索按钮显示为 “搜索”

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当内容改变后，启用搜索按钮
                if (!isSearchButton) {
                    textView.setText("搜索");
                    isSearchButton = true;
                }
                //如果没内容就重置为取消按钮
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    textView.setText("取消");
                    isSearchButton = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当软键盘按下搜索时，相当于点击搜索按钮
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(editText.getText().toString().trim())) {
                    searchButtonClick();
                }
                return false;
            }
        });

        //textView 搜索按钮的点击
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取textView 的文本内容，如果是搜索，就响应 搜索，如果是取消，就响应取消
                if (textView.getText().toString().trim().equals("取消")) {
                    //取消，关闭搜索页
                    finish();
                } else {
                    searchButtonClick();
                }
            }
        });
    }


    /**
     * 搜索按钮点击
     */
    private void searchButtonClick() {

        /**  点击搜索  */
        //把搜索变成取消 重置标记
        textView.setText("取消");
        isSearchButton = false;

        //清空搜索集合
        mGridViewDatas_e.clear();//展览集合
        mGridViewDatas_g.clear();//艺术馆集合
        mGridViewDatas_a.clear();//艺术品集合

        //重置切换
        isSetCurrentItem = false;

        //隐藏软键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        // 搜索关键字
        try {
            searchKey = URLEncoder.encode(editText.getText().toString().trim(), "UTF-8") + "/";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // 搜索内容
        if (searchContentView == null) {
            // 先把原来的背景图片清除
            frameLayout.removeAllViews();
            //放入搜索内容布局，一个tab一个ViewPager
            searchContentView = View.inflate(UIUtils.getContext(), R.layout.search_content_item, null);
            // 初始化搜索结果页面
            initSearchContentView();
            frameLayout.addView(searchContentView);
        }

        //点击搜索后 先显示 加载中
        if (loadingPagerView == null) {
            loadingPagerView = View.inflate(UIUtils.getContext(), R.layout.loading_layout, null);

            frameLayout.addView(loadingPagerView);

            View loadingAnim = loadingPagerView.findViewById(R.id.loading_anmi);
            //旋转动画
            RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1234L);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setInterpolator(new LinearInterpolator());

            //开启动画
            loadingAnim.startAnimation(rotateAnimation);

            loadingPagerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        loadingPagerView.setVisibility(View.VISIBLE);

        //初始化 limit
        limit = 0;

        //展览
        searchM_exhibition();
        //艺术馆
        searchM_gallery();
        //艺术品
        searchM_artwork();


    }

    //谁先搜索到结果
    private int currentItemByResult = 0;
    private void searchM_artwork() {
        //艺术品
        NetUtils.getDataByNet(SearchActivity.this, Const.BASE_URL + ARTWORK_URL + searchKey, RequestMapData.setSearchParams(limit + "", "12"), new SearchArtworkParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                artworkData = (SearchBean_Artwork) data;
                //得到数据，刷新  如果请求到数据
                if (artworkData != null && artworkData.success.equals("1")) {

                    for (SearchBean_Artwork.ArtworkData sba : artworkData.data) {
                        mGridViewDatas_a.add(new GridViewData(sba.image_url, sba.artwork_name, sba.artwork_id, sba.artwork_artist_name));
                    }
                    //得到数据，limit+1；
                    limit++;

                    currentItemByResult = 2;
                }
                // 刷新
                mGridViewAdapter_a.notifyDataSetChanged();
                updateTabItem_count();//更新计数
            }

            @Override
            public void onError(String msg) {

                LogUtils.e(msg);

                //请求失败，隐藏
                updateTabItem_count();//更新计数
            }
        });
    }

    private void searchM_gallery() {
        //艺术馆
        NetUtils.getDataByNet(SearchActivity.this, Const.BASE_URL + GALLERY_URL + searchKey, RequestMapData.setSearchParams(limit + "", "12"), new SearchGalleryParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                galleryData = (SearchBean_Gallery) data;
                //得到数据，刷新  如果请求到数据
                if (galleryData != null && galleryData.success.equals("1")) {

                    for (SearchBean_Gallery.GalleryData sbg : galleryData.data) {
                        mGridViewDatas_g.add(new GridViewData(sbg.gallery_cover, sbg.gallery_name, sbg.gallery_id, sbg.gallery_description));
                    }
                    limit++;

                    currentItemByResult = 1;
                }
                // 刷新
                mGridViewAdapter_g.notifyDataSetChanged();
                updateTabItem_count();//更新计数
            }

            @Override
            public void onError(String msg) {

                LogUtils.e(msg);

                //请求失败，隐藏
                updateTabItem_count();//更新计数
            }
        });
    }

    private void searchM_exhibition() {
        //请求数据，获取搜索结果后填充搜索页  展览搜索
        NetUtils.getDataByNet(SearchActivity.this, Const.BASE_URL + EXHIBITION_URL + searchKey, RequestMapData.setSearchParams(limit + "", "12"), new SearchExhibitionParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                exhibitionData = (SearchBean_Exhibition) data;
                //刷新适配器  如果请求到数据
                if (exhibitionData != null && exhibitionData.success.equals("1")) {

                    for (SearchBean_Exhibition.Search_E_Data sed : exhibitionData.data) {
//                        LogUtils.e(sed.toString());
                        mGridViewDatas_e.add(new GridViewData(sed.exhibition_cover, sed.exhibition_title, sed.exhibition_id, ""));
                    }
                    limit++;

                    currentItemByResult = 0;
                }
                // 刷新
                mGridViewAdapter_e.notifyDataSetChanged();
                updateTabItem_count();//更新计数
            }

            @Override
            public void onError(String msg) {
                //请求失败，隐藏
                updateTabItem_count();//更新计数
            }
        });
    }


//    private boolean lock1 = false;
//    private boolean lock2 = false;
//    // 加载数据 当两个请求的数据都得到时
//    private void initSearchData(){
//        if (lock1 && lock2){
//            initSearchContentView();
//        }
//    }

    /**
     * 初始化搜索内容页
     */
    private void initSearchContentView() {

//        //c 测试图片路径集合
//        imagePathList = new ArrayList<String>();
//        for (int i = 0; i <= 12; i++) {
//            imagePathList.add("assets://test/img" + 1 + ".jpg");
//        }


        //tab  ViewPager
        final SearchTabView searchTabView = (SearchTabView) searchContentView.findViewById(R.id.search_content_searchTab);
        viewPager = (ViewPager) searchContentView.findViewById(R.id.search_content_vp);

        searchTabView.setOnItemClicklistener(new SearchTabView.OnItemClicklistener() {
            @Override
            public void onClick(View view, int index) {
                viewPager.setCurrentItem(index);//选择页面
            }
        });

        pagerList = searchTabView.getDataList();

        //设置 右下角 数量
        updateTabItem_count();

        //全部加载
        viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(new MyVPAdapter());
        //设置选择页面监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                searchTabView.setCurrentSelectedItem(position);
                searchPagePosition = position;//记录当前页
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //当前搜索页是那一页 0, 1, 2
    private int searchPagePosition = 0;

    //在搜索后是否设置了一次切换
    private boolean isSetCurrentItem = false;
    /**
     * 更新计数
     */
    private void updateTabItem_count() {
        if (!isSetCurrentItem && currentItemByResult != 0){
            viewPager.setCurrentItem(currentItemByResult);
            isSetCurrentItem = true;
        }


//        LogUtils.e(pagerList.get(0).tv_count.toString());
        if (mGridViewDatas_e == null) {
            mGridViewDatas_e = new ArrayList<>();
        }
        if (mGridViewDatas_g == null) {
            mGridViewDatas_g = new ArrayList<>();
        }
        if (mGridViewDatas_a == null) {
            mGridViewDatas_a = new ArrayList<>();
        }

        pagerList.get(0).tv_count.setText(mGridViewDatas_e.size() + "");
        pagerList.get(1).tv_count.setText(mGridViewDatas_g.size() + "");
//        pagerList.get(2).tv_count.setText(imagePathList.size() + "");
        pagerList.get(2).tv_count.setText(mGridViewDatas_a.size() + "");

        //在有数据请求到时就 隐藏加载中
        if (loadingPagerView != null) {
            loadingPagerView.setVisibility(View.GONE);
        }
        //如果在刷新就刷新
        PullToRefreshGridView pullToRefreshGridView = null;
        if (searchGridView_ptrs != null){
            pullToRefreshGridView = searchGridView_ptrs.get(searchPagePosition);
        }

        if (pullToRefreshGridView != null && pullToRefreshGridView.isRefreshing()){
            pullToRefreshGridView.onRefreshComplete();
        }
    }

    /**
     * ViewPager Adapter
     */
    private class MyVPAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            FrameLayout page = new FrameLayout(UIUtils.getContext());

            // 搜索结果 GridView
            //三个 PullToRefreshGridView 放到一个集合中
            if (searchGridView_ptrs == null){
                searchGridView_ptrs = new ArrayList<>();
            }
            PullToRefreshGridView pullToRefreshGridView = new PullToRefreshGridView(UIUtils.getContext());

            //添加集合
            searchGridView_ptrs.add(position, pullToRefreshGridView);

            pullToRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

            pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
                @Override
                public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                    switch (position) {
                        case 0:
                            searchM_exhibition();
                            break;
                        case 1:
                            searchM_gallery();
                            break;
                        case 2:
                            searchM_artwork();
                            break;
                        default:
                            break;
                    }
                }
            });

            final GridView gridView = pullToRefreshGridView.getRefreshableView();
//             = new GridView(UIUtils.getContext());
            // 添加到 ViewPager 中
            page.addView(pullToRefreshGridView);

            gridView.setNumColumns(2);// 两行
            gridView.setHorizontalSpacing(UIUtils.dip2Px(5));// 列间距
            gridView.setVerticalSpacing(UIUtils.dip2Px(10));// 行间距

            //gridView 空数据显示视图
            View emptyPager = View.inflate(UIUtils.getContext(), R.layout.empty_pager, null);
            emptyPager.setVisibility(View.GONE);
            ((ViewGroup)gridView.getParent()).addView(emptyPager);// 把空页面 和 GridView放到一起
            gridView.setEmptyView(emptyPager);

            //========================== 给GridView设置内容 ===========================
            switch (position) {
                case 0:

//                    gridView.setAdapter(new GridViewAdapter_Test());

                    // 第一页内容 展览
                    if (mGridViewAdapter_e == null) {
                        mGridViewAdapter_e = new GridViewAdapter(mGridViewDatas_e);
                    }
                    gridView.setAdapter(mGridViewAdapter_e);
                    break;
                case 1:

//                    gridView.setAdapter(new GridViewAdapter_Test());

                    // 第二页内容 艺术馆
                    if (mGridViewAdapter_g == null) {
                        mGridViewAdapter_g = new GridViewAdapter(mGridViewDatas_g);
                    }
                    gridView.setAdapter(mGridViewAdapter_g);
                    break;
                case 2:

                    // 第三页内容 艺术品
                    if (mGridViewAdapter_a == null) {
                        mGridViewAdapter_a = new GridViewAdapter(mGridViewDatas_a);
                    }
                    gridView.setAdapter(mGridViewAdapter_a);
                    break;
                default:
                    break;
            }

            //========================== 给GridView设置内容 ===========================

            // 给GridView设置条目点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                    if (searchPagePosition != 2) {
                        Intent intent = new Intent(SearchActivity.this, DetailPageActivity.class);

                        // TODO 把要打开的详情页的 ID存起来
                        if (searchPagePosition == 0) {
                            intent.putExtra("loadCategory", 2);//position 分类 0+1 -- 2+1
                            LogUtils.e(mGridViewDatas_e.get(i).toString());
                            ConstJS_F.detailId = mGridViewDatas_e.get(i).id;
                        } else if (searchPagePosition == 1) {
                            intent.putExtra("loadCategory", 3);//position 分类 0+1 -- 2+1
                            ConstJS_F.detailId = mGridViewDatas_g.get(i).id;
                        }

                        LogUtils.e(ConstJS_F.detailId);

                        UIUtils.startActivity(intent);
                    } else {
                        //是艺术品页，点击预览
                        //预览的方法 ViewPager
                        showVPWindow(mGridViewDatas_a, i);
                    }

                }
            });

            container.addView(page);

            return page;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 图片预览的方法
     *
     * @param mGridViewDatas_a
     * @param index
     */
    private void showVPWindow(List<GridViewData> mGridViewDatas_a, int index) {
        UIUtils.showToastSafe(index + "");
    }


    // 是否停止加载图片的标记
//    private boolean isLoadImage = false;

    /**
     * GridViewHolder
     */
    private class GridViewHolder {
        public NetworkImageView networkImageView;
        public TextView textView;

        public GridViewHolder(View view) {
            this.networkImageView = (NetworkImageView) view.findViewById(R.id.search_content_networkimageview);
            this.networkImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.textView = (TextView) view.findViewById(R.id.search_content_info);
            view.setTag(this);
        }
    }

    //对应集合
    private List<GridViewData> mGridViewDatas_e = new ArrayList<>();//展览
    private List<GridViewData> mGridViewDatas_g = new ArrayList<>();//艺术馆
    private List<GridViewData> mGridViewDatas_a = new ArrayList<>();//艺术品

    private class GridViewData {
        /**
         * @param imageUrl  图片
         * @param imageName 图片名
         * @param id        id
         * @param info      图片描述 艺术品有
         */
        public GridViewData(String imageUrl, String imageName, String id, String info) {
            this.imageUrl = imageUrl;
            this.imageName = imageName;
            this.id = id;
            this.info = info;
        }

        public GridViewData() {
        }

        // 填充的数据 图片封面，图片名称，有详情页的要有id，艺术品要用描述
        public String imageUrl;
        public String imageName;

        public String id;
        public String info;

        @Override
        public String toString() {
            return "GridViewData{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", imageName='" + imageName + '\'' +
                    ", id='" + id + '\'' +
                    ", info='" + info + '\'' +
                    '}';
        }
    }

    /**
     * GridView内容
     */
    private class GridViewAdapter extends BaseAdapter {

        private List<GridViewData> datas;

        /**
         * 需要对应的内容集合
         *
         * @param datas
         */
        public GridViewAdapter(List<GridViewData> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder gridViewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(UIUtils.getContext(), R.layout.item_gridview_searchcontent, null);
                gridViewHolder = new GridViewHolder(convertView);
            } else {
                gridViewHolder = (GridViewHolder) convertView.getTag();
            }

            // 设置图片
            Netroid.displayImage(datas.get(position).imageUrl, gridViewHolder.networkImageView);
            //设置图片信息
            try {
                gridViewHolder.textView.setText(URLDecoder.decode(datas.get(position).imageName, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
//
//    /**
//     * 测试 数据Adapter
//     */
//    private class GridViewAdapter_Test extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return imagePathList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//
//            return imagePathList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            GridViewHolder gridViewHolder = null;
//            if (convertView == null) {
//                convertView = View.inflate(UIUtils.getContext(), R.layout.item_gridview_searchcontent, null);
//                gridViewHolder = new GridViewHolder(convertView);
//            } else {
//                gridViewHolder = (GridViewHolder) convertView.getTag();
//            }
//
//            // 设置图片
//
//            Netroid.displayImage(imagePathList.get(position), gridViewHolder.networkImageView);
//            //设置图片信息
//            try {
//                gridViewHolder.textView.setText(URLDecoder.decode(imagePathList.get(position), "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//
//            return convertView;
//        }
//    }

}
