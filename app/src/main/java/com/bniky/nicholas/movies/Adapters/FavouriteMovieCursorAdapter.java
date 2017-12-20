package com.bniky.nicholas.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bniky.nicholas.movies.contract.FavouriteMovieContract;
import com.bniky.nicholas.movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Nicholas on 20/12/2017.
 */

public class FavouriteMovieCursorAdapter extends CursorAdapter {

    public FavouriteMovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Fill out and return the list item view
        return LayoutInflater.from(context).inflate(R.layout.layout_movie, parent, false);    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imgV = (ImageView) view.findViewById(R.id.imageview_cover_art);
        TextView titleOfMovie = (TextView) view.findViewById(R.id.textview_movie_name);

        // Find the columns of pet attributes that we're interested in
        int imageColumnIndex = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_IMAGE);
        int titleColumnIndex = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_TITLE);


        // Read the MOVIE attributes from the Cursor for the current FAVOURITE MOVIE
        String imageOfFavouriteFilm = cursor.getString(imageColumnIndex);
        String titleOfFavouriteFilm = cursor.getString(titleColumnIndex);

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+ imageOfFavouriteFilm).into(imgV);
        titleOfMovie.setText(titleOfFavouriteFilm);
    }
}
