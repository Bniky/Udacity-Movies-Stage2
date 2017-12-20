package com.bniky.nicholas.movies.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bniky.nicholas.movies.data.MovieImageData;
import com.bniky.nicholas.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nicholas on 23/09/2017.
 */

public class MoviePosterAdapter extends ArrayAdapter<MovieImageData> {

    private final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();

    public MoviePosterAdapter(Activity context, List<MovieImageData> mID) {

        super(context, 0, mID);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_movie, parent, false);
        }


        // Get the {@link AndroidFlavor} object located at this position in the list
        MovieImageData currentMovieData = getItem(position);

        ImageView poster = (ImageView) listItemView.findViewById(R.id.imageview_cover_art);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+ currentMovieData.getImage()).into(poster);

        TextView mtitle = listItemView.findViewById(R.id.textview_movie_name);
        mtitle.setText(currentMovieData.getTitle());

        Log.i(LOG_TAG, currentMovieData.getImage());

        return listItemView;

    }


}
