package demo.xxx.cn.mydemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.main_ll);
//
//        linearLayout = (LinearLayout) findViewById(R.id.main_ll);

        //添加表格
        addTable(2);
        addTable(3);
        addTable(4);


    }

    private void addTable(int columnCount) {
        //table
        //数据 10行 columnCount列
        List<List<String>> tableData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> rowData = new ArrayList<>();
            // 第一个有数据，第二个空
            for (int j = 0; j < columnCount; j++) {
                if (j == 0){
                    rowData.add("行 " + i);
                }else{
                    rowData.add(i + " 行数据");
                }
            }
            tableData.add(rowData);
        }

        //创建biaoge
        final MyTableView myTableView = new MyTableView(this, tableData);

        //创建 带 button的表格
        ViewGroup viewGroup = myTableView.createTableGroup("我是表格");
//        myTableView.getButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //测试更新
////                List<List<String>> tableData = new ArrayList<>();
////                for (int i = 0; i < 5; i++) {
////                    List<String> rowData = new ArrayList<>();
////                    // 第一个有数据，第二个空
////                    for (int j = 0; j < 10; j++) {
////                        if (j == 0){
////                            rowData.add("行 " + i);
////                        }else{
////                            rowData.add(i + " 行数据");
////                        }
////                    }
////                    tableData.add(rowData);
////                }
////
////                //点击后更新数据
////                myTableView.updateTableData(tableData);
//
//                //测试获取数据
////                System.out.println("============" + myTableView.getTableData().toString());
//            }
//        });

        //把表格View添加到 linearLayout中
        linearLayout.addView(viewGroup);

    }
}
