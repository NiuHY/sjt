package demo.xxx.cn.mydemo.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/13.
 */
public class SearchBean {

    public static List<String[]> test(String str){

        str = "{'list':[[20283,'2201221512250162','测试20151224','20151224','2016'],[20281,'2201221512250163','施工测试','33','2015']]}";

        List<String[]> strings = new ArrayList<>();

        //转成JsonObject --> 得到JsonArray， 解析JsonArray中的每个元素
        try {
            JSONObject jsonObject = new JSONObject(str);

            JSONArray jsonArray = jsonObject.getJSONArray("list");

            //遍历JsonArray，解析每个元素
            for (int i = 0; i < jsonArray.length(); i++) {
                String element = jsonArray.getString(i);

                strings.add(parseElement(element));
            }

            // 得到结果
            return strings;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String[] parseElement(String element) {

        //按 "，" 分
        String[] result = element.split(",");

        //去单引号
        for (int i = 0; i < result.length; i++) {
            String tempStr = result[i];
            if (tempStr.startsWith("'")){
                result[i] = tempStr.substring(1, tempStr.length()-1);
            }
        }

        return result;
    }
}
