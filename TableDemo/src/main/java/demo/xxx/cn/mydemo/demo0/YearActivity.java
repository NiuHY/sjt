package demo.xxx.cn.mydemo.demo0;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/14.
 */
public class YearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = YearUtils.getYearsListView(this);

        setContentView(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(YearActivity.this, ((TextView)view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });



//        ListView listView = new ListView(this);
//
//        MyYearAdapter myYearAdapter = new MyYearAdapter();
//
//        listView.setAdapter(myYearAdapter);
//
//        listView.setSelection(Calendar.getInstance().get(Calendar.YEAR));
    }

    private class MyYearAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Calendar.getInstance().get(Calendar.YEAR) - 1999;//一万年
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = new TextView(YearActivity.this);
            }

            //从0开始
            ((TextView)convertView).setText(position+2000 + "");

            return convertView;
        }
    }
}
