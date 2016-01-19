package cn.xxx.mainpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cn.xxx.test.R;

/**
 * Created by Administrator on 2016/1/18.
 */
public class PagerActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainpager_activity);

        initView();

        //主页ViewPager
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);

    }

    //初始化化 ViewPager
    private void initView() {
        //预加载2页
        viewPager.setOffscreenPageLimit(2);

        //适配器
//        viewPager.setAdapter();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }
}
