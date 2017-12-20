package com.bniky.nicholas.movies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bniky.nicholas.movies.Adapters.FavouriteMovieCursorAdapter;
import com.bniky.nicholas.movies.Contract.FavouriteMovieContract;
import com.bniky.nicholas.movies.Contract.FavouriteMovieContract.MovieEntry;
import com.bniky.nicholas.movies.DbHelper.FavouriteMovieDbHelper;


import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieImageData>>{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //API KEY
    private static final String API_KEY = "";
    //Screen position
    static int mPostion;
    private static final String BASE_SORT_URL = "http://api.themoviedb.org/3/movie";
    //Popular movie query
    private static final String POPULAR_MOVIES_TAG = "/popular?api_key=";
    //Top rated movie query
    private static final String TOP_RATED_MOVIES_TAG = "/top_rated?api_key=";

    private static String POPULAR_MOVIES_URL = BASE_SORT_URL + POPULAR_MOVIES_TAG + API_KEY;
    private static final String TOP_RATED_MOVIES_URL = BASE_SORT_URL + TOP_RATED_MOVIES_TAG + API_KEY;

    private static String currentURL = POPULAR_MOVIES_URL;
    private final static int popularId = 0;
    private final static int topRatedId = 1;

    FavouriteMovieCursorAdapter mfavouriteMovieCursorAdapter;
    FavouriteMovieDbHelper mFavouriteMovieDbHelper;

    TextView messageInfo;

    private static final String [] PROJECTION = {MovieEntry._ID, MovieEntry.COLUMN_MOVIE_ID, MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_IMAGE, MovieEntry.COLUMN_VOTE, MovieEntry.COLUMN_BACK_DROP_IMG, MovieEntry.COLUMN_OVER_VIEW,
    MovieEntry.COLUMN_RELEASE_OF_FILM};

    private LoaderManager.LoaderCallbacks<Cursor> mFavouriteCursorLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getApplicationContext(),
                    MovieEntry.CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            updateUiFavouriteDb(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mfavouriteMovieCursorAdapter.swapCursor(null);
        }
    };

    MoviePosterAdapter adapter;
    GridView moviePosterGridView;

    View progress;

    public static String getBaseSortUrl() {
        return BASE_SORT_URL;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInfo = (TextView) findViewById(R.id.no_list);

        progress = findViewById(R.id.loading_spinner);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            Log.i(LOG_TAG, "Ent======================================================================");
            progress.setVisibility(View.GONE);
            getLoaderManager().initLoader(popularId, null, this);
        }
    }

    @Override
    protected void onPostResume() {
        if(mPostion > 0){
            moviePosterGridView.setSelection(mPostion);
        }
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.popular:
                currentURL = POPULAR_MOVIES_URL;
                getLoaderManager().initLoader(popularId, null, this).forceLoad();
                progress.setVisibility(View.VISIBLE);
                return true;

            case R.id.top_rated:
                currentURL = TOP_RATED_MOVIES_URL;
                getLoaderManager().initLoader(topRatedId, null, this).forceLoad();
                progress.setVisibility(View.VISIBLE);
                return true;

            case R.id.favourite:
                moviePosterGridView.setAdapter(null);
                mfavouriteMovieCursorAdapter = new FavouriteMovieCursorAdapter(this, null);
                moviePosterGridView.setAdapter(mfavouriteMovieCursorAdapter);
                getLoaderManager().initLoader(3, null, mFavouriteCursorLoader).forceLoad();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<MovieImageData>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "LOADER============================");
        progress.setVisibility(View.VISIBLE);
        return new MovieImageLoader(MainActivity.this, currentURL);
    }

    @Override
    public void onLoadFinished(Loader<List<MovieImageData>> loader, List<MovieImageData> data) {
        progress.setVisibility(View.GONE);
        updateUi(data);
        //Go to previous screen position
        if(mPostion >0) {
            moviePosterGridView.setSelection(mPostion);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieImageData>> loader) {
        progress.setVisibility(View.GONE);

        moviePosterGridView.setEmptyView(findViewById(R.id.gridview));
    }

    public void updateUiFavouriteDb (final Cursor data){

        mfavouriteMovieCursorAdapter.swapCursor(data);

        moviePosterGridView.setEmptyView(messageInfo);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent openInfo = new Intent(MainActivity.this, MovieDetailScrollingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("titleMovie", data.getString(data.getColumnIndex(MovieEntry.COLUMN_TITLE)));
                bundle.putString("release", data.getString(data.getColumnIndex(MovieEntry.COLUMN_RELEASE_OF_FILM)));

                double ratings = Double.parseDouble(data.getString(data.getColumnIndex(MovieEntry.COLUMN_VOTE)));
                Log.i(LOG_TAG, "VOTE -------------------- " + ratings);
                bundle.putDouble("rating", ratings);

                bundle.putString("overView", data.getString(data.getColumnIndex(MovieEntry.COLUMN_OVER_VIEW)));
                bundle.putInt("id", data.getInt(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));

                bundle.putString("poster", data.getString(data.getColumnIndex(MovieEntry.COLUMN_IMAGE)));
                bundle.putString("back_drop", data.getString(data.getColumnIndex(MovieEntry.COLUMN_BACK_DROP_IMG)));

                openInfo.putExtras(bundle);
                startActivity(openInfo);
                Log.i(LOG_TAG, "MPOS ---------------");

                mPostion = position;
            }
        });
    }

    public void updateUi(final List<MovieImageData> moviePosterData){

        // Create a new {@link ArrayAdapter} of MoviePosterAdapter
        adapter = new MoviePosterAdapter(this, moviePosterData);

        // Find a reference to the {@link ListView} in the layout
        moviePosterGridView = (GridView) findViewById(R.id.gridview);
        //If view is empty show message
        moviePosterGridView.setEmptyView(messageInfo);

        if(!isOnline()){
            messageInfo.setText(R.string.error_message);
            messageInfo.setVisibility(View.VISIBLE);
        }

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        moviePosterGridView.setAdapter(adapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent openInfo = new Intent(MainActivity.this, MovieDetailScrollingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("titleMovie", moviePosterData.get(position).getTitle());
                bundle.putString("release", moviePosterData.get(position).getReleaseOfFilm());
                Log.i(LOG_TAG, "Rate ------ " + moviePosterData.get(position).getVote_average());
                bundle.putDouble("rating", moviePosterData.get(position).getVote_average());
                bundle.putString("overView", moviePosterData.get(position).getOverView());
                bundle.putInt("id", moviePosterData.get(position).getId());

                bundle.putString("poster", moviePosterData.get(position).getImage());
                bundle.putString("back_drop", moviePosterData.get(position).getBackDrop());

                openInfo.putExtras(bundle);
                startActivity(openInfo);
                Log.i(LOG_TAG, "MPOS ---------------");

                mPostion = position;
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

