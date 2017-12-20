package com.bniky.nicholas.movies.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.bniky.nicholas.movies.data.MovieTrailer;

import java.util.List;

import Utilities.NetworkHelper;

/**
 * Created by Nicholas on 05/12/2017.
 */

public class TrailerLoader extends AsyncTaskLoader<List<MovieTrailer>> {

    private String className = TrailerLoader.class.getName();
    String url;

    public TrailerLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<MovieTrailer> loadInBackground() {
        Log.i("TrailerLoader", url);
        List<MovieTrailer> TrailerList = NetworkHelper.extractData(url);
        return TrailerList;
    }
}
