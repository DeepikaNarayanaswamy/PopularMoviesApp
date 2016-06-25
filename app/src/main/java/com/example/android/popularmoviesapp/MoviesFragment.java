package com.example.android.popularmoviesapp;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmoviesapp.adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.adapter.MoviesCursorAdapter;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.tasks.FetchMoviesTask;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    MoviesAdapter moviesAdapter;
    MoviesCursorAdapter moviesCursorAdapter;
    // These columns are for getting the values from the cursor to display the favorite movies
    public static final int COL_MOVIE_ID  = 1;
    public static final int COL_MOVIE_TITLE  = 2;
    public static final int COL_MOVIE_OVERVIEW  = 3;
    public static final int COL_MOVIE_RATING  =4;
    public static final int COL_MOVIE_RELEASE_DATE  = 5;
    public static final int COL_POSTER_PATH  = 6;

    public static final  int FAVORITE_MOVIES_LOADER = 1;
    public static final  int POPULAR_MOVIES_LOADER = 2;
    public static final  int TOPRATED_MOVIES_LOADER = 3;


    List<Movie> movies = new ArrayList<>();

    public MoviesFragment() {
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Movie movie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // For retaining the current screen on orientation change aa
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ArrayList<Movie> movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(getActivity(), movies);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        // TODO  DEFINE CLICK EVENTS

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                                                Movie movie = new Movie();
                                                movie.setmId(cursor.getInt(COL_MOVIE_ID));
                                                movie.setmReleaseDate(cursor.getString(COL_MOVIE_RELEASE_DATE));
                                                movie.setmOriginalTitle(cursor.getString(COL_MOVIE_TITLE));
                                                movie.setmVoteAverage(cursor.getDouble(COL_MOVIE_RATING));
                                                movie.setmPosterPath(cursor.getString(COL_POSTER_PATH));
                                                movie.setmOverview(cursor.getString(COL_MOVIE_OVERVIEW));
                                                Log.v("Movie id clicked",cursor.getInt(COL_MOVIE_ID)+"");

                                               Log.i(LOG_TAG, "Posterpath : " + movie.getmPosterPath());
                                                ((Callback) getActivity()).onItemSelected(movie);

                                            }
                                        }
        );

        moviesCursorAdapter = new MoviesCursorAdapter(getContext(),null,0);
        gridView.setAdapter(moviesCursorAdapter);


        return rootView;
    }

    public void getMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));
        Log.v(LOG_TAG, "getMovies");

        // Fetch the favorite movies from DB
        if (sort_by.equals("favorite")) {
            fetchFavoriteMovies();
        }else {
            FetchMoviesTask moviesTask = new FetchMoviesTask(getContext(),moviesAdapter);
            moviesTask.execute(sort_by);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG,"onstart");
        getMovies();

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));
        // Change the title of the main activity based on the sort preference

        if (sort_by.equals("favorite")){
            activity.getSupportActionBar().setTitle(MoviesAppConstants.FAVORITE_MOVIES);
        }else if (sort_by.equals("popular")){
            activity.getSupportActionBar().setTitle(MoviesAppConstants.POPULAR_MOVIES);
        }else if (sort_by.equals("top_rated")){
            activity.getSupportActionBar().setTitle(MoviesAppConstants.TOPRATED_MOVIES);
        }
        Log.v(LOG_TAG, sort_by);
        if (sort_by.equals("favorite")){
            getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
        }else if (sort_by.equals("popular")){
            getLoaderManager().initLoader(POPULAR_MOVIES_LOADER, null, this);
        }else if (sort_by.equals("top_rated")){
            getLoaderManager().initLoader(TOPRATED_MOVIES_LOADER, null, this);
        }

        //activity.getSupportActionBar().setTitle(sort_by);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG,"onresume()");

        AppCompatActivity activity = (AppCompatActivity)getActivity();

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));

        if (sort_by.equals("favorite")){
            getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
        }else if (sort_by.equals("popular")){
            getLoaderManager().initLoader(POPULAR_MOVIES_LOADER, null, this);
        }else if (sort_by.equals("top_rated")){
            getLoaderManager().initLoader(TOPRATED_MOVIES_LOADER, null, this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));
        // Change the title of the main activity based on the sort preference

        if (sort_by.equals("favorite")){
            getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);
        }else if (sort_by.equals("popular")){
            getLoaderManager().initLoader(POPULAR_MOVIES_LOADER, null, this);
        }else if (sort_by.equals("top_rated")){
            getLoaderManager().initLoader(TOPRATED_MOVIES_LOADER, null, this);
        }
        // sending popular movies for now


    }

    private void fetchFavoriteMovies(){
        Cursor cursor = getContext().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null, MoviesContract.MoviesEntry.COL_IS_FAVORITE + " = 1" ,null,null);
        moviesAdapter.clear();
        while(cursor.moveToNext()){
            Movie movie = new Movie();
            movie.setmId(Integer.parseInt(cursor.getString(COL_MOVIE_ID)));
            movie.setmOriginalTitle(cursor.getString(COL_MOVIE_TITLE));
            movie.setmPosterPath(cursor.getString(COL_POSTER_PATH));
            movie.setmReleaseDate(cursor.getString(COL_MOVIE_RELEASE_DATE));
            movie.setmOverview(cursor.getString(COL_MOVIE_OVERVIEW));
            movie.setmVoteAverage(cursor.getDouble(COL_MOVIE_RATING));

            moviesAdapter.add(movie);
        }
        cursor.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> cursorLoader = null;
        if (id == FAVORITE_MOVIES_LOADER) {
            cursorLoader = new CursorLoader(getContext(), MoviesContract.MoviesEntry.CONTENT_URI, null, MoviesContract.MoviesEntry.COL_IS_FAVORITE + "= 1", null, null);
        }else if (id == TOPRATED_MOVIES_LOADER){
            cursorLoader = new CursorLoader(getContext(), MoviesContract.MoviesEntry.CONTENT_URI, null, MoviesContract.MoviesEntry.COL_IS_TOPRATED + "= 1", null, null);
        }else if (id == POPULAR_MOVIES_LOADER){
            cursorLoader = new CursorLoader(getContext(), MoviesContract.MoviesEntry.CONTENT_URI, null, MoviesContract.MoviesEntry.COL_IS_POPULAR + "= 1", null, null);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG, loader.getId() + "");

        moviesCursorAdapter.swapCursor(data);
        while(data.moveToNext()) {
            int colIndex = data.getColumnIndex(MoviesContract.MoviesEntry.COL_MOVIE_TITLE);
            Log.v(LOG_TAG, data.getString(colIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesCursorAdapter.swapCursor(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(LOG_TAG,"onPause()");
    }

}
