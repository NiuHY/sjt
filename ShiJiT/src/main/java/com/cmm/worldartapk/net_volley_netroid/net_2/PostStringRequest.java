package com.cmm.worldartapk.net_volley_netroid.net_2;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.request.StringRequest;

/**
 * Created by Administrator on 2015/12/18.
 */
public class PostStringRequest extends StringRequest {
    public PostStringRequest(int method, String url, Listener<String> listener) {
        super(Method.POST, url, listener);
    }
}
