package com.bniky.nicholas.movies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
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

    Bundle extras;
    Bundle onStateBundle;

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


        extras = getIntent().getExtras();
        if(extras != null) {
            loadDataFromIntent(extras);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie_detail_scrolling, menu);
        return true;
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

    void loadDataFromIntent(Bundle extraFromIntent){
        String titleMovie = extraFromIntent.get("titleMovie").toString();
        String release = extraFromIntent.get("release").toString();
        String rating = extraFromIntent.get("rating").toString() + "/10";
        String overView = extraFromIntent.get("overView").toString();

        String poster = extraFromIntent.get("poster").toString();
        String back_drop = extraFromIntent.get("back_drop").toString();


        title.setText(titleMovie);
        releaseDate.setText(release);
        movieRating.setText(rating);
        movieOverView.setText(overView);

        Picasso.with(getApplicationContext()).load(imageAdrress + "/w185/" + poster).into(moviePoster);
        Picasso.with(getApplicationContext()).load(imageAdrress + "/original/"+ back_drop).into(movieBackDrop);
    }

}
