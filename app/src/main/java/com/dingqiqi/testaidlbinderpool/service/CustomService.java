package com.dingqiqi.testaidlbinderpool.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dingqiqi.testaidlbinderpool.util.BinderPool;

/**
 * 提供服务
 * Created by Administrator on 2017/7/11.
 */
public class CustomService extends Service {
    //实现全在BinderPoolImpl中
    private IBinder mBinderPool = new BinderPool.BinderPoolImpl();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

}
