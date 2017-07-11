package com.dingqiqi.testaidlbinderpool;

import android.os.IBinder;

interface IBinderPool {
    IBinder queryBinder(in int type);
}
