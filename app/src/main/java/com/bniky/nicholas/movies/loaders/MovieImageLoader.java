package com.bniky.nicholas.movies.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;


import com.bniky.nicholas.movies.data.MovieImageData;

import java.util.List;

import Utilities.NetworkHelper;

/**
 * Created by Nicholas on 23/09/2017.
 */

public class MovieImageLoader extends AsyncTaskLoader<List<MovieImageData>> {

    private final String LOG_TAG = MovieImageLoader.class.getName();
    private String url;

    public MovieImageLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "ON-START-LOADING");
        forceLoad();
    }

    @Override
    public List<MovieImageData> loadInBackground() {
        List <MovieImageData> storeMoviePicture = NetworkHelper.extractData(url);
        return storeMoviePicture;
    }
}
