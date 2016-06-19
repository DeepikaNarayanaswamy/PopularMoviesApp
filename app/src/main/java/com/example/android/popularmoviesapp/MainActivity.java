package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.pojo.Movie;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {
    boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));
        Log.v(LOG_TAG,sort_by);
        if (sort_by.equals(R.string.pref_value_favorite)){
            getSupportActionBar().setTitle(MoviesAppConstants.FAVORITE_MOVIES);
        }else if (sort_by.equals(R.string.pref_value_Popular)){
          getSupportActionBar().setTitle(MoviesAppConstants.POPULAR_MOVIES);
        }else if (sort_by.equals(R.string.pref_value_toprated)){
            getSupportActionBar().setTitle(MoviesAppConstants.TOPRATED_MOVIES);
        }

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(this,SettingsActivity.class);
            startActivity(settingsActivity);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
        // For a tablet, start the fragment automatically so that in the 2nd col. the details are displayed.
        if (mTwoPane){

            Bundle args = new Bundle();
            args.putParcelable(MoviesAppConstants.MOVIE_OBJECT,movie);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        // For a phone start the new activity
        else{
            Bundle args = new Bundle();
            args.putParcelable(MoviesAppConstants.MOVIE_OBJECT, movie);

            Intent invokeDetailActivity = new Intent(this,MovieDetailActivity.class).putExtras(args);
            startActivity(invokeDetailActivity);
        }
    }
}

