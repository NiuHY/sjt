package com.cmm.worldartapk.bean.parser;

import com.cmm.worldartapk.bean.SearchBean_Gallery;
import com.cmm.worldartapk.net_volley_netroid.net_2.BaseParser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/12/11.
 */
public class SearchGalleryParser extends BaseParser<SearchBean_Gallery> {
    @Override
    public SearchBean_Gallery parserJson(String json) {
        //解析JavaBean
        Gson gson = new Gson();
        SearchBean_Gallery searchbean_gallery = gson.fromJson(json, SearchBean_Gallery.class);
        return searchbean_gallery;
    }
}
