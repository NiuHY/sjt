package com.cmm.worldartapk.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class SearchTest {
    public String limit;
    public String offset;
    public List<SearchData> data;

    public class SearchData {
        public String exhibition_id;
        public String exhibition_image;
        public String exhibition_title;
    }

    @Override
    public String toString() {
        return "SearchTest{" +
                "limit='" + limit + '\'' +
                ", offset='" + offset + '\'' +
                ", data=" + data +
                '}';
    }
}
