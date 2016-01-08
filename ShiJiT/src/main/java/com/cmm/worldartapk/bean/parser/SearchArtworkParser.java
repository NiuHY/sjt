package com.cmm.worldartapk.bean.parser;

import com.cmm.worldartapk.bean.SearchBean_Artwork;
import com.cmm.worldartapk.net_volley_netroid.net_2.BaseParser;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015/12/11.
 */
public class SearchArtworkParser extends BaseParser<SearchBean_Artwork> {
    @Override
    public SearchBean_Artwork parserJson(String json) {
        //解析JavaBean
        Gson gson = new Gson();
        SearchBean_Artwork searchBean_artwork = gson.fromJson(json, SearchBean_Artwork.class);
        return searchBean_artwork;
    }
}
