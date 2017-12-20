package com.bniky.nicholas.movies.helper;

import com.bniky.nicholas.movies.contract.FavouriteMovieContract.MovieEntry;

/**
 * Created by Nicholas on 20/12/2017.
 */

public class ProjectHelper {

    //API KEY
    private static final String API_KEY = "";
    //Screen position
    private static final String BASE_SORT_URL = "http://api.themoviedb.org/3/movie";
    //Popular movie query
    private static final String POPULAR_MOVIES_TAG = "/popular?api_key=";
    //Top rated movie query
    private static final String TOP_RATED_MOVIES_TAG = "/top_rated?api_key=";

    private static final String POPULAR_MOVIES_URL = BASE_SORT_URL + POPULAR_MOVIES_TAG + API_KEY;
    private static final String TOP_RATED_MOVIES_URL = BASE_SORT_URL + TOP_RATED_MOVIES_TAG + API_KEY;

    private final static int popularId = 0;
    private final static int topRatedId = 1;

    private static final String [] PROJECTION = {MovieEntry._ID, MovieEntry.COLUMN_MOVIE_ID, MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_IMAGE, MovieEntry.COLUMN_VOTE, MovieEntry.COLUMN_BACK_DROP_IMG, MovieEntry.COLUMN_OVER_VIEW,
            MovieEntry.COLUMN_RELEASE_OF_FILM};

    public static String[] getPROJECTION() {
        return PROJECTION;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getBaseSortUrl() {
        return BASE_SORT_URL;
    }

    public static String getPopularMoviesTag() {
        return POPULAR_MOVIES_TAG;
    }

    public static String getTopRatedMoviesTag() {
        return TOP_RATED_MOVIES_TAG;
    }

    public static String getPopularMoviesUrl() {
        return POPULAR_MOVIES_URL;
    }

    public static String getTopRatedMoviesUrl() {
        return TOP_RATED_MOVIES_URL;
    }

    public static int getPopularId() {
        return popularId;
    }

    public static int getTopRatedId() {
        return topRatedId;
    }

}
