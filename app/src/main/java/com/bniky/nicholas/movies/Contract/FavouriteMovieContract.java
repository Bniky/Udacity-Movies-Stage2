package com.bniky.nicholas.movies.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Nicholas on 18/12/2017.
 */

public final class FavouriteMovieContract {

    public static final String CONTENT_AUTHORITY = "com.bniky.nicholas.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie_favourite";



    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIE);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "favourites";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_VOTE = "vote_average";
        public static final String COLUMN_BACK_DROP_IMG = "back_drop";
        public static final String COLUMN_OVER_VIEW = "over_view";
        public static final String COLUMN_RELEASE_OF_FILM = "release_of_film";

    }
}
