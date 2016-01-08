package com.cmm.worldartapk.net_volley_netroid.net_2;

/**
 * Created by Administrator on 2015/12/10.
 * Json 解析工具父类
 */
public abstract class BaseParser<T> {

    /**
     * @param json 要解析的 json串
     * @return 解析后的JavaBean
     */
    public abstract T parserJson(String json);
}
