package com.bniky.nicholas.movies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.data;
import static android.R.string.no;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieImageData>>{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //API KEY
    private static final String API_KEY = "227bc80a341eb5ef323a521732265376";

    private static final String BASE_SORT_URL = "http://api.themoviedb.org/3/movie";
    //Popular movie query
    private static final String POPULAR_MOVIES_TAG = "/popular?api_key=";
    //Top rated movie query
    private static final String TOP_RATED_MOVIES_TAG = "/top_rated?api_key=";

    private static final String POPULAR_MOVIES_URL = BASE_SORT_URL + POPULAR_MOVIES_TAG + API_KEY;
    private static final String TOP_RATED_MOVIES_URL = BASE_SORT_URL + TOP_RATED_MOVIES_TAG + API_KEY;

    private static String currentURL = POPULAR_MOVIES_URL;

    View progress;

    List<MovieImageData> movieData;

    //Movie base
    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie";
    //Image base URL
    private static final String MOVIEDB_IMAGE_BASE = "http://image.tmdb.org/t/p/w185/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.loading_spinner);

        Log.v(LOG_TAG, "initLoader========================================");
        getLoaderManager().initLoader(0, null, this).forceLoad();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.top_rated:
                if (!currentURL.equals(TOP_RATED_MOVIES_URL)) {
                    currentURL = TOP_RATED_MOVIES_URL;
                    getLoaderManager().initLoader(1, null, this).forceLoad();
                    return true;
                } else {
                    return true;
                }

                case R.id.popular:
                if (!currentURL.equals(POPULAR_MOVIES_URL)) {
                    currentURL = POPULAR_MOVIES_URL;
                    getLoaderManager().initLoader(0, null, this).forceLoad();
                    return true;
                } else {
                    return true;
                }
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
        updateUi(data);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieImageData>> loader) {

    }

    public void updateUi(final List<MovieImageData> moviePosterData){
        progress.setVisibility(View.GONE);


        // Create a new {@link ArrayAdapter} of earthquakes
        MoviePosterAdapter adapter = new MoviePosterAdapter(this, moviePosterData);

        TextView noItemFound = (TextView) findViewById(R.id.no_list);
        // Find a reference to the {@link ListView} in the layout
        GridView moviePosterGridView = (GridView) findViewById(R.id.gridview);
        moviePosterGridView.setEmptyView(noItemFound);

        if(!isOnline()){
            noItemFound.setVisibility(View.VISIBLE);
        }else{
            //noItemFound.setText("No earthquakes found!");
        }


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        moviePosterGridView.setAdapter(adapter);

//        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent openInfo = new Intent(Intent.ACTION_VIEW, Uri.parse(e.get(i).getUrl()));
//                startActivity(openInfo);
//            }
//        });
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

