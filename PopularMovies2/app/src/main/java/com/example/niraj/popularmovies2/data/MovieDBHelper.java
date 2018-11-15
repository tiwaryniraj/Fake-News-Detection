package com.github.simonoppowa.popularmoviesappstage1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.github.simonoppowa.popularmoviesappstage1.data.MovieContract.MovieEntry.*;

public class MovieDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 2;


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " +
                        FAVORITES_TABLE_NAME + " ( " +
                        COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY ," +
                        COLUMN_MOVIE_TITLE + " STRING NOT NULL ," +
                        COLUMN_ORIGINAL_MOVIE_TITLE + " STRING NOT NULL ," +
                        COLUMN_MOVIE_OVERVIEW + " STRING NOT NULL ," +
                        COLUMN_MOVIE_IMAGE_PATH + " STRING NOT NULL ," +
                        COLUMN_MOVIE_RELEASE_DATE + " DATE NOT NULL ," +
                        COLUMN_MOVIE_USER_RATING + " STRING NOT NULL ," +
                        COLUMN_DATE_ADDED + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + " );";

        Log.d("MYTAG",  SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
