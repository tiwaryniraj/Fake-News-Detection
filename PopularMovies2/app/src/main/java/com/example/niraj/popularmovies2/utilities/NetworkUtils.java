package com.github.simonoppowa.popularmoviesappstage1.utilities;

import android.net.Uri;
import android.util.Log;

import com.github.simonoppowa.popularmoviesappstage1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Simon on 14.03.2018.
 */

public final class NetworkUtils {

    //MovieUrl String Constants
    private static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIEDB_POPULAR_TAG = "popular";
    private static final String MOVIEDB_RATING_TAG = "top_rated";

    private static final String MOVIEDB_API_KEY_TAG = "api_key";
    private static final String MOVIEDB_LANGUAGE_TAG = "language";
    private static final String MOVIEDB_PAGE_TAG = "page";

    private static final String LANGUAGE_PARAM = "en-US";

    //MovieImageUrl String Constants
    private static final String MOVIEDB_IMAGE_URL = "https://image.tmdb.org/t/p/";
    private static final String MOVIEDB_IMAGE_SIZE_TAG = "w185";

    //MovieVideos String Constants
    private static final String MOVIEDB_VIDEOS_TAG = "videos";

    //MovieReviewUrl String Constants
    private static final String MOVIEDB_REVIEWS_TAG = "reviews";

    private static String myApiKey;

    public static URL buildPopularMoviesUrlByPopularity(int page) {

        myApiKey = BuildConfig.MY_MOVIE_DB_API_KEY;

        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL + MOVIEDB_POPULAR_TAG).buildUpon()
                .appendQueryParameter(MOVIEDB_API_KEY_TAG, myApiKey)
                .appendQueryParameter(MOVIEDB_LANGUAGE_TAG, LANGUAGE_PARAM)
                .appendQueryParameter(MOVIEDB_PAGE_TAG, Integer.toString(page))
                .build();

        URL builtUrl = buildUrl(builtUri);

        Log.d("MYTAG",builtUrl.toString());

        return builtUrl;
    }

    public static URL buildPopularMoviesUrlByRating(int page) {
        myApiKey = BuildConfig.MY_MOVIE_DB_API_KEY;
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL + MOVIEDB_RATING_TAG).buildUpon()
                .appendQueryParameter(MOVIEDB_API_KEY_TAG, myApiKey)
                .appendQueryParameter(MOVIEDB_LANGUAGE_TAG, LANGUAGE_PARAM)
                .appendQueryParameter(MOVIEDB_PAGE_TAG, Integer.toString(page))
                .build();

        URL buildUrl = buildUrl(builtUri);

        Log.d("MYTAG", buildUrl.toString());

        return buildUrl;
    }

    public static URL buildMovieReviewsUrl(int movieId) {
        myApiKey = BuildConfig.MY_MOVIE_DB_API_KEY;

        String reviewUrl = MOVIEDB_BASE_URL +  String.valueOf(movieId) + "/" + MOVIEDB_REVIEWS_TAG;

        Uri builtUri = Uri.parse(reviewUrl).buildUpon()
                .appendQueryParameter(MOVIEDB_API_KEY_TAG, myApiKey)
                .build();
        URL builtUrl = buildUrl(builtUri);

        Log.d("MYTAG",  builtUri.toString());

        return builtUrl;
    }

    public static URL buildMovieVideosUrl(int movieId) {
        myApiKey = BuildConfig.MY_MOVIE_DB_API_KEY;

        String videosUrl = MOVIEDB_BASE_URL + String.valueOf(movieId) + "/" + MOVIEDB_VIDEOS_TAG;

        Uri builtUri = Uri.parse(videosUrl).buildUpon()
                .appendQueryParameter(MOVIEDB_API_KEY_TAG, myApiKey)
                .build();
        URL builtUrl = buildUrl(builtUri);

        Log.d("MYTAG", builtUrl.toString());

        return builtUrl;

    }

    public static URL buildUrl(Uri uri) {
        URL newUrl = null;

        try {
            newUrl = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return newUrl;
    }

    public static String buildMovieImageUrlString(String imagePath) {
        return MOVIEDB_IMAGE_URL + MOVIEDB_IMAGE_SIZE_TAG + imagePath;
    }


    public static String getResponseFromHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput) {
                return scanner.next();
            }
            else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
