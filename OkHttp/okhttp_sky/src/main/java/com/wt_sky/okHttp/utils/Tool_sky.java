package com.wt_sky.okHttp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.wt_sky.okHttp.network.OkHttpHelper;

/**
 * Created by wentao on 15/10/9.
 */
public class Tool_sky {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMOBILE_MODEL() {
        return android.os.Build.MODEL == null ? "-1" : android.os.Build.MODEL;
    }

    /**
     * 获取设备系统版本
     */
    public static String getSYSTEM() {
        return android.os.Build.VERSION.RELEASE == null ? "-1"
                : android.os.Build.VERSION.RELEASE;
    }

    /**
     * 判断网络连接是否存在
     */
    public static boolean hasInternet(Context activity) {

        ConnectivityManager manager = (ConnectivityManager) activity

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            if (OkHttpHelper.IS_SHOW_TOAST)
                Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            if (OkHttpHelper.IS_SHOW_TOAST)
                Toast.makeText(activity, "网络未连接", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }
}
