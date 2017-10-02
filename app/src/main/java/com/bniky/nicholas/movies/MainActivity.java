package com.bniky.nicholas.movies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
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
import android.widget.GridView;
import android.widget.TextView;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieImageData>>{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    //API KEY
    private static final String API_KEY = "";

    private static final String BASE_SORT_URL = "http://api.themoviedb.org/3/movie";
    //Popular movie query
    private static final String POPULAR_MOVIES_TAG = "/popular?api_key=";
    //Top rated movie query
    private static final String TOP_RATED_MOVIES_TAG = "/top_rated?api_key=";

    private static final String POPULAR_MOVIES_URL = BASE_SORT_URL + POPULAR_MOVIES_TAG + API_KEY;
    private static final String TOP_RATED_MOVIES_URL = BASE_SORT_URL + TOP_RATED_MOVIES_TAG + API_KEY;

    private static String currentURL = POPULAR_MOVIES_URL;
    static int loaderNumbr = 0;
    MoviePosterAdapter adapter;
    GridView moviePosterGridView;

    View progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.loading_spinner);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            Log.i(LOG_TAG, "Ent======================================================================");
            progress.setVisibility(View.GONE);
            getLoaderManager().initLoader(loaderNumbr, null, this).forceLoad();
        }
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
                getLoaderManager().getLoader(0).forceLoad();
                progress.setVisibility(View.VISIBLE);
                loaderNumbr = 0;
                return true;


        case R.id.top_rated:

                currentURL = TOP_RATED_MOVIES_URL;
                getLoaderManager().initLoader(1, null, this).forceLoad();
                loaderNumbr = 1;
                progress.setVisibility(View.VISIBLE);
                return true;
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
    }

    @Override
    public void onLoaderReset(Loader<List<MovieImageData>> loader) {
        progress.setVisibility(View.GONE);
        moviePosterGridView.setAdapter(null);
    }

    public void updateUi(final List<MovieImageData> moviePosterData){

        // Create a new {@link ArrayAdapter} of MoviePosterAdapter
        adapter = new MoviePosterAdapter(this, moviePosterData);

        TextView messageInfo = (TextView) findViewById(R.id.no_list);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent openInfo = new Intent(MainActivity.this, MovieDetailScrollingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("titleMovie", moviePosterData.get(i).getTitle());
                bundle.putString("titleMovie", moviePosterData.get(i).getTitle());
                bundle.putString("release", moviePosterData.get(i).getReleaseOfFilm());
                bundle.putDouble("rating", moviePosterData.get(i).getVote_average());
                bundle.putString("overView", moviePosterData.get(i).getOverView());

                bundle.putString("poster", moviePosterData.get(i).getImage());
                bundle.putString("back_drop", moviePosterData.get(i).getBackDrop());

                openInfo.putExtras(bundle);
                startActivity(openInfo);
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

