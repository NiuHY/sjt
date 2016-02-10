package demo.xxx.cn.mydemo.demo3_pinyin;

/**
 * Created by Administrator on 2015/10/8.
 */
public class NameItem implements Comparable<NameItem> {
    //姓名 和 对应的拼音
    private String name;
    private String pinyin;

    public NameItem(String name) {
        this.name = name;
        this.pinyin = PinyinUtils.getPinyin(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int compareTo(NameItem another) {
        //全部转成大写进行比较
        return this.pinyin.toUpperCase().compareTo(another.getPinyin().toUpperCase());
    }
}
