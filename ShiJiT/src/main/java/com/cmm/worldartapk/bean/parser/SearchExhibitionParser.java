package com.cmm.worldartapk.bean.parser;

import com.cmm.worldartapk.bean.SearchBean_Exhibition;
import com.cmm.worldartapk.net_volley_netroid.net_2.BaseParser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/12/11.
 */
public class SearchExhibitionParser extends BaseParser<SearchBean_Exhibition> {
    @Override
    public SearchBean_Exhibition parserJson(String json) {
        //解析JavaBean
        Gson gson = new Gson();
        SearchBean_Exhibition searchbean_exhibition = gson.fromJson(json, SearchBean_Exhibition.class);
        return searchbean_exhibition;
    }
}
