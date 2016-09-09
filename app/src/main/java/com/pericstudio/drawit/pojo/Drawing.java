package com.pericstudio.drawit.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.cloudmine.api.db.LocallySavableCMObject;

public class Drawing extends LocallySavableCMObject implements Parcelable {

    public static final String CLASS_NAME = "Drawing";

    public static final Parcelable.Creator<Drawing> CREATOR
            = new Parcelable.Creator<Drawing>() {
        public Drawing createFromParcel(Parcel in) {
            return new Drawing(in);
        }

        public Drawing[] newArray(int size) {
            return new Drawing[size];
        }
    };


    private String title;
    private String description;

    public Drawing() {
        super();
    }

    public Drawing(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public Drawing(Parcel parcel) {
        title = parcel.readString();
        description = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }

    @Override
    public String getClassName() {
        return CLASS_NAME;
    }
}
