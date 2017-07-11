package com.dingqiqi.testaidlbinderpool.activity;

import android.os.Handler;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dingqiqi.testaidlbinderpool.Book;
import com.dingqiqi.testaidlbinderpool.IBookService;
import com.dingqiqi.testaidlbinderpool.IUserService;
import com.dingqiqi.testaidlbinderpool.R;
import com.dingqiqi.testaidlbinderpool.util.BinderPool;

public class MainActivity extends AppCompatActivity {

    private IBookService mIBookService;
    private IUserService mIUserService;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //耗时操作,不能放在主线程中,开一个子线程初始化,可以放在application中去绑定service
                getBookService();
                getUserService();
            }
        }).start();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Book book = new Book("开发艺术探索!", 100);
                try {
                    int index = getBookService().insertBook(book);

                    Log.i("aaa", "插入书籍成功!");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_query:
                try {
                    Book getBook = getBookService().getBook("test");
                    if (getBook == null) {
                        Log.i("aaa", "获取书籍fail!");
                    } else {
                        Log.i("aaa", "获取书籍成功!  " + getBook.getName());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_jisuan:
                try {
                    int result = getUserService().add(4, 2);
                    Log.i("aaa", "计算成功!  " + result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    /**
     * 获取图书binder对象
     *
     * @return
     */
    public IBookService getBookService() {
        if (mIBookService == null) {
            try {
                mIBookService = IBookService.Stub.asInterface(BinderPool.getInstance(this).queryBinder(BinderPool.TYPE_BOOK));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return mIBookService;
    }

    /**
     * 获取用户binder对象
     *
     * @return
     */
    public IUserService getUserService() {
        if (mIUserService == null) {
            try {
                mIUserService = IUserService.Stub.asInterface(BinderPool.getInstance(this).queryBinder(BinderPool.TYPE_USER));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return mIUserService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BinderPool.getInstance(this).unBindService();
    }
}
