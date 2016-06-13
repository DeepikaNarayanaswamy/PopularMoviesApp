package com.example.android.popularmoviesapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmoviesapp.adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment   {
    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    MoviesAdapter moviesAdapter;
    // These columns are for getting the values from the cursor to display the favorite movies
    public static final int COL_MOVIE_ID  = 1;
    public static final int COL_MOVIE_TITLE  = 2;
    public static final int COL_MOVIE_OVERVIEW  = 3;
    public static final int COL_MOVIE_RATING  =4;
    public static final int COL_MOVIE_RELEASE_DATE  = 5;
    public static final int COL_POSTER_PATH  = 6;

    List<Movie> movies = new ArrayList<>();

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ArrayList<Movie> movies = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(getActivity(), movies);


        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = moviesAdapter.getItem(position);
                    Log.i(LOG_TAG,"Posterpath : "+movie.getmPosterPath() );

                    Intent invokeDetailActivity = new Intent(getActivity(),MovieDetailActivity.class).
                            putExtra("MovieObject",movie);

                    startActivity(invokeDetailActivity);
                }
            }
        );





        return rootView;
    }

    public void getMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));
        Log.v("MoviesFragment", "getMovies");


        if (sort_by.equals("favorite")) {
            fetchFavoriteMovies();
        }else {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute(sort_by);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovies();
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        public  final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        @Override
        protected List<Movie> doInBackground(String... params) {

            Log.i(LOG_TAG, "Entering doInBackground method");
            // By default we show the popular movies
            String sort_by = getString(R.string.pref_value_Popular);
            if (params != null)
                sort_by = params[0];

            Log.i(LOG_TAG,"Sorting by "+sort_by);

                movies = MoviesAppHelper.fetchMovie(sort_by);

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            moviesAdapter.clear();
            for(Movie movie : movies){

                moviesAdapter.add(movie);
            }
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    private void fetchFavoriteMovies(){
        Cursor cursor = getContext().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, null,null ,null,null);
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


}
