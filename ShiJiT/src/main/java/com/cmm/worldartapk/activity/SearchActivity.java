package com.cmm.worldartapk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.cmm.worldartapk.ui.ExtendedViewPager;
import com.cmm.worldartapk.ui.SearchTabView;
import com.cmm.worldartapk.ui.TouchImageView;
import com.cmm.worldartapk.utils.FileUtils;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.SJT_UI_Utils;
import com.cmm.worldartapk.utils.UIUtils;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.image.NetworkImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

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
    private GridViewAdapter mGridViewAdapter_e;
    private GridViewAdapter mGridViewAdapter_g;
    private GridViewAdapter mGridViewAdapter_a;
    private ViewPager viewPager;
    private View loadingPagerView;
    private InputMethodManager imm;
    private String searchKey;
    private List<PullToRefreshGridView> searchGridView_ptrs;
    private WindowManager windowManager;
    private View dfWindowView;
    private View vp_ll_btViewnGroup;
    private Button saveBT;
    private Button addBT;
    private Button cancelBT;
    private View imagevp_btngroup;
    private boolean isWindowViewShow;

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

        // 搜索关键字
        try {
            searchKey = URLEncoder.encode(editText.getText().toString().trim(), "UTF-8") + "/";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //把搜索变成取消 重置标记
        textView.setText("取消");
        isSearchButton = false;

        //清空搜索集合
        mGridViewDatas_e.clear();//展览集合
        mGridViewDatas_g.clear();//艺术馆集合
        mGridViewDatas_a.clear();//艺术品集合

        //初始化 limit
        limit_artwork = 0;
        limit_gallery = 0;
        limit_exhibition = 0;


        //隐藏软键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);




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

        //把ViewPager选中第一页 (默认)
        if (viewPager != null){
            viewPager.setCurrentItem(0);
        }

        //点击搜索后 初始化 加载中页面
        if (loadingPagerView == null) {
            loadingPagerView = View.inflate(UIUtils.getContext(), R.layout.loading_layout, null);

            ((ViewGroup)viewPager.getParent()).addView(loadingPagerView);

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
            loadingPagerView.setVisibility(View.GONE);
        }



        //开始搜索 默认第一个搜索展讯， 其他在切换到的时候才进行搜索

        //展览
        searchM_exhibition();

    }

    private int limit_artwork = 0;//limit
    private void searchM_artwork() {

        //显示正在加载中
        showLoadingAnim();

        //艺术品
        NetUtils.getDataByNet(SearchActivity.this, Const.BASE_URL + ARTWORK_URL + searchKey, RequestMapData.setSearchParams(limit_artwork + "", "12"), new SearchArtworkParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                artworkData = (SearchBean_Artwork) data;
                //得到数据，刷新  如果请求到数据
                if (artworkData != null && artworkData.success.equals("1")) {

                    for (SearchBean_Artwork.ArtworkData sba : artworkData.data) {
                        mGridViewDatas_a.add(new GridViewData(sba.image_url, sba.artwork_name, sba.artwork_id, sba.artwork_artist_name));
                    }
                    //得到数据，limit+1；
                    limit_artwork++;
                }
                // 刷新
                mGridViewAdapter_a.notifyDataSetChanged();
                hintLoadingAnim();//更新计数
            }

            @Override
            public void onError(String msg) {

                LogUtils.e(msg);

                //请求失败，隐藏
                hintLoadingAnim();//更新计数
            }
        });
    }

    private int limit_gallery = 0;
    private void searchM_gallery() {

        //显示正在加载中
        showLoadingAnim();

        //艺术馆
        NetUtils.getDataByNet(SearchActivity.this, Const.BASE_URL + GALLERY_URL + searchKey, RequestMapData.setSearchParams(limit_gallery + "", "12"), new SearchGalleryParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                galleryData = (SearchBean_Gallery) data;
                //得到数据，刷新  如果请求到数据
                if (galleryData != null && galleryData.success.equals("1")) {

                    for (SearchBean_Gallery.GalleryData sbg : galleryData.data) {
                        mGridViewDatas_g.add(new GridViewData(sbg.gallery_cover, sbg.gallery_name, sbg.gallery_id, sbg.gallery_description));
                    }
                    limit_gallery++;

                }
                // 刷新
                mGridViewAdapter_g.notifyDataSetChanged();
                hintLoadingAnim();//更新计数
            }

            @Override
            public void onError(String msg) {

                LogUtils.e(msg);

                //请求失败，隐藏
                hintLoadingAnim();//更新计数
            }
        });
    }

    private int limit_exhibition = 0;
    private void searchM_exhibition() {

        //显示正在加载中
        showLoadingAnim();

        //请求数据，获取搜索结果后填充搜索页  展览搜索
        NetUtils.getDataByNet(SearchActivity.this, Const.BASE_URL + EXHIBITION_URL + searchKey, RequestMapData.setSearchParams(limit_exhibition + "", "12"), new SearchExhibitionParser(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                exhibitionData = (SearchBean_Exhibition) data;
                //刷新适配器  如果请求到数据
                if (exhibitionData != null && exhibitionData.success.equals("1")) {

                    for (SearchBean_Exhibition.Search_E_Data sed : exhibitionData.data) {
//                        LogUtils.e(sed.toString());
                        mGridViewDatas_e.add(new GridViewData(sed.exhibition_cover, sed.exhibition_title, sed.exhibition_id, ""));
                    }
                    limit_exhibition++;
                }
                // 刷新
                mGridViewAdapter_e.notifyDataSetChanged();
                hintLoadingAnim();//更新计数
            }

            @Override
            public void onError(String msg) {
                //请求失败，隐藏
                hintLoadingAnim();//更新计数
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

                //如果没数据就去请求
                switch (position) {
                    case 0:
                        if (mGridViewDatas_e.isEmpty()) searchM_exhibition();
                        break;
                    case 1:
                        if (mGridViewDatas_g.isEmpty()) searchM_gallery();
                        break;
                    case 2:
                        if (mGridViewDatas_a.isEmpty()) searchM_artwork();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 当前搜索页是那一页 0, 1, 2
     */
    private int searchPagePosition = 0;

    /**
     * 显示加载中
     */
    private synchronized void showLoadingAnim(){
        //如果当前内容页是正在加载中的状态就不显示加载中
        //如果在刷新就结束刷新
        PullToRefreshGridView pullToRefreshGridView = null;
        if (searchGridView_ptrs != null){
            pullToRefreshGridView = searchGridView_ptrs.get(searchPagePosition);
        }

        if (pullToRefreshGridView == null || !pullToRefreshGridView.isRefreshing()){
//            显示加载中
            if (loadingPagerView != null) {
                loadingPagerView.setVisibility(View.VISIBLE);
            }
        }
////            显示加载中
//        if (loadingPagerView != null) {
//            loadingPagerView.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * 隐藏加载动画
     */
    private synchronized void hintLoadingAnim() {

            //隐藏加载中
            if (loadingPagerView != null) {
                loadingPagerView.setVisibility(View.GONE);
            }

//        LogUtils.e(pagerList.get(0).tv_count.toString());
//        if (mGridViewDatas_e == null) {
//            mGridViewDatas_e = new ArrayList<>();
//        }
//        if (mGridViewDatas_g == null) {
//            mGridViewDatas_g = new ArrayList<>();
//        }
//        if (mGridViewDatas_a == null) {
//            mGridViewDatas_a = new ArrayList<>();
//        }
//        pagerList.get(0).tv_count.setText(mGridViewDatas_e.size() + "");
//        pagerList.get(1).tv_count.setText(mGridViewDatas_g.size() + "");
////        pagerList.get(2).tv_count.setText(imagePathList.size() + "");
//        pagerList.get(2).tv_count.setText(mGridViewDatas_a.size() + "");


        //如果在刷新就结束刷新
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

//                        LogUtils.e(ConstJS_F.detailId);

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


    private long evpClickTime;//ViewPager 点击延时
    /**
     * 图片预览的方法
     *
     * @param mGridViewDatas_a
     * @param index
     */
    private void showVPWindow(final List<GridViewData> mGridViewDatas_a, final int index) {
//        UIUtils.showToastSafe(index + "");

        //添加一个窗口去预览图片
        //初始化窗体
        if(windowManager == null){
            windowManager = getWindowManager();
        }

        dfWindowView = View.inflate(UIUtils.getContext(), R.layout.imagepreview_window, null);


        //预览ViewPager
        ExtendedViewPager evp = (ExtendedViewPager) dfWindowView.findViewById(R.id.evp);

        //隐藏的保存收藏布局
        vp_ll_btViewnGroup = dfWindowView.findViewById(R.id.imagevp_btn_ll);

        //图片描述 信息和按钮
        final TextView imageInfo_tv = (TextView) dfWindowView.findViewById(R.id.imagepre_vp_imageinfo);
        final View imageInfo_bt = dfWindowView.findViewById(R.id.imagepre_vp_imageinfo_btn);
        //点击图片描述 隐藏它，显示按钮，点击按钮，隐藏它，显示图片描述，在切换页面时切换对应的图片描述
        imageInfo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo_tv.setVisibility(View.GONE);
                imageInfo_bt.setVisibility(View.VISIBLE);
            }
        });
        imageInfo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo_tv.setVisibility(View.VISIBLE);
                imageInfo_bt.setVisibility(View.GONE);
            }
        });
        //设置对应描述信息
        imageInfo_tv.setText(mGridViewDatas_a.get(index).info);
        //ViewPager切换页面改变信息
        evp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imageInfo_tv.setText((mGridViewDatas_a.get(position).info));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //三个按钮
        saveBT = (Button) dfWindowView.findViewById(R.id.imagevp_save);
        addBT = (Button) dfWindowView.findViewById(R.id.imagevp_add);
        cancelBT = (Button) dfWindowView.findViewById(R.id.imagevp_cancel);

        //button组
        imagevp_btngroup = dfWindowView.findViewById(R.id.imagevp_btngroup);

        //点击消失
        View blankView = dfWindowView.findViewById(R.id.imagepreview_blank);
        blankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vp_ll_btViewnGroup != null){
                    vp_ll_btViewnGroup.setVisibility(View.GONE);
                }
            }
        });

        //如果是第三页才显示 收藏按钮
        addBT.setVisibility(View.VISIBLE);


        // 给预览页 设置点击监听，如果点击后500ms中没有再次点击 就关闭预览
        dfWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (evpClickTime > 0) {
                    if ((SystemClock.currentThreadTimeMillis() - evpClickTime) < 500L) {
                        // 关闭预览
                        if (isWindowViewShow) {
                            windowManager.removeView(dfWindowView);
                            isWindowViewShow = false;
                            return;
                        }
                    }
                }
                evpClickTime = SystemClock.currentThreadTimeMillis();
            }
        });



        //给ViewPager设置适配器
        evp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mGridViewDatas_a.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                //通过路径加载图片
                final String imagePath = mGridViewDatas_a.get(position).imageUrl;

                //图片的id
                String imageId = mGridViewDatas_a.get(position).id;

                if (imagePath.endsWith(".gif")) {

                    //如果是 gif图片就换显示方法
                    final GifImageView gifImageView = new GifImageView(UIUtils.getContext());

                    gifImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 关闭预览
                            if (isWindowViewShow) {
                                windowManager.removeView(dfWindowView);
                                isWindowViewShow = false;
                                return;
                            }
                        }
                    });

                    //load
                    gifImageView.setImageResource(R.drawable.ic_launcher);

                    //通过imagePath判断本地是否有缓存，有就从本地加载，没有才从网络加载
                    if (FileUtils.findGifFile(imagePath)){
                        //存在，直接从本地缓存加载
                        try {
                            GifDrawable gifDrawable = new GifDrawable(FileUtils.getGifpath(imagePath));
                            gifImageView.setImageDrawable(gifDrawable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        // 不存在，从网络获取，然后保存
                        NetUtils.getByteByNet(imagePath, new Listener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] response) {
                                try {

                                    GifDrawable gifDrawable = new GifDrawable(response);
                                    gifImageView.setImageDrawable(gifDrawable);

                                    //保存到本地
                                    FileUtils.saveGif(response, imagePath);
                                } catch (IOException e) {
                                    //加载失败，显示错误图片
                                    gifImageView.setImageResource(R.drawable.load_failed);
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    showSaveImageWindow(gifImageView, imageId);//保存
                    container.addView(gifImageView);
                    return gifImageView;
                } else {
                    TouchImageView touchImageView = new TouchImageView(UIUtils.getContext());

                    touchImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 关闭预览
                            if (isWindowViewShow) {
                                windowManager.removeView(dfWindowView);
                                isWindowViewShow = false;
                                return;
                            }
                        }
                    });

                    Netroid.displayImage(imagePath, touchImageView);
                    showSaveImageWindow(touchImageView, imageId);//保存
                    container.addView(touchImageView);
                    return touchImageView;
                }

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        evp.setCurrentItem(index);

        // 设置布局参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //宽高填充
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //全屏 要有WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，好像因为这个才能相应返回键？
        params.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //透明窗体
        params.format = PixelFormat.TRANSPARENT;
        //普通应用程序窗口
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        //添加
        windowManager.addView(dfWindowView, params);

        //设置打开标记
        isWindowViewShow = true;

    }

    // 长按图片 保存和收藏
    private void showSaveImageWindow(final ImageView imageView, final String imageId){
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //dfWindowView
                //显示
                vp_ll_btViewnGroup.setVisibility(View.VISIBLE);
//                UIUtils.showToastSafe(""+dfWindowView.getHeight()+"\n"+vp_ll_btViewnGroup.getHeight());
//                //弹出时的平移动画
                TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
                ta.setDuration(300L);
                imagevp_btngroup.startAnimation(ta);

                // 三个按钮
                //保存本地，添加收藏，取消
                cancelBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //隐藏
                        vp_ll_btViewnGroup.setVisibility(View.GONE);
                    }
                });

                addBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //隐藏
                        vp_ll_btViewnGroup.setVisibility(View.GONE);

                        //去 收藏，传 id 和收藏类型
                        // TODO 图片的 id
                        if (SJT_UI_Utils.userState()) {
                            if (!TextUtils.isEmpty(ConstJS_F.detailId)){
                                collect(imageId);
                            }else {
                                SJT_UI_Utils.showDialog(SearchActivity.this, "收藏失败", false);
                            }

                        } else {
                            UIUtils.showToastSafe("请登陆...");
                            startActivity(new Intent(SearchActivity.this, LoadActivity.class));
                        }
                    }
                });

                saveBT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //隐藏
                        vp_ll_btViewnGroup.setVisibility(View.GONE);

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                        FileUtils.saveBitmap(bitmapDrawable.getBitmap());
                        UIUtils.showToastSafe("已保存");
                        // TODO 保存图片
                        SJT_UI_Utils.showDialog(SearchActivity.this, "已保存", true);
                    }
                });


                return true;
            }
        });
    }

    /**
     * 收藏的方法
     * @param artworkID 收藏目标的 id
     */
    private void collect(String artworkID) {
        //拼接请求地址
        String url = "";
        try {
            url = Const.BASE_URL + "api/" + "artwork" + "/" + URLEncoder.encode(artworkID, "UTF-8") + "/collect/";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //请求服务器传数据
        NetUtils.pushStringByNet_POST(url, RequestMapData.params_artworkCollect(), new MyNetWorkObject.SuccessListener() {
            @Override
            public void onSuccess(Object data) {
                SJT_UI_Utils.showDialog(SearchActivity.this, "收藏成功", true);
                LogUtils.e("收藏成功" + data);
            }

            @Override
            public void onError(String msg) {
                //请求失败，隐藏
                UIUtils.showToastSafe("收藏失败 ：" + msg);
            }
        });
    }

    //重写 OnKeyDown

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在按返回键时判断是否打开了窗口，如果打开了就先关闭它
        if (keyCode == KeyEvent.KEYCODE_BACK && isWindowViewShow) {
            windowManager.removeView(dfWindowView);
            isWindowViewShow = false;
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            this.networkImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
