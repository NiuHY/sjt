package com.cmm.worldartapk.net_volley_netroid.net_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.cmm.worldartapk.net_volley_netroid.Const;
import com.cmm.worldartapk.net_volley_netroid.Netroid;
import com.cmm.worldartapk.utils.LogUtils;
import com.cmm.worldartapk.utils.UIUtils;
import com.duowan.mobile.netroid.DefaultRetryPolicy;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.NetroidError;
import com.duowan.mobile.netroid.Request;
import com.duowan.mobile.netroid.request.StringRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2015/12/10.
 * 网络对象
 * 在NetUtils中创建，在创建时传入所需参数，通过回调接口接受请求结果
 */
public class MyNetWorkObject {
    private Context context;
//    private boolean flag;// 是否请求数据

//    private static boolean isLoad = false;

    //请求成功的回调接口
    public interface SuccessListener {
        public abstract void onSuccess(Object data);
        public abstract void onError(String msg);
    }

    private SuccessListener successListener;

    public void setSuccessListener(SuccessListener successListener) {
        this.successListener = successListener;
    }

    private String url; //请求的地址
    private Map<String, String> requestDataMap; //请求参数
    //请求方式默认get
    private BaseParser<?> jsonParser; //解析工具类

    /**
     * 初始化网络请求对象
     *
     * @param context         当前Activity的上下文对象，用来弹出对话框
     * @param url             请求的地址
     * @param requestDataMap  请求的参数map
     * @param jsonParser      解析工具类
     * @param successListener 请求到数据的回调
     */
    public MyNetWorkObject(Context context, String url, Map<String, String> requestDataMap, BaseParser<?> jsonParser, SuccessListener successListener) {
        this.context = context;
        this.successListener = successListener;
        this.url = url;
        this.requestDataMap = requestDataMap;
        this.jsonParser = jsonParser;
    }

    public MyNetWorkObject(){}

    public void setRequestInfo(Context context, String url, Map<String, String> requestDataMap, BaseParser<?> jsonParser, SuccessListener successListener) {
        this.context = context;
        this.successListener = successListener;
        this.url = url;
        this.requestDataMap = requestDataMap;
        this.jsonParser = jsonParser;
    }

    /**
     * 给服务器传字符串
     */
    public void pushString(final int METHOD) {
        if (!NetUtils.hasConnectedNetwork()) {
            UIUtils.showToastSafe("无法连接网络");
//            Toast.makeText(UIUtils.getContext(), "无法联网。。", Toast.LENGTH_LONG).show();
            return;
        }
        //请求
        if (METHOD == Request.Method.POST) {
            postRequest();
        } else {
            get();
        }
    }

