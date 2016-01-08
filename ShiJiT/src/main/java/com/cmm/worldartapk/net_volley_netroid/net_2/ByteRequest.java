package com.cmm.worldartapk.net_volley_netroid.net_2;

import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetworkResponse;
import com.duowan.mobile.netroid.Request;
import com.duowan.mobile.netroid.Response;

/**
 * Created by Administrator on 2015/12/15.
 */
public class ByteRequest extends Request<byte[]> {
    public ByteRequest(int method, String url, Listener<byte[]> listener) {
        super(method, url, listener);
    }

    public ByteRequest(String url, Listener<byte[]> listener) {
        super(url, listener);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, response);
    }
}
