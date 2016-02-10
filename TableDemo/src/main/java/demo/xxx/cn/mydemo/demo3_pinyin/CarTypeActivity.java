package demo.xxx.cn.mydemo.demo3_pinyin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/27.
 */
public class CarTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> carTypeList = TempDataClass.carTypeList;

        ListView listView = new ListView(this);

        setContentView(listView);

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, carTypeList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();

                TempDataClass.carTypeList = null;
            }
        });
    }
}
