package com.cmm.worldartapk.bean.parser;

import com.cmm.worldartapk.bean.SearchTest;
import com.cmm.worldartapk.net_volley_netroid.net_2.BaseParser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/12/11.
 */
public class SearchTestParser extends BaseParser<SearchTest> {
    @Override
    public SearchTest parserJson(String json) {
        //解析JavaBean
        Gson gson = new Gson();

        SearchTest searchTest = gson.fromJson(json, SearchTest.class);
        return searchTest;
    }
}
