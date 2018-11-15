package com.github.simonoppowa.popularmoviesappstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable{

    private String key;
    private String site;
    private String name;
    private String type;

    //Parcelable constructor
    private Video(Parcel input) {
        key = input.readString();
        site = input.readString();
        name = input.readString();
        type = input.readString();
    }

    public Video(String key, String site, String name, String type) {
        this.key = key;
        this.site = site;
        this.name = name;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(site);
        parcel.writeString(name);
        parcel.writeString(type);
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {

        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        @Override
        public Video[] newArray(int i) {
            return new Video[i];
        }
    };
}
