package com.bniky.nicholas.movies.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import com.bniky.nicholas.movies.helper.ProjectHelper;
import com.bniky.nicholas.movies.contract.FavouriteMovieContract.MovieEntry;
import com.bniky.nicholas.movies.R;

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static int mPostion;

    GridView moviePosterGridView;

    FavouriteMovieCursorAdapter mfavouriteMovieCursorAdapter;
    TextView messageInfo;


    public static int getmPostion() {
        return mPostion;
    }

    public static void setmPostion(int mPostion) {
        FavouriteActivity.mPostion = mPostion;
    }

    public static String[] getPROJECTION() {
        return ProjectHelper.getPROJECTION();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        setTitle(getText(R.string.app_name) + " " + getText(R.string.favourite));

        moviePosterGridView = (GridView) findViewById(R.id.gridview);
        messageInfo = (TextView) findViewById(R.id.no_list);


        getLoaderManager().initLoader(2, null, this).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MovieEntry.CONTENT_URI,
                ProjectHelper.getPROJECTION(),
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

    @Override
    protected void onPostResume() {
        getLoaderManager().initLoader(2, null, this).forceLoad();
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
                Intent intent = new Intent(FavouriteActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.top_rated:
                Intent intentTopRated = new Intent(FavouriteActivity.this, TopRatedActivity.class);
                startActivity(intentTopRated);
                return true;

            case R.id.favourite:
                getLoaderManager().initLoader(2, null, this).forceLoad();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void updateUiFavouriteDb(final Cursor data) {

        mfavouriteMovieCursorAdapter = new FavouriteMovieCursorAdapter(this, null);

        mfavouriteMovieCursorAdapter.swapCursor(data);

        moviePosterGridView.setEmptyView(messageInfo);

        moviePosterGridView.setAdapter(mfavouriteMovieCursorAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent openInfo = new Intent(FavouriteActivity.this, MovieDetailScrollingActivity.class);
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

                setmPostion(position);
            }
        });
    }
}
