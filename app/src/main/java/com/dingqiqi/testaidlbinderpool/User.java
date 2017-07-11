package com.dingqiqi.testaidlbinderpool;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 只能跟aidl文件同目录下
 * Created by Administrator on 2017/7/11.
 */
public class User implements Parcelable{

    private String name;

    protected User(Parcel in) {
        name = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
