package com.bniky.nicholas.movies.DbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bniky.nicholas.movies.Contract.FavouriteMovieContract;
import com.bniky.nicholas.movies.Contract.FavouriteMovieContract.MovieEntry;

/**
 * Created by Nicholas on 18/12/2017.
 */

public class FavouriteMovieDbHelper extends SQLiteOpenHelper {

    public static final String db_name = "movie.db";
    public static final int version = 2;

    public FavouriteMovieDbHelper(Context context) {
        super(context, db_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "(" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACK_DROP_IMG + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVER_VIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_OF_FILM + " TEXT NOT NULL);";

        //Create table command
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
