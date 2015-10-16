package com.wt_sky.okHttp.network.base;


import com.wt_sky.okHttp.network.interfaces.ResponseStatus;

/**
 * Created by wentao on 15/10/13.
 */
public class BaseResponseValue {
    public int responseStatus;

    public int responseCode;

    public String responseJson;

    public boolean isSuccessed() {
        return responseStatus == ResponseStatus.SUCCESS;
    }

    public boolean isReqFailed() {
        return responseStatus == ResponseStatus.REQ_FAILURE;
    }

    public boolean isParFailed() {
        return responseStatus == ResponseStatus.PAR_FAILURE;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("responseCode = " + responseCode)
                .append("; responseStatus = " + responseStatus)
                .append(responseJson == null ? "; responseJson is null" : "; " + responseJson).toString();
    }
}
