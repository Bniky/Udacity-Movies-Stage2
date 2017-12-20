package com.bniky.nicholas.movies.dbProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bniky.nicholas.movies.contract.FavouriteMovieContract;
import com.bniky.nicholas.movies.dbHelper.FavouriteMovieDbHelper;

/**
 * Created by Nicholas on 18/12/2017.
 */

public class MovieProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = MovieProvider.class.getSimpleName();

    //Used for database info
    FavouriteMovieDbHelper favouriteMovieDbHelper;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int FAVOURITE = 100;
    private static final int FAVOURITE_MOVIE_ID = 101;
    private static final int FAVOURITE_TITLE = 102;

    static {
        //Table
        sUriMatcher.addURI(FavouriteMovieContract.CONTENT_AUTHORITY, "movie_favourite", FAVOURITE);

        //Look at id
        sUriMatcher.addURI(FavouriteMovieContract.CONTENT_AUTHORITY, "movie_favourite/#", FAVOURITE_MOVIE_ID);

        //Look at title
        sUriMatcher.addURI(FavouriteMovieContract.CONTENT_AUTHORITY, "movie_favourite/*", FAVOURITE_TITLE);
    }


    @Override
    public boolean onCreate() {
        // ContentProvider methods.

        favouriteMovieDbHelper = new FavouriteMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
// Get readable database
        SQLiteDatabase database = favouriteMovieDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE:
                // For the favouite move code, query the table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of movies in the table.
                return cursor = database.query(FavouriteMovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

            case FAVOURITE_MOVIE_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(FavouriteMovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case FAVOURITE_TITLE:
                selection = FavouriteMovieContract.MovieEntry.COLUMN_TITLE + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(FavouriteMovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE:
                return insertFavouriteMovie(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a favourite movie into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertFavouriteMovie(Uri uri, ContentValues values) {

        SQLiteDatabase database = favouriteMovieDbHelper.getWritableDatabase();

        Integer movieId = values.getAsInteger(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID);

        if(movieId == null || !isValidGender(movieId)){
            throw new IllegalArgumentException(("Pet requires valid gender"));
        }


        String movieTitle = values.getAsString(FavouriteMovieContract.MovieEntry.COLUMN_TITLE);
        if(movieTitle == null){
            throw new IllegalArgumentException("Pet requires a name");
        }


        // Insert a new favourite into the movie database table with the given ContentValues
        long id = database.insert(FavouriteMovieContract.MovieEntry.TABLE_NAME, null,values);

        if(id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    private boolean isValidGender(Integer id) {

        if(id > 0){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = favouriteMovieDbHelper.getWritableDatabase();
        int rowsDeleted;

        //Get the end of Uri
        String endOfUri = getLastBitFromUrl(uri.toString());

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE:
                // Delete all rows that match the selection and selection args

                //Number of rows deleted
                rowsDeleted = database.delete(FavouriteMovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                if(rowsDeleted != 0 ){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                // Return the number of rows deleted
                return rowsDeleted;
            case FAVOURITE_MOVIE_ID:
                // Delete a single row given by the ID in the URI
                selection = FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};


                rowsDeleted = database.delete(FavouriteMovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                if(rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted;

            case FAVOURITE_TITLE:
                // Delete a single row given by the ID in the URI
                selection = FavouriteMovieContract.MovieEntry.COLUMN_TITLE + "=?";
                selectionArgs = new String[]{endOfUri};

                rowsDeleted = database.delete(FavouriteMovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

                if(rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    public static String getLastBitFromUrl(final String url){
        // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
