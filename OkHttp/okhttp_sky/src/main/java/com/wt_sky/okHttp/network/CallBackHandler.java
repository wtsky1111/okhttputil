package com.wt_sky.okHttp.network;

import android.os.Handler;
import android.os.Message;

import com.wt_sky.okHttp.network.interfaces.CallBackMessage;

import java.lang.ref.WeakReference;


/**
 * Created by wentao on 15/10/14.
 */
public final class CallBackHandler extends Handler {
    WeakReference<CallBackMessage> obj_weak;

    CallBackMessage obj;

    public CallBackHandler(CallBackMessage obj) {
        this(obj, true);
    }

    public CallBackHandler(CallBackMessage obj, boolean isWeakRef) {
        if (isWeakRef) obj_weak = new WeakReference<>(obj);
        else this.obj = obj;
    }

    @Override
    public void handleMessage(Message msg) {
        if (obj != null) obj.handlerMessage(msg);
        else {
            CallBackMessage callBackMessage = obj_weak.get();
            if (callBackMessage != null) callBackMessage.handlerMessage(msg);
        }
    }
}