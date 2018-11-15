package com.github.simonoppowa.popularmoviesappstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private int movieId;

    private String author;
    private String content;

    //Parcelable constructor
    private Review(Parcel input) {
        movieId = input.readInt();
        author = input.readString();
        content = input.readString();
    }


    public Review(int movieId, String author, String content) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
