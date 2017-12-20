package com.bniky.nicholas.movies.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import com.bniky.nicholas.movies.Data.ReviewsOfMovie;

import java.util.List;

import Utilities.NetworkHelper;

/**
 * Created by Nicholas on 08/12/2017.
 */

public class ReviewLoader extends AsyncTaskLoader<List<ReviewsOfMovie>> {

    private String className = ReviewLoader.class.getName();
    String url;

    public ReviewLoader(Context context, String url){
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ReviewsOfMovie> loadInBackground() {
        Log.i(className, url);
        List<ReviewsOfMovie> reviewsOfMovie = NetworkHelper.extractData(url);
        return reviewsOfMovie;
    }
}
