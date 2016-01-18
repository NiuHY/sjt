package cn.xxx.mainpagerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

        //主页ViewPager
        viewPager = (ViewPager) findViewById(R.id.main_viewPager);

    }
}
