package com.bniky.nicholas.movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class MovieDetailScrollingActivity extends AppCompatActivity {

    TextView title;
    TextView releaseDate;
    TextView movieRating;
    TextView movieOverView;

    ImageView moviePoster;
    ImageView movieBackDrop;

    String imageAdrress = "http://image.tmdb.org/t/p";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView) findViewById(R.id.title_name);
        releaseDate = (TextView) findViewById(R.id.release);
        movieRating = (TextView) findViewById(R.id.rating);
        movieOverView = (TextView) findViewById(R.id.movie_descrption);

        moviePoster = (ImageView) findViewById(R.id.poster);
        movieBackDrop = (ImageView) findViewById(R.id.back_drop);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            loadDataFromIntent(extras);
        }
    }


    void loadDataFromIntent(Bundle extraFromIntent){
        String titleMovie = extraFromIntent.get("titleMovie").toString();
        String release = extraFromIntent.get("release").toString();
        String rating = extraFromIntent.get("rating").toString();
        String overView = extraFromIntent.get("overView").toString();

        String poster = extraFromIntent.get("poster").toString();
        String back_drop = extraFromIntent.get("back_drop").toString();


        title.setText(titleMovie);
        releaseDate.setText(release);
        movieRating.setText(rating+ "/10");
        movieOverView.setText(overView);

        Picasso.with(getApplicationContext()).load(imageAdrress + "/w342/" + poster).into(moviePoster);
        Picasso.with(getApplicationContext()).load(imageAdrress + "/w780/"+ back_drop).into(movieBackDrop);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
