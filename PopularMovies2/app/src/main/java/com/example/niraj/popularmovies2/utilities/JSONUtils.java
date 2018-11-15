package com.github.simonoppowa.popularmoviesappstage1.utilities;

import android.util.Log;

import com.github.simonoppowa.popularmoviesappstage1.model.Movie;
import com.github.simonoppowa.popularmoviesappstage1.model.Review;
import com.github.simonoppowa.popularmoviesappstage1.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Simon on 14.03.2018.
 */

public class JSONUtils {

    private static final String JSON_RESULTS_TAG = "results";

    //movie tags
    private static final String JSON_ID_TAG = "id";
    private static final String JSON_TITLE_TAG = "title";
    private static final String JSON_ORIGINAL_TITLE_TAG = "original_title";
    private static final String JSON_OVERVIEW_TAG = "overview";
    private static final String JSON_IMAGE_PATH_TAG = "poster_path";
    private static final String JSON_USER_RATING_TAG = "vote_average";
    private static final String JSON_USER_RELEASED_TAG = "release_date";

    //video tags
    private static final String JSON_KEY_TAG = "key";
    private static final String JSON_NAME_TAG = "name";
    private static final String JSON_SITE_TAG = "site";
    private static final String JSON_TYPE_TAG = "type";

    //review tags
    private static final String JSON_AUTHOR_TAG = "author";
    private static final String JSON_CONTENT_TAG = "content";

    public static List<Movie> getMovieListFromJSON(String jsonString) throws JSONException {

        //Movie attributes
        int id;
        String title, originalTitle, overview, imagePath, fullImagePath, releaseDateString, userRating;
        Date releaseDate;

        //MovieList
        List<Movie> movieList = new ArrayList<>();

        //full Json object
        JSONObject fullJson = new JSONObject(jsonString);

        Log.d("MYTAG", fullJson.toString(4));

        //results Json array
        JSONArray resultsJsonArray = fullJson.getJSONArray(JSON_RESULTS_TAG);

        for(int i = 0; i < resultsJsonArray.length(); i++) {

            //movie id
            id = resultsJsonArray.getJSONObject(i).getInt(JSON_ID_TAG);

            //movie title
            title = resultsJsonArray.getJSONObject(i).getString(JSON_TITLE_TAG);

            //movie originalTitle
            originalTitle = resultsJsonArray.getJSONObject(i).getString(JSON_ORIGINAL_TITLE_TAG);

            //movie overview
            overview = resultsJsonArray.getJSONObject(i).getString(JSON_OVERVIEW_TAG);

            //movie imagePath
            imagePath = resultsJsonArray.getJSONObject(i).getString(JSON_IMAGE_PATH_TAG);
            fullImagePath = NetworkUtils.buildMovieImageUrlString(imagePath);

            //movie userRating
            userRating =  resultsJsonArray.getJSONObject(i).getString(JSON_USER_RATING_TAG);

            //movie releaseDate
            releaseDateString = resultsJsonArray.getJSONObject(i).getString(JSON_USER_RELEASED_TAG);
            releaseDate = convertDate(releaseDateString);

            //movie object
            Movie newMovie = new Movie(id, title, originalTitle, overview, fullImagePath, userRating, releaseDate);

            movieList.add(newMovie);
        }
        return movieList;
    }

    private static Date convertDate(String releaseDateString) {
        SimpleDateFormat simpleReleaseDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date releaseDate = null;
        try {
            releaseDate = simpleReleaseDate.parse(releaseDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return releaseDate;
    }

    public static List<Video> getMovieVideosFromJSON(String jsonString) throws JSONException {

        //video attributes
        String key, site, name, type;

        List<Video> videoList = new ArrayList<>();

        //full json object
        JSONObject fullJson = new JSONObject(jsonString);

        //results Json array
        JSONArray resultsJsonArray = fullJson.getJSONArray(JSON_RESULTS_TAG);

        Log.d("MYTAG", fullJson.toString(4));
        for(int i = 0; i < resultsJsonArray.length(); i++) {
            //video key
            key = resultsJsonArray.getJSONObject(i).getString(JSON_KEY_TAG);

            //video site
            site = resultsJsonArray.getJSONObject(i).getString(JSON_SITE_TAG);

            //video name
            name = resultsJsonArray.getJSONObject(i).getString(JSON_NAME_TAG);

            //video type
            type = resultsJsonArray.getJSONObject(i).getString(JSON_TYPE_TAG);

            Video newVideo = new Video(key, site, name, type);

            videoList.add(newVideo);
        }
        return videoList;
    }

    public static List<Review> getMovieReviewsFromJSON(String jsonString) throws JSONException {

        //review attributes
        int movieId;
        String author, content;

        List<Review>  reviewList = new ArrayList<>();

        //full json object
        JSONObject fullJson = new JSONObject(jsonString);

        //results Json array
        JSONArray resultsJsonArray = fullJson.getJSONArray(JSON_RESULTS_TAG);

        Log.d("MYTAG", fullJson.toString(4));

        for(int i = 0; i < resultsJsonArray.length(); i++) {
            //movieId
            movieId = fullJson.getInt(JSON_ID_TAG);

            //review author
            author = resultsJsonArray.getJSONObject(i).getString(JSON_AUTHOR_TAG);

            //review content
            content = resultsJsonArray.getJSONObject(i).getString(JSON_CONTENT_TAG);

            Review newReview = new Review(movieId, author, content);

            reviewList.add(newReview);
        }
        return reviewList;
    }
}
