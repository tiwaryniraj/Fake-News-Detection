package com.github.simonoppowa.popularmoviesappstage1;

import android.os.AsyncTask;

import com.github.simonoppowa.popularmoviesappstage1.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Simon on 14.03.2018.
 */

public class FetchPopularMoviesTask extends AsyncTask<URL, Void, String>{

    private AsyncTaskCompleteListener listener = null;

    public interface AsyncTaskCompleteListener {
        void asyncTaskProcessFinish(String output);
    }

    public FetchPopularMoviesTask(AsyncTaskCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(URL... urls) {
        URL fetchUrl = urls[0];

        String mFetchResults = null;

        try {
            mFetchResults = NetworkUtils.getResponseFromHttp(fetchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mFetchResults;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.asyncTaskProcessFinish(s);
    }
}
