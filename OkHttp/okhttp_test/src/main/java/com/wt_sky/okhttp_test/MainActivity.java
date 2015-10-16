package com.wt_sky.okhttp_test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wt_sky.okHttp.network.CallBackHandler;
import com.wt_sky.okHttp.network.OkHttpHelper;
import com.wt_sky.okHttp.network.base.BaseResponseValue;
import com.wt_sky.okHttp.network.base.BaseTask;
import com.wt_sky.okHttp.network.interfaces.CallBackMessage;

/**
 * Created by wentao on 15/10/16.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpHelper.getInstance().initialize(this);
        BaseTask.createGetTask(null, 100, "http://www.baidu.com", handler).execute();
    }

    CallBackMessage message = new CallBackMessage() {
        @Override
        public void handlerMessage(Message msg) {
            BaseResponseValue value = (BaseResponseValue) msg.obj;
            Log.i("CallBackHandler", value.toString());
        }
    };

    private Handler handler = new CallBackHandler(new CallBackMessage() {
        @Override
        public void handlerMessage(Message msg) {
            BaseResponseValue value = (BaseResponseValue) msg.obj;
            Log.i("CallBackHandler", value.toString());
        }
    }, false);
}
