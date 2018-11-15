package com.github.simonoppowa.popularmoviesappstage1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.simonoppowa.popularmoviesappstage1.data.MovieContract;
import com.github.simonoppowa.popularmoviesappstage1.model.Movie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.simonoppowa.popularmoviesappstage1.data.MovieContract.MovieEntry.*;

public class FavoritesActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{

    private static final String MOVIE_KEY = MainActivity.MOVIE_KEY;

    private RecyclerView mFavoritesRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessage;

    private List<Movie> mFavoritesMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setTitle(R.string.favorites_activity_title);


        mFavoritesRecyclerView = findViewById(R.id.favorite_movies_recycler_view);
        mErrorMessage = findViewById(R.id.no_favorites);

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mFavoritesRecyclerView.setLayoutManager(mLinearLayoutManager);

        mMovieAdapter = new MovieAdapter(this, this, mFavoritesMoviesList);
        mFavoritesRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavoritesMoviesList = new ArrayList<>();
        loadFavoritesMovies();
        if(mFavoritesMoviesList.isEmpty()) {
            showErrorMessage();
        }
        mMovieAdapter.setPopularMoviesList(mFavoritesMoviesList);
    }

    private void showErrorMessage() {
        mFavoritesRecyclerView.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loadFavoritesMovies() {
        Cursor moviesCursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_MOVIES_URL,
                null,
                null,
                null,
                COLUMN_DATE_ADDED + " DESC");

        while(moviesCursor != null && moviesCursor.moveToNext()) {
            Long releaseDateString = moviesCursor.getLong(moviesCursor.getColumnIndex(COLUMN_MOVIE_RELEASE_DATE));
            Date releaseDate = new Date(releaseDateString);

            Movie newMovie = new Movie(
                    moviesCursor.getInt(moviesCursor.getColumnIndex(COLUMN_MOVIE_ID)),
                    moviesCursor.getString(moviesCursor.getColumnIndex(COLUMN_MOVIE_TITLE)),
                    moviesCursor.getString(moviesCursor.getColumnIndex(COLUMN_ORIGINAL_MOVIE_TITLE)),
                    moviesCursor.getString(moviesCursor.getColumnIndex(COLUMN_MOVIE_OVERVIEW)),
                    moviesCursor.getString(moviesCursor.getColumnIndex(COLUMN_MOVIE_IMAGE_PATH)),
                    moviesCursor.getString(moviesCursor.getColumnIndex(COLUMN_MOVIE_USER_RATING)),
                    releaseDate);
            mFavoritesMoviesList.add(newMovie);
        }

        mMovieAdapter.setPopularMoviesList(mFavoritesMoviesList);

        moviesCursor.close();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);

        detailIntent.putExtra(MOVIE_KEY, mFavoritesMoviesList.get(clickedItemIndex));

        startActivity(detailIntent);
    }
}
