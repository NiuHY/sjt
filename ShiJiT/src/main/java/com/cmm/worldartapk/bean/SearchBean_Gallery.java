package com.cmm.worldartapk.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/12/21.
 * Gallery Bean
 */
public class SearchBean_Gallery {
    //{"success":1,"data":[{"gallery_id":5,"gallery_name":"%E5%AE%89%E8%BF%AA%C2%B7%E6%B2%83%E9%9C%8D%E5%B0%94%E7%9A%84%E5%B7%A5%E5%8E%82","gallery_en_name":"Andy%20Warhol's%20Factory","gallery_cover":"http://image.yuntoo.com/2048/2048/fc258e42c557fb4642a94bffc75cf1a8.jpg@1e_750w_750h_1c_0i_1o_75Q_1x.jpg","gallery_owner_id":531,"gallery_description":"%E6%AC%A2%E8%BF%8E%E6%9D%A5%E5%88%B0%E6%B3%A2%E6%99%AE%E8%89%BA%E6%9C%AF%E5%A4%A7%E5%B8%88%E7%9A%84%E4%B8%96%E7%95%8C%E3%80%82%E5%AE%89%E8%BF%AA%C2%B7%E6%B2%83%E9%9C%8D%E5%B0%94%EF%BC%88Andy%20Warhol%EF%BC%8C1928%E5%B9%B48%E6%9C%886%E6%97%A5%EF%BC%8D1987%E5%B9%B42%E6%9C%8822%E6%97%A5%EF%BC%89%EF%BC%8C%E6%B3%A2%E6%99%AE%E8%89%BA%E6%9C%AF%E7%9A%84%E5%80%A1%E5%AF%BC%E8%80%85%E5%92%8C%E9%A2%86%E8%A2%96%EF%BC%8C%E4%B9%9F%E6%98%AF20%E4%B8%96%E7%BA%AA%E5%85%A8%E7%90%83%E6%9C%80%E7%9F%A5%E5%90%8D%E7%9A%84%E6%96%87%E5%8C%96%E4%BA%BA%E7%89%A9%E3%80%82%E4%BB%96%E5%90%8C%E6%97%B6%E8%BF%98%E6%98%AF%E7%94%B5%E5%BD%B1%E5%88%B6%E7%89%87%E4%BA%BA%E3%80%81%E4%BD%9C%E5%AE%B6%E3%80%81%E6%91%87%E6%BB%9A%E4%B9%90%E4%BD%9C%E6%9B%B2%E8%80%85%E3%80%81%E5%87%BA%E7%89%88%E5%95%86%E3%80%81%E7%A4%BE%E4%BA%A4%E6%98%8E%E6%98%9F%E3%80%82%E4%BB%96%E5%88%9B%E5%8A%9E%E7%9A%84%E5%B7%A5%E5%8E%82%EF%BC%88Factory%EF%BC%89%EF%BC%8C%E6%98%AF20%E4%B8%96%E7%BA%AA%E6%B5%81%E8%A1%8C%E6%96%87%E5%8C%96%E4%B9%8C%E6%89%98%E9%82%A6%E3%80%82","gallery_category":1,"gallery_phone":null,"gallery_build_time":null,"gallery_open_time":null,"gallery_url":null,"gallery_location":null}],"offset":0,"limit":1}

    public String limit;
    public String offset;
    public String success;

    public List<GalleryData> data;
    public static class GalleryData{

        public String gallery_category;
        public String gallery_cover;
        public String gallery_description;
        public String gallery_en_name;
        public String gallery_id;
        public String gallery_name;
        public String gallery_owner_id;

        @Override
        public String toString() {
            return "GalleryData{" +
                    "gallery_category='" + gallery_category + '\'' +
                    ", gallery_cover='" + gallery_cover + '\'' +
                    ", gallery_description='" + gallery_description + '\'' +
                    ", gallery_en_name='" + gallery_en_name + '\'' +
                    ", gallery_id='" + gallery_id + '\'' +
                    ", gallery_name='" + gallery_name + '\'' +
                    ", gallery_owner_id='" + gallery_owner_id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchBean_Gallery{" +
                "limit='" + limit + '\'' +
                ", offset='" + offset + '\'' +
                ", success='" + success + '\'' +
                ", data=" + data +
                '}';
    }
}
