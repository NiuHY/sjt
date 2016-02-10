package demo.xxx.cn.mydemo.demo3_pinyin;

import java.util.List;

/**
 * Created by Administrator on 2016/1/27.
 */
public class CarBrandEntry {
//    {
//        "carbrandid": "0432f008091748aaafa9b0d6fa66e622",
//            "carbrand": "0",
//            "cartype": "A 阿尔法罗米欧",
//            "data": [
//        {
//            "carbrandid": "0213b64059bb4ef5a34f443cd5643bcb",
//                "carbrand": "0432f008091748aaafa9b0d6fa66e622",
//                "cartype": "Giulietta"
//        },
//        {
//            "carbrandid": "20d2a67e46ec45f48ba946085b60058c",
//                "carbrand": "0432f008091748aaafa9b0d6fa66e622",
//                "cartype": "ALFA 8C"
//        },
//        {
//            "carbrandid": "1f8cd421c7484be8a3d60fbc48b966da",
//                "carbrand": "0432f008091748aaafa9b0d6fa66e622",
//                "cartype": "ALFA 159"
//        }
//        ]
//    }

    public String carbrandid;
    public String carbrand;
    /**
     * 品牌 逗比
     */
    public String cartype;

    public List<TypeEntry> data;



    public static class TypeEntry{
        public String carbrandid;
        public String carbrand;
        /**
         * 型号
         */
        public String cartype;
    }
}
