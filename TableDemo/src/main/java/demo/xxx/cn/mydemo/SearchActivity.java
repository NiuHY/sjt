package demo.xxx.cn.mydemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class SearchActivity extends AppCompatActivity {

    private EditText mSearchEt;
    private ListView mSearchListview;
    private List<String> requestResult;
    private ArrayAdapter<String> adapter;
    private InputMethodManager imm;
    private String searchKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //软键盘 控制
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setContentView(R.layout.search_activity);

        mSearchEt = (EditText) findViewById(R.id.search_et);
        mSearchListview = (ListView) findViewById(R.id.search_listview);

        //软键盘 点击搜索 触发
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当软键盘按下搜索时，相当于点击搜索按钮
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(mSearchEt.getText().toString().trim())) {
                    searchButtonClick();
                }
                return false;
            }
        });

        requestResult = new ArrayList<>();
        requestResult.add("基本数据  1");
        requestResult.add("基本数据  2");

        //适配器
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, requestResult);
        mSearchListview.setAdapter(adapter);

        mSearchListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击跳到表格
                Toast.makeText(SearchActivity.this, "跳转", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 搜索点击后
     */
    private void searchButtonClick() {

        //关闭软键盘
        imm.hideSoftInputFromWindow(mSearchEt.getWindowToken(), 0);

        //获取 搜索内容
        searchKey = mSearchEt.getText().toString().trim();
        //通过这个值 去请求，得到结果



        Toast.makeText(this, "请求数据", Toast.LENGTH_SHORT).show();

        //换结果集合
        requestResult.clear();

        for (int i = 0; i < 5; i++) {
            requestResult.add("请求结果 " + (int)(Math.random()*100));
        }

        //得到请求数据，刷新ListView
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }

    }
}
