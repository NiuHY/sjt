package demo.xxx.cn.mydemo.demo3_pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Administrator on 2015/10/8.
 */
public class PinyinUtils {

    /**
     * 接受一个name返回对应的拼音
     *
     * @param name 需要处理的name
     * @return 返回name对应的 拼音
     */
    public static String getPinyin(String name) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//没有声调
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//全部大写

        //StringBuilder
        StringBuilder sb = new StringBuilder();
        //遍历名字的每一个字
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);

            //特殊情况
            if (Character.isWhitespace(ch)) {
                //空格跳过
                continue;
            } else if (ch <= 127) {
                //不是汉字,直接拼接
                sb.append(ch);
            } else {
                //拼音
                try {
//                    //多音字 取第一个
//                    Log.e("NIU", ch + "");
//                    Log.e("NIU", format.toString());
                    String pinyin = PinyinHelper.toHanyuPinyinStringArray(ch, format)[0];
                    sb.append(pinyin);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            }
        }

        return sb.toString().toUpperCase();
    }
}
