package com.github.simonoppowa.popularmoviesappstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Simon on 14.03.2018.
 */

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String originalTitle;
    private String overview;
    private String imagePath;
    private String userRating;
    private Date releaseDate;

    //Parcelable constructor
    private Movie(Parcel input) {
        id = input.readInt();
        title = input.readString();
        originalTitle = input.readString();
        overview = input.readString();
        imagePath = input.readString();
        userRating = input.readString();
        releaseDate = new Date(input.readLong());
    }


    public Movie(int id, String title, String originalTitle, String overview, String poster_path, String userRating, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.imagePath = poster_path;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setImage(String poster_path) {
        this.imagePath = poster_path;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseYearString() {
        SimpleDateFormat simpleDateRelease = new SimpleDateFormat("yyyy", Locale.getDefault());

        return simpleDateRelease.format(releaseDate);
    }

    public java.sql.Date getSQLDate() {
        return new java.sql.Date(releaseDate.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeString(imagePath);
        parcel.writeString(userRating);
        parcel.writeLong(releaseDate.getTime());

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {


        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

}

