package demo.xxx.cn.mydemo.demo4.ch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import demo.xxx.cn.mydemo.R;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ChActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);

        View view = new View(this);
        view.setBackgroundResource(R.drawable.test);

        setContentView(view);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null){
                    convertView = View.inflate(ChActivity.this, R.layout.cehua_item, null);
                }
                return convertView;
            }
        });
    }
}
