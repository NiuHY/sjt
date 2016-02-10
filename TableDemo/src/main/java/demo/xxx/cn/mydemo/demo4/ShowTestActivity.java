package demo.xxx.cn.mydemo.demo4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import demo.xxx.cn.mydemo.R;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ShowTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_activity);
    }

    public void showD(View view){
        Utils.showDialog(this);
    }
}
