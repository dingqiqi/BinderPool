package com.dingqiqi.testaidlbinderpool.impl;

import android.os.RemoteException;
import android.util.Log;

import com.dingqiqi.testaidlbinderpool.Book;
import com.dingqiqi.testaidlbinderpool.IBookService;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/7/11.
 */
public class BookServiceImpl extends IBookService.Stub {

    private CopyOnWriteArrayList<Book> mList = new CopyOnWriteArrayList<Book>();

    @Override
    public Book getBook(String name) throws RemoteException {
        Log.i("aaa", "service  getBook ");
        return mList.get(mList.size() - 1);
    }

    @Override
    public int insertBook(Book book) throws RemoteException {
        Log.i("aaa", "service  insertBook ");
        mList.add(book);
        return 1;
    }
}
