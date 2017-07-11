package com.dingqiqi.testaidlbinderpool;

import com.dingqiqi.testaidlbinderpool.Book;

interface IBookService {
    Book getBook(in String name);

    int insertBook(in Book book);
}
