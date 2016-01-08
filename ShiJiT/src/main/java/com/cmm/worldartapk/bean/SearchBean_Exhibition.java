package com.cmm.worldartapk.bean;

import java.util.List;

/**
 * Exhibition Bean
 * Created by Administrator on 2015/12/17.
 */
public class SearchBean_Exhibition {
    public List<Search_E_Data> data;
    public String success;
    public String limit;
    public String offset;

    public static class Search_E_Data {
        public String exhibition_city;
        public String exhibition_city_cover;
        public String exhibition_country;
        public String exhibition_cover;
        public String exhibition_end_time;
        public String exhibition_id;
        public String exhibition_intro;
        public String exhibition_location;
        public String exhibition_open_time;
        public String exhibition_province;
        public String exhibition_ref_story_id;
        public String exhibition_sequence;
        public String exhibition_start_time;
        public String exhibition_title;
        public List<ExhibitionImage> exhibition_image_array;

        public static class ExhibitionImage {
            public String image_url;
            public String material_id;
            public String resource_id;
            public ResourceDescription resource_description;

            public static class ResourceDescription {
                public String CoverCropRect;
                public String CoverCropRectRate;
                public String CoverDescriptionAlignment;
                public String CoverDescriptionFontIsBold;
                public String CoverDescription;
                public String CoverDisplayWidthRate;
                public String CoverSize;
                public String type;

                public String ImageCropRect;
                public String ImageCropRectRate;
                public String ImageDescriptionAlignment;
                public String ImageDescriptionFontIsBold;
                public String ImageDescription;
                public String ImageDisplayWidthRate;
                public String ImageSize;

                @Override
                public String toString() {
                    return "ResourceDescription{" +
                            "CoverCropRect='" + CoverCropRect + '\'' +
                            ", CoverCropRectRate='" + CoverCropRectRate + '\'' +
                            ", CoverDescriptionAlignment='" + CoverDescriptionAlignment + '\'' +
                            ", CoverDescriptionFontIsBold='" + CoverDescriptionFontIsBold + '\'' +
                            ", CoverDescription='" + CoverDescription + '\'' +
                            ", CoverDisplayWidthRate='" + CoverDisplayWidthRate + '\'' +
                            ", CoverSize='" + CoverSize + '\'' +
                            ", type='" + type + '\'' +
                            ", ImageCropRect='" + ImageCropRect + '\'' +
                            ", ImageCropRectRate='" + ImageCropRectRate + '\'' +
                            ", ImageDescriptionAlignment='" + ImageDescriptionAlignment + '\'' +
                            ", ImageDescriptionFontIsBold='" + ImageDescriptionFontIsBold + '\'' +
                            ", ImageDescription='" + ImageDescription + '\'' +
                            ", ImageDisplayWidthRate='" + ImageDisplayWidthRate + '\'' +
                            ", ImageSize='" + ImageSize + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "ExhibitionImage{" +
                        "image_url='" + image_url + '\'' +
                        ", material_id='" + material_id + '\'' +
                        ", resource_id='" + resource_id + '\'' +
                        ", resource_description=" + resource_description +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "SearchData{" +
                    "exhibition_city='" + exhibition_city + '\'' +
                    ", exhibition_city_cover='" + exhibition_city_cover + '\'' +
                    ", exhibition_country='" + exhibition_country + '\'' +
                    ", exhibition_cover='" + exhibition_cover + '\'' +
                    ", exhibition_ent_time='" + exhibition_end_time + '\'' +
                    ", exhibition_id='" + exhibition_id + '\'' +
                    ", exhibition_intro='" + exhibition_intro + '\'' +
                    ", exhibition_location='" + exhibition_location + '\'' +
                    ", exhibition_open_time='" + exhibition_open_time + '\'' +
                    ", exhibition_province='" + exhibition_province + '\'' +
                    ", exhibition_ref_story_id='" + exhibition_ref_story_id + '\'' +
                    ", exhibition_sequence='" + exhibition_sequence + '\'' +
                    ", exhibition_start_time='" + exhibition_start_time + '\'' +
                    ", exhibition_title='" + exhibition_title + '\'' +
                    ", exhibition_image_array=" + exhibition_image_array +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SearchBean_Exhibition{" +
                "data=" + data +
                ", success='" + success + '\'' +
                ", limit='" + limit + '\'' +
                ", offset='" + offset + '\'' +
                '}';
    }
}
