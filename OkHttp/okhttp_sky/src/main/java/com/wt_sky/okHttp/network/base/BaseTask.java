package com.wt_sky.okHttp.network.base;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.wt_sky.okHttp.network.OkHttpHelper;
import com.wt_sky.okHttp.network.interfaces.ITask;
import com.wt_sky.okHttp.network.interfaces.RequestMethod;
import com.wt_sky.okHttp.network.interfaces.ResponseStatus;

import java.io.IOException;
import java.util.Map;

/**
 * Created by wentao on 15/10/13.
 */
public class BaseTask implements ITask {

    private static final String TAG = "RequestCallBack";

    private Object tag;

    private int requestCode;

    private String url;

    private Map<String, String> map;

    private RequestBody body;

    private Handler handler;

    private RequestMethod requestMethod;

    public static BaseTask createTask(RequestBody body, int requestCode,
                                      RequestMethod requestMethod, String url, Handler handler) {
        BaseTask task = new BaseTask();
        task.body = body;
        task.requestCode = requestCode;
        task.requestMethod = requestMethod;
        task.handler = handler;
        task.url = url;
        return task;
    }

    public static BaseTask createGetTask(Map<String, String> map, int requestCode, String url, Handler handler) {
        BaseTask task = new BaseTask();
        task.requestCode = requestCode;
        task.requestMethod = RequestMethod.GET;
        task.map = map;
        task.handler = handler;
        task.url = url;
        return task;
    }

    @Override
    public void execute() {
        if (OkHttpHelper.isInitialize()) {
            throw new IllegalStateException("OkHttpHelper must be  initialize");
        }
        switch (requestMethod) {
            case GET:
                OkHttpHelper.getInstance().doGet(url, map, callback);
                break;
            case POST:
                OkHttpHelper.getInstance().doPost(url, body, callback);
                break;
            case DELETE:
                OkHttpHelper.getInstance().doDelete(url, body, callback);
                break;
            case PUT:
                OkHttpHelper.getInstance().doPut(url, body, callback);
                break;
        }
    }

    @Override
    public void cancel() {

    }

    Callback callback = new Callback() {
        @Override
        public void onFailure(Request request, IOException e) {
            if (handler == null) return;
            BaseResponseValue responseValue = new BaseResponseValue();
            responseValue.responseStatus = ResponseStatus.REQ_FAILURE;
            responseValue.responseJson = e.getMessage();

            Message msg = handler.obtainMessage();
            msg.obj = responseValue;
            msg.arg1 = requestCode;
            handler.sendMessage(msg);
        }

        @Override
        public void onResponse(Response response) throws IOException {
            if (handler == null) return;
            BaseResponseValue responseValue = new BaseResponseValue();
            responseValue.responseJson = null;
            responseValue.responseStatus = ResponseStatus.PAR_FAILURE;
            responseValue.responseCode = -1;
            try {
                responseValue.responseJson = response.body().string();
                responseValue.responseCode = response.code();
                responseValue.responseStatus = ResponseStatus.SUCCESS;
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
            Log.i(TAG, responseValue.toString());

            Message msg = handler.obtainMessage();
            msg.obj = responseValue;
            msg.arg1 = requestCode;
            handler.sendMessage(msg);
        }
    };
}
