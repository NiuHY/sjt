package demo.xxx.cn.mydemo.demo1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import demo.xxx.cn.mydemo.R;

/**
 * Created by Administrator on 2016/1/25.
 */
public class ClickActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo1);

        final ClickManagerTest clickManagerTest = new ClickManagerTest();

        button = (Button) findViewById(R.id.button);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return clickManagerTest.clickMethod(v, event);
            }
        });
    }
}
