package com.cmm.worldartapk.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7.
 */
public class SearchBean_Artwork {

    public String limit;
    public String offset;
    public String success;

    public List<ArtworkData> data;
    public static class ArtworkData{
//        artwork_artist_name": "Unknown engraver",
//                "artwork_id": 10221428,
//                "artwork_name": "Unknown engraver",
//                "image_height": 1920,
//                "image_id": 10221428,
//                "image_url": "http://image.yuntoo.com/1024/1536/62e90b655186f2d7666f9481872b3639.jpg@0e_1200w_1920h_0c_0i_1o_75Q_1x.jpg",
//                "image_width": 1398

        public String artwork_artist_name;
        public String artwork_id;
        public String artwork_name;
        public String image_height;
        public String image_id;
        public String image_url;
        public String image_width;
    }
}
