package com.jin.idcardrecognize_rebuild.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by YaLin on 2015/9/15.
 */
public class RetData implements Parcelable {
    public RecRect rect;
    public String word;

    protected RetData(Parcel in) {
        rect = in.readParcelable(RecRect.class.getClassLoader());
        word = in.readString();
    }

    public static final Creator<RetData> CREATOR = new Creator<RetData>() {
        @Override
        public RetData createFromParcel(Parcel in) {
            return new RetData(in);
        }

        @Override
        public RetData[] newArray(int size) {
            return new RetData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(rect, flags);
        dest.writeString(word);
    }
}
