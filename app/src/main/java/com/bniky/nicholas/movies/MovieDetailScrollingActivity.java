package com.bniky.nicholas.movies;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bniky.nicholas.movies.Adapters.ReviewCardViewAdapter;
import com.bniky.nicholas.movies.Adapters.TrailerAdapter;
import com.bniky.nicholas.movies.Contract.FavouriteMovieContract;
import com.bniky.nicholas.movies.Data.MovieTrailer;
import com.bniky.nicholas.movies.Data.ReviewsOfMovie;
import com.bniky.nicholas.movies.DbHelper.FavouriteMovieDbHelper;
import com.bniky.nicholas.movies.Loaders.ReviewLoader;
import com.bniky.nicholas.movies.Loaders.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailScrollingActivity extends AppCompatActivity {

    private final String LOG = MovieDetailScrollingActivity.class.getSimpleName();

    TextView title;
    TextView releaseDate;
    TextView movieRating;
    TextView movieOverView;

    ImageView moviePoster;
    ImageView movieBackDrop;

    CardView noReview;
    NestedScrollView nestedScrollView;

    private String titleMovie;
    private String release;
    private String rating;
    private String overView;

    private String poster;
    private String back_drop;
    private int movieId;

    private String ratingFromDb;

    FavouriteMovieDbHelper mFavouriteMovieDbHelper;

    boolean fav;
    Cursor cursor;

    private LoaderManager.LoaderCallbacks<List<MovieTrailer>> movieTrailerResultLoaderListener =
            new LoaderManager.LoaderCallbacks<List<MovieTrailer>>()

            {
                @Override
                public Loader<List<MovieTrailer>> onCreateLoader ( int id, Bundle args){
                    Log.i("MovieDetailScrolling", urlTrailer);
                    return new TrailerLoader(MovieDetailScrollingActivity.this, urlTrailer);
                }

                @Override
                public void onLoadFinished
                        (Loader < List < MovieTrailer >> loader, List < MovieTrailer > data){
                    updateTraileListOfVideosUi(data);
                }

                @Override
                public void onLoaderReset (Loader < List < MovieTrailer >> loader) {

                }
            };

    private LoaderManager.LoaderCallbacks<List<ReviewsOfMovie>> reviewResultLoaderListener =
            new LoaderManager.LoaderCallbacks<List<ReviewsOfMovie>>()

            {

                @Override
                public Loader<List<ReviewsOfMovie>> onCreateLoader(int id, Bundle args) {
                    Log.i("MovieDetailScrolling", urlReview);
                    return new ReviewLoader(MovieDetailScrollingActivity.this, urlReview);
                }

                @Override
                public void onLoadFinished(Loader<List<ReviewsOfMovie>> loader, List<ReviewsOfMovie> data) {
                    updateReviewUi(data);
                    dialog.dismiss();
                }

                @Override
                public void onLoaderReset(Loader<List<ReviewsOfMovie>> loader) {

                }
            };

    private static final int MOVIE_TRAOLER_RESULT_ID = 1;
    private static final int Review_RESULT_ID = 2;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mCV_Adapter;

    String imageAdrress = "http://image.tmdb.org/t/p";



    Bundle extras;

    RecyclerView recyclerView_list_item;
    RecyclerView recyclerView_card_item;

    String urlTrailer;
    String urlReview;

    ProgressDialog dialog;

    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_scrolling);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        title = (TextView) findViewById(R.id.title_name);
        releaseDate = (TextView) findViewById(R.id.release);
        movieRating = (TextView) findViewById(R.id.rating);
        movieOverView = (TextView) findViewById(R.id.movie_descrption);

        moviePoster = (ImageView) findViewById(R.id.poster);
        movieBackDrop = (ImageView) findViewById(R.id.back_drop);

        noReview = (CardView) findViewById(R.id.no_rv_cv);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nested_scroll);

        recyclerView_list_item = (RecyclerView) findViewById(R.id.list_of_trailers_);
        recyclerView_list_item.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_card_item = (RecyclerView) findViewById(R.id.cv_of_reviews);
        recyclerView_card_item.setLayoutManager(new LinearLayoutManager(this));

        extras = getIntent().getExtras();
        if(extras != null) {
            loadDataFromIntent(extras);
            Log.i("MovieDetailScrolling", "" + extras.getInt("id"));
            getLoaderManager().initLoader(MOVIE_TRAOLER_RESULT_ID, null, movieTrailerResultLoaderListener).forceLoad();
            getLoaderManager().initLoader(Review_RESULT_ID, null, reviewResultLoaderListener).forceLoad();

        }

        // Setup FAB to open EditorActivity
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clicked) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));
                    removeMovieFromFavourite();
                    //Show Toast message saying removed from Favourites
                    clicked = false;
                }else {
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
                    addMovieToFavourite();

                    clicked = true;
                }
            }
        });

    }

    private void addMovieToFavourite(){

        Uri currentMovieId = ContentUris.withAppendedId(FavouriteMovieContract.MovieEntry.CONTENT_URI, movieId);

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_TITLE, titleMovie);
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_IMAGE, poster);
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_VOTE, ratingFromDb);
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_BACK_DROP_IMG, back_drop);
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_OVER_VIEW, overView);
        values.put(FavouriteMovieContract.MovieEntry.COLUMN_RELEASE_OF_FILM, release);

        // Insert a new row for pet in the database, returning the ID of that new row.
        Uri newUri = getContentResolver().insert(FavouriteMovieContract.MovieEntry.CONTENT_URI, values);


        // Show a toast message depending on whether or not the insertion was successful
        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_movie_favourite_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            //Show Toast message saying added to Favourites
            Toast.makeText(this, getString(R.string.add_favourite),
                    Toast.LENGTH_SHORT).show();
        }


