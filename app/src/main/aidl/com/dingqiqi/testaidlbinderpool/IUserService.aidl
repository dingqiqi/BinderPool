package com.dingqiqi.testaidlbinderpool;

import com.dingqiqi.testaidlbinderpool.User;

interface IUserService {
    int insertUser(in User user);

    User getUser(in String name);

    int add(in int a,in int b);
}
