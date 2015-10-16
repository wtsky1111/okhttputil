package com.wt_sky.okHttp.base;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.wt_sky.okHttp.network.CallBackHandler;
import com.wt_sky.okHttp.network.interfaces.CallBackMessage;

import java.lang.ref.WeakReference;


/**
 * Created by wentao on 15/10/14.
 */
public class BaseActivity extends AppCompatActivity implements CallBackMessage {

    protected CallBackHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new CallBackHandler(new WeakReference<CallBackMessage>(this));
    }

    @Override
    public void handlerMessage(Message msg) {

    }
}