//        cursor = getContentResolver().query(FavouriteMovieContract.MovieEntry.CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//
//        while(cursor.moveToNext()){
//
//            int mId = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID);
//            int tiId = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_TITLE);
//
//            int mov = cursor.getInt(mId);
//            String titleI = cursor.getString(tiId);
//
//            Log.i(LOG, ""+ mov + " ---- " + titleI);
//        }

    }

    private void removeMovieFromFavourite() {

        Uri currentMovieId = ContentUris.withAppendedId(FavouriteMovieContract.MovieEntry.CONTENT_URI, movieId);

        int rowDeleted = getContentResolver().delete(currentMovieId, null, null);

        if(rowDeleted == 0){
            Toast.makeText(this, getString(R.string.editor_delete_movie_favourite_failed),
                    Toast.LENGTH_SHORT).show();
        }else {
            //Show Toast message saying added to Favourites
            Toast.makeText(this, getString(R.string.remove_favourite),
                    Toast.LENGTH_SHORT).show();
        }

        String [] projection = { FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID };


        cursor = getContentResolver().query(currentMovieId,
                null,
               null,
                null,
                null);
//
        Log.i(LOG, "Removed ID - " + cursor.getCount());

//        while(cursor.moveToNext()){
//
//            int mId = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID);
//            int tiId = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_TITLE);
//
//            int mov = cursor.getInt(mId);
//            String titleI = cursor.getString(tiId);
//
//            Log.i(LOG, ""+ mov + " ---- " + titleI);
//        }

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

        //Dialog prompt box
        dialog = ProgressDialog.show(MovieDetailScrollingActivity.this, "",
                "Loading. Please wait...", true);



        titleMovie = extraFromIntent.get("titleMovie").toString();
        release = extraFromIntent.get("release").toString();
        rating = extraFromIntent.get("rating").toString() + "/10";
        ratingFromDb = extraFromIntent.get("rating").toString();
        overView = extraFromIntent.get("overView").toString();

        poster = extraFromIntent.get("poster").toString();
        back_drop = extraFromIntent.get("back_drop").toString();

        movieId = extraFromIntent.getInt("id");
        Log.i("MovieDetailScrolling", "" + movieId);
        urlTrailer = MainActivity.getBaseSortUrl() +"/" + movieId + "/videos?api_key=" + MainActivity.getApiKey();
        urlReview = MainActivity.getBaseSortUrl() +"/" + movieId + "/reviews?api_key=" + MainActivity.getApiKey();

        setTitle(titleMovie);
        title.setText(titleMovie);
        releaseDate.setText(release);
        movieRating.setText(rating);
        movieOverView.setText(overView);

        Picasso.with(getApplicationContext()).load(imageAdrress + "/w185/" + poster).into(moviePoster);
        Picasso.with(getApplicationContext()).load(imageAdrress + "/original/"+ back_drop).into(movieBackDrop);

        checkIfSavedAsFav();

    }

    private void checkIfSavedAsFav() {

        Uri currentMovieId = ContentUris.withAppendedId(FavouriteMovieContract.MovieEntry.CONTENT_URI, movieId);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        cursor = getContentResolver().query(currentMovieId,
                null,
                null,
                null,
                null);
//
        //If movieId is in table
        if(cursor.getCount() > 0){
            int checkMovieId = cursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_ID);
            cursor.moveToNext();

            Log.i(LOG, "MOVIE ID ------------------------ " + movieId + " - - - " + checkMovieId);
            Log.i(LOG, "COUNT ------------------------ " + cursor.getCount());
            //Check movieId number
            if(checkMovieId == 1) {
                //Check if fab button has been already clicked
                clicked = true;
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_on));
            }else{
                //Check if fab button is not clicked
                clicked = false;
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.btn_star_big_off));

            }
        }
    }

    void updateTraileListOfVideosUi(final List<MovieTrailer> mT){

        mAdapter = new TrailerAdapter(getApplicationContext(), mT);
        recyclerView_list_item.setAdapter(mAdapter);
    }

    void updateReviewUi(final List<ReviewsOfMovie> reviews){

        mCV_Adapter = new ReviewCardViewAdapter(getApplicationContext(), reviews);

        if(reviews.isEmpty()){
            noReview.setVisibility(View.VISIBLE);
        }else {
            recyclerView_card_item.setAdapter(mCV_Adapter);
        }

        nestedScrollView.setVisibility(View.VISIBLE);
    }


}