    /**
     * 标准请求
     *
     * @param METHOD 请求方式
     */
    public void getDataByNet(final int METHOD) {

//        flag = true;
        // TODO 判断网络情况，1 是否能上网；2 是否是wifi 如果不是wifi就弹出对话框判断是否请求
        if (!NetUtils.hasConnectedNetwork()) {
            UIUtils.showToastSafe("无法连接网络");
//            Toast.makeText(UIUtils.getContext(), "无法联网。。", Toast.LENGTH_LONG).show();
            if (successListener != null){
                successListener.onError("无法连接网络！");
            }
            return;
        } else if (!Const.isWifi && !NetUtils.isWifiConnected()) {
            //如果有线程在进行判断，就不进行判断
            if (!Const.getJudgeWifiState()){
                //记录自己正在判断
                Const.setJudgeWifiState(true);

                //不是wifi
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("没有连接 WIFI 是否继续加载？");
                builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //记录 允许加载，每次只判断一次
                        Const.isWifi = true;

                        //请求
                        if (METHOD == Request.Method.POST) {
                            postRequest();
                        } else {
                            get();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (successListener != null){
                            successListener.onError("取消访问！");
                        }
                        //如果点取消，下次还进行判断
                        Const.setJudgeWifiState(false);
                    }
                });
                builder.show();
            }
        } else {
            //是wifi，正常请求
            //请求
            if (METHOD == Request.Method.POST) {
                postRequest();
            } else {
                get();
            }
        }
    }

    /**
     * get 请求 得到解析后的对于的JavaBean
     */
    private void get() {
        //拼接url请求参数 get
        if (requestDataMap != null && !requestDataMap.isEmpty()) {
            //对请求参数进行编码
            String encodedParams = encodeParameters(requestDataMap);
            if (encodedParams.length() > 0) {
                if (-1 == url.indexOf("?"))//=-1表示没有?连接符号
                    url = url + "?" + encodedParams;//如何没有?连接符号则加上?后,再接参数
                else {//如果有?连接符则直接连参数
                    url = url + "&" + encodedParams;
                }
            }
        }

        LogUtils.i("URL = " + url);

        // 请求JSON数据
        StringRequest request = new StringRequest(url, new Listener<String>() {
            @Override
            public void onPreExecute() {
                //UIUtils.showToastSafe("loading......");
            }

            // cancel the dialog with onFinish() callback
            @Override
            public void onFinish() {
//                UIUtils.hintToast();
            }

            @Override
            public void onSuccess(String response) {
                // TODO   请求成功的回调  JSON 解析错误 ——待
                //通过解析工具类 解析 json串得到相应对象
                if (jsonParser != null) {
                    Object data = jsonParser.parserJson(response);
                    if (successListener != null && data != null) {
                        //回调
                        successListener.onSuccess(data);
                    }
                } else if (successListener != null && response != null) {
                    //回调
                    successListener.onSuccess(response);
                }

            }

            @Override
            public void onError(NetroidError error) {
//                UIUtils.showToastSafe("连接服务器失败");
                if (successListener != null){
                    successListener.onError(error.toString());
                }
            }

            @Override
            public void onCancel() {
                UIUtils.showToastSafe("取消访问！");
                if (successListener != null){
                    successListener.onError("取消访问！");
                }
            }

            @Override
            public void onProgressChange(long fileSize, long downloadedSize) {
                LogUtils.e("fileSize: "+ fileSize+"  downloadedSize" + downloadedSize);
            }
        });


        //设置请求超时
        request.setRetryPolicy(new DefaultRetryPolicy(7878, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // 添加到请求队列
        Netroid.getmRequestQueue().add(request);
    }

    /**
     * POST 请求
     */
    private void postRequest() {
        if (requestDataMap != null && !requestDataMap.isEmpty()) {
            //对请求参数进行编码
            String encodedParams = encodeParameters(requestDataMap);
            if (encodedParams.length() > 0) {
                if (-1 == url.indexOf("?"))//=-1表示没有?连接符号
                    url = url + "?" + encodedParams;//如何没有?连接符号则加上?后,再接参数
                else {//如果有?连接符则直接连参数
                    url = url + "&" + encodedParams;
                }
            }
        }

        LogUtils.i("URL = " + url);

        // 请求JSON数据
        StringRequest request = new StringRequest(Request.Method.POST, url, new Listener<String>() {
            @Override
            public void onPreExecute() {
                //UIUtils.showToastSafe("loading......");
            }

            // cancel the dialog with onFinish() callback
            @Override
            public void onFinish() {
//                UIUtils.hintToast();
            }

            @Override
            public void onSuccess(String response) {

                LogUtils.e(response);

                // TODO   请求成功的回调  JSON 解析错误 ——待
                //通过解析工具类 解析 json串得到相应对象
                if (jsonParser != null) {
                    Object data = jsonParser.parserJson(response);
                    if (successListener != null && data != null) {
                        //回调
                        successListener.onSuccess(data);
                    }
                } else if (successListener != null && response != null) {
                    //回调
                    successListener.onSuccess(response);
                }

            }

            @Override
            public void onError(NetroidError error) {
                UIUtils.showToastSafe("请求失败...");
                if (successListener != null) {
                    //回调
                    successListener.onSuccess(error.toString());
                }
            }

            @Override
            public void onCancel() {
                UIUtils.showToastSafe("取消访问！");
                if (successListener != null) {
                    //回调
                    successListener.onSuccess("取消访问！");
                }
            }
        });


        // 添加到请求队列
        Netroid.getmRequestQueue().add(request);
    }


    /**
     * 对请求参数进行编码
     */
    private String encodeParameters(Map<String, String> map) {
        StringBuffer buf = new StringBuffer();
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = (String) map.get(key);

            if ((key == null) || ("".equals(key)) || (value == null)
                    || ("".equals(value))) {
                continue;
            }
            if (i != 0)
                buf.append("&");
            try {
                buf.append(URLEncoder.encode(key, "UTF-8")).append("=")
                        .append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            i++;
        }
        return buf.toString();
    }
}
