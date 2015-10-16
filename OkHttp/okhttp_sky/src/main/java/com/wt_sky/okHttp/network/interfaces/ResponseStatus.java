package com.wt_sky.okHttp.network.interfaces;

/**
 * Created by wentao on 15/10/12.
 */
public class ResponseStatus {
    public static final int SUCCESS = 0;
    public static final int REQ_FAILURE = 1;//请求失败
    public static final int PAR_FAILURE = 2;//请求成功，但是返回值解析失败
}
