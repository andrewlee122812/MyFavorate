package com.jin.idcardrecognize_rebuild.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YaLin on 2015/9/15.
 */
public class RecRect implements Parcelable {
    public int height;
    public int left;
    public int top;
    public int width;

    protected RecRect(Parcel in) {
        height = in.readInt();
        left = in.readInt();
        top = in.readInt();
        width = in.readInt();
    }

    public static final Creator<RecRect> CREATOR = new Creator<RecRect>() {
        @Override
        public RecRect createFromParcel(Parcel in) {
            return new RecRect(in);
        }

        @Override
        public RecRect[] newArray(int size) {
            return new RecRect[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(height);
        dest.writeInt(left);
        dest.writeInt(top);
        dest.writeInt(width);
    }
}
