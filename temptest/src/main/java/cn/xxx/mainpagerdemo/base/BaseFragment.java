package cn.xxx.mainpagerdemo.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/12/24.
 */
public abstract class BaseFragment extends Fragment {

    private View fragmentContentView;

    // 初始化布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //缓存View对象

        if (fragmentContentView == null){
            fragmentContentView = initFragmentView(inflater, container, savedInstanceState);
        }else{
            //被ViewPager复用不能有父控件，移除
            ((ViewGroup)fragmentContentView.getParent()).removeView(fragmentContentView);
        }

        return fragmentContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 对控件进行操作
        initViewData(savedInstanceState);
    }

    /**
     * 子类重写这个方法 初始化布局
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 内容 View
     */

    protected abstract View initFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    /**
     * 子类重写这个方法 初始化对控件的操作
     * @param savedInstanceState
     */
    protected abstract void initViewData(Bundle savedInstanceState);



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
