package com.wt_sky.okHttp.network;


import android.content.Context;
import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.wt_sky.okHttp.utils.Tool_sky;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wentao on 15/10/8.
 */
public class OkHttpHelper {

    public static boolean IS_SHOW_TOAST = true;

    private static final String USER_AGENT = "User-Agent";

    private static OkHttpHelper instance;

    private OkHttpClient mClient;

    private Context mContext;

    private String user_agent = null;

    private OkHttpHelper() {
        mClient = new OkHttpClient();
        mClient.setConnectTimeout(2000, TimeUnit.MILLISECONDS);
        mClient.setReadTimeout(4000, TimeUnit.MILLISECONDS);
        mClient.setWriteTimeout(4000, TimeUnit.MILLISECONDS);
    }

    public static synchronized OkHttpHelper getInstance() {
        if (instance == null) {
            instance = new OkHttpHelper();
        }
        return instance;
    }

    public void initialize(Context context) {
        initialize(context, null);
    }

    public void initialize(Context context, String user_agent) {
        mContext = context;
        mClient.setCookieHandler(new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL));
        this.user_agent = user_agent;
    }

    public void setUser_agent(String user_agent) {
        this.user_agent = user_agent;
    }

    public CookieStore getCookieStore() {
        CookieManager cookieManager = (CookieManager) mClient.getCookieHandler();
        return cookieManager.getCookieStore();
    }

    public void doGet(String url, Map<String, String> map, Callback callBack) {
        if (!Tool_sky.hasInternet(mContext)) return;

        if (map != null && map.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    builder.append(key).append("=").append(value).append("&");
                }
            }
            String string = builder.toString();
            if (string.length() > 0) {
                url += url.contains("?") ? "&" : "?";
                url += string.substring(0, string.length() - 1);
            }
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .get();
        if (user_agent != null) builder.header(USER_AGENT, user_agent);
        Request request = builder.build();
        mClient.newCall(request).enqueue(callBack);
    }

    public void doPost(String url, RequestBody requestBody, Callback callBack) {
        if (!Tool_sky.hasInternet(mContext)) return;
        if (requestBody == null) {
            requestBody = new FormEncodingBuilder().build();
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (user_agent != null) builder.header(USER_AGENT, user_agent);
        Request request = builder.build();
        mClient.newCall(request).enqueue(callBack);
    }

    public void doDelete(String url, RequestBody requestBody, Callback callBack) {
        if (!Tool_sky.hasInternet(mContext)) return;
        if (requestBody == null) {
            requestBody = new FormEncodingBuilder().build();
        }
        Request.Builder builder = new Request.Builder()
                .url(url)
                .delete(requestBody);
        if (user_agent != null) builder.header(USER_AGENT, user_agent);
        Request request = builder.build();
        mClient.newCall(request).enqueue(callBack);
    }

    public void doPut(String url, RequestBody requestBody, Callback callBack) {
        if (!Tool_sky.hasInternet(mContext)) return;
        if (requestBody == null) {
            requestBody = new FormEncodingBuilder().build();
        }

        Request.Builder builder = new Request.Builder()
                .url(url)
                .put(requestBody);
        if (user_agent != null) builder.header(USER_AGENT, user_agent);
        Request request = builder.build();
        mClient.newCall(request).enqueue(callBack);
    }

    public void calcel(Object tag) {
        mClient.cancel(tag);
    }
}
