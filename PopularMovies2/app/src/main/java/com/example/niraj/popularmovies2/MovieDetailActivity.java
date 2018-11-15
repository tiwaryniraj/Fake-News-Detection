package com.github.simonoppowa.popularmoviesappstage1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.simonoppowa.popularmoviesappstage1.data.MovieContract;
import com.github.simonoppowa.popularmoviesappstage1.model.Movie;
import com.github.simonoppowa.popularmoviesappstage1.model.Review;
import com.github.simonoppowa.popularmoviesappstage1.model.Video;
import com.github.simonoppowa.popularmoviesappstage1.utilities.JSONUtils;
import com.github.simonoppowa.popularmoviesappstage1.utilities.NetworkUtils;
import com.github.simonoppowa.popularmoviesappstage1.utilities.YoutubeUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.github.simonoppowa.popularmoviesappstage1.data.MovieContract.MovieEntry.*;

public class MovieDetailActivity extends AppCompatActivity implements VideoAdapter.ListItemClickListener{

    private static final String MOVIE_KEY = MainActivity.MOVIE_KEY;
    private static final String VIDEO_KEY = "videos";
    private static final String REVIEWS_KEY = "reviews";

    private RecyclerView mVideoRecyclerView;
    private LinearLayoutManager mHorizontalLinearLayoutManager;
    private VideoAdapter mVideoAdapter;

    private RecyclerView mReviewRecyclerView;
    private LinearLayoutManager mVerticalLinearLayoutManager;
    private ReviewAdapter mReviewAdapter;

    private List<Video> mVideoList;
    private List<Review> mReviewList;

    private LinearLayout mExtraDetailLayout;
    private ProgressBar mExtraDetailProgressBar;
    private TextView mTitleTV;
    private ImageView mImageIV;
    private TextView mReleaseDateTV;
    private TextView mRatingTV;
    private TextView mOverviewTV;

    private TextView mReviewErrorMessage;
    private TextView mVideoErrorMessage;

    private ImageButton mFavoritesButton;
    private TextView mFavoritesLabel;

    private Movie mSelectedMovie;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mTitleTV = findViewById(R.id.title_tv);
        mImageIV = findViewById(R.id.image_tv);
        mReleaseDateTV = findViewById(R.id.release_date_tv);
        mRatingTV = findViewById(R.id.rating_tv);
        mOverviewTV = findViewById(R.id.overview_tv);

        mReviewErrorMessage = findViewById(R.id.review_error_message);
        mVideoErrorMessage = findViewById(R.id.video_error_message);

        mExtraDetailLayout = findViewById(R.id.movie_extra_detail);
        mExtraDetailProgressBar = findViewById(R.id.extra_detail_progressbar);

        mFavoritesButton = findViewById(R.id.favorite_icon);
        mFavoritesLabel = findViewById(R.id.favorites_label);

        mVideoList = new ArrayList<>();
        mReviewList = new ArrayList<>();

        setTitle(R.string.movie_detail_name);

        Intent intent = getIntent();

        mSelectedMovie = intent.getParcelableExtra(MOVIE_KEY);

        if(mSelectedMovie == null) {
            throw new NullPointerException("No Movie was passed to MovieDetailActivity");
        }

        //video recyclerView
        mVideoRecyclerView = findViewById(R.id.video_item_recycler_view);
        mHorizontalLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        mVideoRecyclerView.setLayoutManager(mHorizontalLinearLayoutManager);

        mVideoAdapter = new VideoAdapter(this, this, mVideoList);

        mVideoRecyclerView.setAdapter(mVideoAdapter);

        //review recyclerView
        mReviewRecyclerView = findViewById(R.id.review_item_recycler_view);
        mVerticalLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(mVerticalLinearLayoutManager);

        mReviewAdapter = new ReviewAdapter(this, mReviewList);
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        mReviewRecyclerView.setNestedScrollingEnabled(false);

        populateMainUI();

        showFavoriteButton();

        //check for rotation
        if(savedInstanceState != null && savedInstanceState.containsKey(VIDEO_KEY) && savedInstanceState.containsKey(REVIEWS_KEY)) {
            mVideoList = savedInstanceState.getParcelableArrayList(VIDEO_KEY);
            mReviewList = savedInstanceState.getParcelableArrayList(REVIEWS_KEY);
            checkVideoList();
            checkReviewList();
            showExtraDetails();
        } else {
            new ExtraDetailsQueryTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(VIDEO_KEY, (ArrayList<? extends Parcelable>) mVideoList);
        outState.putParcelableArrayList(REVIEWS_KEY, (ArrayList<? extends Parcelable>) mReviewList);
    }

