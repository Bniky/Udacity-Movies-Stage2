package com.bniky.nicholas.movies.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.bniky.nicholas.movies.adapters.FavouriteMovieCursorAdapter;
import com.bniky.nicholas.movies.adapters.MoviePosterAdapter;
import com.bniky.nicholas.movies.helper.ProjectHelper;
import com.bniky.nicholas.movies.contract.FavouriteMovieContract.MovieEntry;
import com.bniky.nicholas.movies.data.MovieImageData;
import com.bniky.nicholas.movies.loaders.MovieImageLoader;
import com.bniky.nicholas.movies.R;

import java.util.List;


public class TopRatedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieImageData>>{


    private static String currentURL = ProjectHelper.getTopRatedMoviesUrl();

    private static int mPostion;

    FavouriteMovieCursorAdapter mfavouriteMovieCursorAdapter;

    TextView messageInfo;

    public static int getmPostion() {
        return mPostion;
    }

    public static String[] getPROJECTION() {
        return ProjectHelper.getPROJECTION();
    }

    MoviePosterAdapter adapter;
    GridView moviePosterGridView;

    View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getText(R.string.app_name) + " " + getText(R.string.top_rated));

        messageInfo = (TextView) findViewById(R.id.no_list);

        progress = findViewById(R.id.loading_spinner);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movie")) {
            progress.setVisibility(View.GONE);
            getLoaderManager().initLoader(ProjectHelper.getTopRatedId(), null, this);
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
                Intent intent = new Intent(TopRatedActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.top_rated:
                getLoaderManager().initLoader(ProjectHelper.getTopRatedId(), null, this).forceLoad();
                progress.setVisibility(View.VISIBLE);
                return true;

            case R.id.favourite:
                Intent intentFavouriteMovies = new Intent(TopRatedActivity.this, FavouriteActivity.class);
                startActivity(intentFavouriteMovies);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<MovieImageData>> onCreateLoader(int id, Bundle args) {
        progress.setVisibility(View.VISIBLE);
        return new MovieImageLoader(TopRatedActivity.this, currentURL);
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
                Intent openInfo = new Intent(TopRatedActivity.this, MovieDetailScrollingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("titleMovie", data.getString(data.getColumnIndex(MovieEntry.COLUMN_TITLE)));
                bundle.putString("release", data.getString(data.getColumnIndex(MovieEntry.COLUMN_RELEASE_OF_FILM)));

                double ratings = Double.parseDouble(data.getString(data.getColumnIndex(MovieEntry.COLUMN_VOTE)));
                bundle.putDouble("rating", ratings);

                bundle.putString("overView", data.getString(data.getColumnIndex(MovieEntry.COLUMN_OVER_VIEW)));
                bundle.putInt("id", data.getInt(data.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));

                bundle.putString("poster", data.getString(data.getColumnIndex(MovieEntry.COLUMN_IMAGE)));
                bundle.putString("back_drop", data.getString(data.getColumnIndex(MovieEntry.COLUMN_BACK_DROP_IMG)));

                openInfo.putExtras(bundle);
                startActivity(openInfo);

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
                Intent openInfo = new Intent(TopRatedActivity.this, MovieDetailScrollingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("titleMovie", moviePosterData.get(position).getTitle());
                bundle.putString("release", moviePosterData.get(position).getReleaseOfFilm());

                bundle.putDouble("rating", moviePosterData.get(position).getVote_average());
                bundle.putString("overView", moviePosterData.get(position).getOverView());
                bundle.putInt("id", moviePosterData.get(position).getId());

                bundle.putString("poster", moviePosterData.get(position).getImage());
                bundle.putString("back_drop", moviePosterData.get(position).getBackDrop());

                openInfo.putExtras(bundle);
                startActivity(openInfo);

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

