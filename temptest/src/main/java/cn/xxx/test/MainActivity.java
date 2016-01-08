package cn.xxx.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/12/31.
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
//
//        listView = (ListView) findViewById(R.id.main_listview);
//
//        //填充测试数据
//        ArrayList<String> testList = new ArrayList<>();
//        for (int i = 0; i < 99; i++) {
//            testList.add("测试+++++" + i);
//        }
//
//        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, testList));


        ImageView imageView = (ImageView) findViewById(R.id.img);


        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.settings_bg);
        //获得图片的宽，并创建结果bitmap
        int width = bmp.getWidth();
        Bitmap resultBmp = Bitmap.createBitmap(width, width,
                Bitmap.Config.ARGB_4444);

        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(0, resultBmp.getHeight());
        path.lineTo(resultBmp.getWidth(), resultBmp.getHeight());
        path.lineTo(resultBmp.getWidth(), resultBmp.getWidth() / 10);
        path.close();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(resultBmp);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        //画圆
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 选择交集去上层图片
        canvas.drawBitmap(bmp, 0, 0, paint);


        imageView.setImageBitmap(resultBmp);

        bmp.recycle();
    }
}
