package com.mobilprog.tadam.mobilprog.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thoma on 2017. 04. 19..
 */

public class Partners implements Parcelable{

    private String picture;
    private String name;
    private String lastMessage;

    public Partners(String picture, String name, String lastMessage) {
        this.picture = picture;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    protected Partners(Parcel in) {
        picture = in.readString();
        name = in.readString();
        lastMessage = in.readString();
    }

    public static final Creator<Partners> CREATOR = new Creator<Partners>() {
        @Override
        public Partners createFromParcel(Parcel in) {
            return new Partners(in);
        }

        @Override
        public Partners[] newArray(int size) {
            return new Partners[size];
        }
    };

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() { return lastMessage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(picture);
        parcel.writeString(name);
        parcel.writeString(lastMessage);
    }

}
