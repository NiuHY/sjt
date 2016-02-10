package demo.xxx.cn.mydemo.demo3_pinyin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/1/27.
 */
public class TestActivity extends AppCompatActivity {

    private List<CarBrandEntry> carBrandEntries;
    private List<String> strings;
    private QuickIndexBarListView quickIndexBarView;
    private AlertDialog alertDialog;
    private Map<String, List<String>> carTypeMap;
    private List<String> stringBrandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        strings = new ArrayList<>();

        //创建包含快速索引的ListView的布局
        quickIndexBarView = new QuickIndexBarListView(this, strings);
        quickIndexBarView.setHideState(true);
        setContentView(quickIndexBarView);

        //点击监听
        quickIndexBarView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyToast.showMyToast(TestActivity.this, quickIndexBarView.getNameItems().get(position).getName());

                //这里应该是 点击后关闭当前Activity然后设置结果到打开这个Activity
                //你的事

                // DEMO型号 点击后打开一个新的Activity 显示一个ListView 他的数据就是品牌对应型号集合
                TempDataClass.carTypeList = carTypeMap.get(quickIndexBarView.getNameItems().get(position).getName());
                startActivity(new Intent(TestActivity.this, CarTypeActivity.class));

            }
        });

        //创建品牌对象集合
        carBrandEntries = new ArrayList<>();

        //请求这个 url 得到品牌数据集合
        final String url = "http://wap.bjoil.com/pic/mobile/mss_carbrand_data.json";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(9999);

        //在请求之前 弹出不可撤销的进度对话框
        showDiglog();

        asyncHttpClient.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int code, Header[] headers, String responseBody, Throwable throwable) {
                MyToast.showMyToast(TestActivity.this, "请求失败");
            }

            @Override
            public void onSuccess(int code, Header[] headers, String responseBody) {
                MyToast.showMyToast(TestActivity.this, "请求成功");
                dismissDialog();
                //结果是个 JsonArray
                try {
                    JSONArray jsonArray = new JSONArray(responseBody);
                    carBrandEntries.clear();
                    Gson gson = new Gson();
                    //遍历得到每个对象对应的json，然后解析
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String objJson = jsonArray.getString(i);
                        carBrandEntries.add(gson.fromJson(objJson, CarBrandEntry.class));
                    }
                    //这里得到了 carBrandEntries集合 转换提取集合中品牌信息
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    /**
     * 设置布局数据
     */
    private void setData() {
        //品牌集合
        stringBrandList = new ArrayList<String>();
        //型号集合 键是品牌名，值是型号集合
        carTypeMap = new HashMap<>();

        for (int i = 0; i < carBrandEntries.size(); i++) {
            stringBrandList.add(carBrandEntries.get(i).cartype);//品牌
            //遍历data得到型号
            List<String> carTypeList = new ArrayList<String>();
            for (int j = 0; j < carBrandEntries.get(i).data.size(); j++) {
                carTypeList.add(carBrandEntries.get(i).data.get(j).cartype);
            }
            carTypeMap.put(carBrandEntries.get(i).cartype, carTypeList);//品牌和对应型号
        }
//        Log.e("NIU", stringBrandList.toString());
        quickIndexBarView.setData(stringBrandList);
    }


    private void showDiglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(new ProgressBar(this));
        alertDialog.show();
    }
    private void dismissDialog(){
        alertDialog.dismiss();
    }
}
