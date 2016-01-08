package demo.xxx.cn.mydemo;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class MyTableView {
    private Context context;

    //表 数据
    private List<List<String>> tableData;
    private int columnCount;

    private View tableView;
    private Button button;

    /**
     * 表格管理对象
     *
     * @param context
     * @param tableData   表格数据
     * @param columnCount 列数 如果行数据的长度都相等，可以使用另一个构造方法
     */
    public MyTableView(Context context, List<List<String>> tableData, int columnCount) {
        this.context = context;
        this.tableData = tableData;
        if (columnCount > 0){
            this.columnCount = columnCount;
        }

    }

    /**
     *  表格管理对象
     *
     * @param context
     * @param tableData  表格数据 如果 行数据的没有设置完全，可以使用另一个固定列数的构造方法
     */
    public MyTableView(Context context, List<List<String>> tableData) {
        this.context = context;
        this.tableData = tableData;
    }

    /**
     * 通过行数据集合 和列数 创建表格
     *
     * @return
     */
    public View createTable() {

        //先创建一个表View
        if (tableView == null) {
            tableView = View.inflate(context, R.layout.table, null);
        } else {
            //如果tableView不是null 就是update，清空内容，重新添加
            ((LinearLayout) tableView).removeAllViews();
        }


        //遍历集合得到每一行数据，添加数据
        for (int i = 0; i < tableData.size(); i++) {
            //每一行创建一个row
            LinearLayout tableRow = (LinearLayout) View.inflate(context, R.layout.table_row, null);

            //获取列数据
            final List<String> rowData = tableData.get(i);

            //遍历列数据根据数据创建TextView 或者 EditText 添加到 row中
            int j_columnCount = 0;
            if (columnCount > 0){
                j_columnCount = columnCount;
            }else {
                j_columnCount = rowData.size();
            }
            for (int j = 0; j < j_columnCount; j++) {
                //获取数据
                String data = rowData.get(j);
                if (TextUtils.isEmpty(data)) {
                    data = "";
                }
                //如果是第一列，用TextView显示 不能修改，其他用EditText可以修改
                if (j == 0) {
                    TextView textView = new TextView(context);
                    tableRow.addView(textView);
                    textView.setText(data);
                    textView.setLayoutParams(getLayoutParams(dip2Px(110), dip2Px(40)));
                    textView.setGravity(Gravity.CENTER);

                } else {
                    final EditText editText = new EditText(context);
                    tableRow.addView(editText);
                    editText.setText(data);
                    editText.setLayoutParams(getLayoutParams(dip2Px(0), dip2Px(40)));
                    editText.setGravity(Gravity.CENTER);
                    editText.setBackgroundDrawable(null);

                    //给EditText设置变化监听，动态更改 数据
                    final int finalJ = j;
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String data = editText.getText().toString().trim();
                            rowData.set(finalJ, data);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }

                //添加列分割线
                if (j != rowData.size() - 1) {
                    //列分割线
                    View viewColumn = new View(context);
                    viewColumn.setLayoutParams(new LinearLayout.LayoutParams(dip2Px(1), ViewGroup.LayoutParams.MATCH_PARENT));
                    viewColumn.setBackgroundColor(0xff111111);
                    tableRow.addView(viewColumn);
                }
            }

            //添加到表格中
            ((LinearLayout) tableView).addView(tableRow);
            //每添加一个就添加一个分割线，如果是最后一行，不添加
            if (i != tableData.size() - 1) {
                //行分割线
                View viewRow = new View(context);
                viewRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2Px(1)));
                viewRow.setBackgroundColor(0xff111111);
                ((LinearLayout) tableView).addView(viewRow);
            }
        }

        return tableView;
    }

    /**
     * 获取表格数据集合
     *
     * @return
     */
    public List<List<String>> getTableData() {
        return tableData;
    }

    /**
     * 直接更新 表格数据
     *
     * @param tableData
     */
    public void updateTableData(List<List<String>> tableData) {
        this.tableData = tableData;
        tableView = createTable();
    }

    /**
     * 得到表格对象
     *
     * @return
     */
    public View getTableView() {
        return tableView;
    }


    /**
     * 得到表格按钮
     *
     * @return
     */
    public Button getButton() {
        return button;
    }

    /**
     * 创建带 Button的表格
     *
     * @param buttonName Button名字
     * @return
     */
    public ViewGroup createTableGroup(String buttonName) {

        ViewGroup viewGroup = (ViewGroup) View.inflate(context, R.layout.tablegroup, null);
        LinearLayout linearLayout = (LinearLayout) viewGroup.findViewById(R.id.main_ll);

        //button
        button = new Button(context);
        button.setText(buttonName);
        //创建biaoge
        tableView = createTable();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableView != null && tableView.getVisibility() == View.VISIBLE) {
                    tableView.setVisibility(View.GONE);

                } else if (tableView != null && tableView.getVisibility() == View.GONE) {
                    tableView.setVisibility(View.VISIBLE);
                }
            }
        });


        linearLayout.addView(button);
        linearLayout.addView(tableView);

        return viewGroup;
    }


    //LayoutParams
    private LinearLayout.LayoutParams getLayoutParams(int w, int h) {
        LinearLayout.LayoutParams layoutParams = new TableRow.LayoutParams();

        if (w == 0) {
            layoutParams.weight = 1;
        }

        layoutParams.width = w;
        layoutParams.height = h;

        int margin = dip2Px(1) / 4;
        layoutParams.setMargins(margin, margin, margin, margin);
        return layoutParams;
    }


    public int dip2Px(int dp) {
        //获取屏幕显示规格密度
        float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5);
        return px;
    }
}
