package com.github.simonoppowa.popularmoviesappstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.simonoppowa.popularmoviesappstage1.data.MovieContract.MovieEntry;

import java.util.Objects;

import static com.github.simonoppowa.popularmoviesappstage1.data.MovieContract.MovieEntry.*;

public class MovieContentProvider extends ContentProvider {

    private MovieDBHelper mMovieDbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDbHelper = new MovieDBHelper(context);
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case MOVIES:
                returnCursor = db.query(FAVORITES_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = MovieEntry.COLUMN_MOVIE_ID + " =?";
                String[] mSelectionArgs = new String[] {id};

                returnCursor = db.query(FAVORITES_TABLE_NAME, projection, mSelection, mSelectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported query action " + uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case  MOVIES:
                long id = db.insert(FAVORITES_TABLE_NAME, null, contentValues);

                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_MOVIES_URL, id);
                } else {
                    throw new android.database.SQLException("Failed to insert movie");
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri " + uri);
        }

        Log.d("MYTAG","Added new movie");

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int moviesDeleted;

        switch (match) {
            case MOVIES:
                moviesDeleted = db.delete(FAVORITES_TABLE_NAME, null, null);
                Log.d("MYTAG", "Deleted all favorites");
                break;

            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                moviesDeleted = db.delete(FAVORITES_TABLE_NAME, COLUMN_MOVIE_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Couldn't delete");
        }

        Log.d("MYTAG", moviesDeleted + " movie(s) deleted");

        if(moviesDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //all movies
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);

        //one movie
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public void shutdown() {
        mMovieDbHelper.close();
        super.shutdown();
    }
}
