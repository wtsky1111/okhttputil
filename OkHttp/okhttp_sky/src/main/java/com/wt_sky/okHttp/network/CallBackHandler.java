package com.wt_sky.okHttp.network;

import android.os.Handler;
import android.os.Message;

import com.wt_sky.okHttp.network.interfaces.CallBackMessage;

import java.lang.ref.WeakReference;


/**
 * Created by wentao on 15/10/14.
 */
public final class CallBackHandler extends Handler {
    WeakReference<CallBackMessage> obj;

    public CallBackHandler(WeakReference<CallBackMessage> obj) {
        this.obj = obj;
    }

    @Override
    public void handleMessage(Message msg) {
        CallBackMessage callBackMessage = obj.get();
        if (callBackMessage != null) callBackMessage.handlerMessage(msg);
    }
}