package com.mobilprog.tadam.mobilprog.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thoma on 2017. 04. 29..
 */

public class User implements Parcelable{

    private String uid;
    private String username;
    private String email;

    public User() {

    }

    public User(String uid ,String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
    }

    protected User(Parcel in) {
        uid = in.readString();
        username = in.readString();
        email = in.readString();
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

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() { return uid; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(username);
        parcel.writeString(email);
    }
}
