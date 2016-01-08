package com.cmm.worldartapk.net_volley_netroid.net_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cmm.worldartapk.net_volley_netroid.Netroid;
import com.cmm.worldartapk.utils.UIUtils;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.Request;
import com.duowan.mobile.netroid.request.StringRequest;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/10.
 */
public class NetUtils {


    /**
     * 根据传入的参数进行网络请求，如果有返回数据，在回调方法中接受
     */
    public static void getDataByNet(Context context, String url, Map<String, String> requestDataMap, BaseParser<?> jsonParser, MyNetWorkObject.SuccessListener successListener){
        //TODO  封参数
        // 创建 网络请求对象，请求JSON数据
        MyNetWorkObject myNetWorkObject = new MyNetWorkObject(context, url, requestDataMap, jsonParser, successListener);

//        MyNetWorkObject myNetWorkObject = new MyNetWorkObject();
//        myNetWorkObject.setRequestInfo(context, url, requestDataMap, jsonParser, successListener);

        myNetWorkObject.getDataByNet(Request.Method.GET);
    }

    /**
     * POST 请求
     * 根据传入的参数进行网络请求，如果有返回数据，在回调方法中接受
     */
    public static void getDataByNet_POST(Context context, String url, Map<String, String> requestDataMap, BaseParser<?> jsonParser, MyNetWorkObject.SuccessListener successListener){
        //TODO  封参数
        // 创建 网络请求对象，请求JSON数据
        MyNetWorkObject myNetWorkObject = new MyNetWorkObject(context, url, requestDataMap, jsonParser, successListener);
        myNetWorkObject.getDataByNet(Request.Method.POST);
    }

    /**
     * 请求网络，返回字符串
     */
    public static void getStringByNet(String url, Listener<String> listener){
        StringRequest stringRequest = new StringRequest(url, listener);
        Netroid.getmRequestQueue().add(stringRequest);
    }

    /**
     * 请求网络 返回数据的字节数组 加载gif图片
     * @param url
     * @param listener
     */
    public static void getByteByNet(String url, Listener<byte[]> listener){
        ByteRequest byteRequest = new ByteRequest(url, listener);
        Netroid.getmRequestQueue().add(byteRequest);
    }

    /**
     * 给服务器传一个字符串
     * @param url 地址
     * @param requestDataMap 请求参数(字符串)
     * @param successListener 成功回调
     */
    public static void pushStringByNet(String url, Map<String, String> requestDataMap, MyNetWorkObject.SuccessListener successListener){
        MyNetWorkObject myNetWorkObject = new MyNetWorkObject(null, url, requestDataMap, null, successListener);
        myNetWorkObject.pushString(Request.Method.GET);
    }

    /**
     * POST 请求
     * 给服务器传一个字符串
     * @param url 地址
     * @param requestDataMap 请求参数(字符串)
     * @param successListener 成功回调
     */
    public static void pushStringByNet_POST(String url, Map<String, String> requestDataMap, MyNetWorkObject.SuccessListener successListener){
        MyNetWorkObject myNetWorkObject = new MyNetWorkObject(null, url, requestDataMap, null, successListener);
        myNetWorkObject.pushString(Request.Method.POST);
    }

    /**
     * wifi是否连接
     * @return wifi是否连接
     */
    public static boolean isWifiConnected() {
        Context context = UIUtils.getContext();
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否能上网
     * @return  是否能上网
     */
    public static boolean hasConnectedNetwork() {
        Context context = UIUtils.getContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * 打开设置网络界面
     * */
    public static void openNetworkSetting(final Context context){
        //提示对话框
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?").setPositiveButton("设置", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                } else {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                }
                context.startActivity(intent);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }
}
