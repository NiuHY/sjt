package com.cmm.worldartapk.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/15.
 * 是否看过
 */
public class IsReadBean {
    public List<DataId> allId;

    public static class DataId{

        public DataId(int id) {
            this.id = id;
        }

        public int id;
    }
}
