package com.github.simonoppowa.popularmoviesappstage1.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.github.simonoppowa.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_MOVIES_URL = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String FAVORITES_TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_ORIGINAL_MOVIE_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        public static final String COLUMN_MOVIE_IMAGE_PATH = "movieImagePath";
        public static final String COLUMN_MOVIE_USER_RATING = "movieUserRating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_DATE_ADDED = "dateAdded";
    }
}
