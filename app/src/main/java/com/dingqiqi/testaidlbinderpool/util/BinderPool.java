package com.dingqiqi.testaidlbinderpool.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.dingqiqi.testaidlbinderpool.IBinderPool;
import com.dingqiqi.testaidlbinderpool.impl.BookServiceImpl;
import com.dingqiqi.testaidlbinderpool.impl.UserServiceImpl;
import com.dingqiqi.testaidlbinderpool.service.CustomService;

import java.util.concurrent.CountDownLatch;

/**
 * binder池 提供binder实现
 * Created by dingqiqi on 2017/7/11.
 */
public class BinderPool {
    //图书类型
    public static final int TYPE_BOOK = 1001;
    //用户类型
    public static final int TYPE_USER = 1002;
    //实例
    private static BinderPool mInstance;
    //binder连接池
    private IBinderPool mIBinder;

    //同步工具类
    private static CountDownLatch mDownLatch;

    private Context mContext;

    private BinderPool(Context context) {
        mContext = context;
        bindMyService(context);
    }

    public static BinderPool getInstance(Context context) {
        if (mInstance == null) {
            synchronized (BinderPool.class) {
                mInstance = new BinderPool(context);
            }
        }

        return mInstance;
    }

    /**
     * 绑定aidl服务
     *
     * @param context
     */
    private void bindMyService(Context context) {
        //初始化同步工具类
        mDownLatch = new CountDownLatch(1);

//        Intent intent = new Intent(context, CustomService.class);
        //模拟绑定第三方服务
        Intent intent = new Intent();
        intent.setAction("com.dingqiqi.testaidlbinderpool.service");
        intent.setPackage("com.dingqiqi.testaidlbinderpool");
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        try {
            //绑定服务异步转同步  主线程的操作会在这个方法上阻塞,直到其他线程完成各自的任务。
            mDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定成功后转化binder
            mIBinder = IBinderPool.Stub.asInterface(service);

            try {
                mIBinder.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            //每调用一次这个方法，初始化的count值就减1。count的值等于0，然后主线程就能通过await()方法，恢复执行自己的任务
            mDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    /**
     * 断开重连
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i("aaa", "binderDied");
            bindMyService(mContext);
        }
    };

    /**
     * 用于客户端查找获取对应的binder
     *
     * @param type
     * @return
     * @throws RemoteException
     */
    public IBinder queryBinder(int type) throws RemoteException {

        if (mIBinder == null) {
            return null;
        }

        //调用的事IBinderPool 的queryBinder方法,实现在BinderPoolImpl中
        return mIBinder.queryBinder(type);
    }

    /**
     * 用于服务端,实现了对应的binder服务
     */
    public static class BinderPoolImpl extends IBinderPool.Stub {

        @Override
        public IBinder queryBinder(int type) throws RemoteException {

            IBinder iBinder = null;
            if (type == TYPE_BOOK) {
                iBinder = new BookServiceImpl();
            } else if (type == TYPE_USER) {
                iBinder = new UserServiceImpl();
            }

            return iBinder;
        }
    }

    /**
     * 断开连接
     */
    public void unBindService() {
        //断开连接监听
        if (mIBinder != null && mIBinder.asBinder() != null) {
            mIBinder.asBinder().unlinkToDeath(mDeathRecipient, 0);
        }
        //断开连接
        mContext.unbindService(mConnection);
    }

}