    @SuppressLint("StaticFieldLeak")
    private void populateMainUI() {

        //mTitleTextView with original title
        if(mSelectedMovie.getTitle().equals(mSelectedMovie.getOriginalTitle())) {
            mTitleTV.setText(mSelectedMovie.getTitle());
        }
        else {
            mTitleTV.setText(mSelectedMovie.getTitle() + "\n(" + mSelectedMovie.getOriginalTitle() + ")");
        }

        //mImageIV
        Picasso.with(getApplicationContext()).load(mSelectedMovie.getImagePath()).into(mImageIV);

        mReleaseDateTV.setText(mSelectedMovie.getReleaseYearString());

        mOverviewTV.setText(mSelectedMovie.getOverview());

        mRatingTV.setText(mSelectedMovie.getUserRating() + "/10");
    }

    @SuppressLint("StaticFieldLeak")
    private void showFavoriteButton() {
        new AsyncTask<Cursor, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Cursor... cursors) {
                String stringId = Integer.toString(mSelectedMovie.getId());

                Uri uri = MovieContract.MovieEntry.CONTENT_MOVIES_URL;
                uri = uri.buildUpon().appendPath(stringId).build();

                return getContentResolver().query(uri, null, null, null, null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                super.onPostExecute(cursor);

                if(cursor == null || cursor.getCount() == 0) {
                    setButtonNoFavorite();
                } else {
                    setButtonFavorite();
                }

                mFavoritesButton.setVisibility(View.VISIBLE);
                mFavoritesLabel.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private class ExtraDetailsQueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //videos
                URL videosUrl = NetworkUtils.buildMovieVideosUrl(mSelectedMovie.getId());
                String jsonVideosString = NetworkUtils.getResponseFromHttp(videosUrl);
                mVideoList = JSONUtils.getMovieVideosFromJSON(jsonVideosString);

                //reviews
                URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(mSelectedMovie.getId());
                String jsonReviewString = NetworkUtils.getResponseFromHttp(reviewsUrl);
                mReviewList = JSONUtils.getMovieReviewsFromJSON(jsonReviewString);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            checkVideoList();
            checkReviewList();

            showExtraDetails();
        }
    }

    private void showExtraDetails() {
        mExtraDetailLayout.setVisibility(View.VISIBLE);
        mExtraDetailProgressBar.setVisibility(View.GONE);
    }

    private void checkVideoList() {
        //check if videos available
        if (!mVideoList.isEmpty()) {
            mVideoAdapter.setVideoList(mVideoList);
        } else {
            mVideoRecyclerView.setVisibility(View.GONE);
            mVideoErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    private void checkReviewList() {
        //check if reviews available
        if (!mReviewList.isEmpty()) {
            mReviewAdapter.setReviewList(mReviewList);
        } else {
            mReviewRecyclerView.setVisibility(View.GONE);
            mReviewErrorMessage.setVisibility(View.VISIBLE);
        }
        showExtraDetails();
    }

    public void onFavoritesClicked(View view) {
        if(!isFavorite) {

            //add to favorites content provider
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_MOVIE_ID, mSelectedMovie.getId());
            contentValues.put(COLUMN_MOVIE_TITLE, mSelectedMovie.getTitle());
            contentValues.put(COLUMN_ORIGINAL_MOVIE_TITLE, mSelectedMovie.getOriginalTitle());
            contentValues.put(COLUMN_MOVIE_OVERVIEW, mSelectedMovie.getOverview());
            contentValues.put(COLUMN_MOVIE_IMAGE_PATH, mSelectedMovie.getImagePath());
            contentValues.put(COLUMN_MOVIE_USER_RATING, mSelectedMovie.getUserRating());
            contentValues.put(COLUMN_MOVIE_RELEASE_DATE, String.valueOf(mSelectedMovie.getSQLDate()));

            Uri uri = getContentResolver().insert(CONTENT_MOVIES_URL, contentValues);

            if(uri != null) {
                //movie was successfully inserted
                Toast toast = Toast.makeText(this, R.string.add_to_favorites, Toast.LENGTH_SHORT);
                toast.show();

                setButtonFavorite();
            } else {
                throw new UnsupportedOperationException("Couldn't insert to favorites");
            }


        } else {
            Toast toast = Toast.makeText(this,R.string.remove_from_favorites, Toast.LENGTH_SHORT);
            toast.show();

            String stringId = Integer.toString(mSelectedMovie.getId());

            Uri uri = MovieContract.MovieEntry.CONTENT_MOVIES_URL;
            uri = uri.buildUpon().appendPath(stringId).build();

            int deleted = getContentResolver().delete(uri, null, null);

            if(deleted != 0) {
                setButtonNoFavorite();
            } else {
                throw new UnsupportedOperationException("Couldn't delete movie from favorites");
            }
        }
    }

    private void setButtonFavorite() {
        mFavoritesButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_36px));
        mFavoritesLabel.setText(R.string.remove_favorites_label);
        isFavorite = true;
    }

    private void setButtonNoFavorite() {
        mFavoritesLabel.setText(R.string.add_favorites_label);
        mFavoritesButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_36px));
        isFavorite = false;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Uri youtubeLink = Uri.parse(String.valueOf(YoutubeUtils.buildYoutubeVideoLink(mVideoList.get(clickedItemIndex).getKey())));
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeLink);

        startActivity(youtubeIntent);
    }
}
