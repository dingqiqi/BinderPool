package com.dingqiqi.testaidlbinderpool.impl;

import android.os.RemoteException;
import android.util.Log;

import com.dingqiqi.testaidlbinderpool.Book;
import com.dingqiqi.testaidlbinderpool.IBookService;
import com.dingqiqi.testaidlbinderpool.IUserService;
import com.dingqiqi.testaidlbinderpool.User;

/**
 * Created by Administrator on 2017/7/11.
 */
public class UserServiceImpl extends IUserService.Stub {

    @Override
    public int insertUser(User user) throws RemoteException {
        return 0;
    }

    @Override
    public User getUser(String name) throws RemoteException {
        return null;
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        Log.i("aaa", "service add");
        return a + b;
    }
}
