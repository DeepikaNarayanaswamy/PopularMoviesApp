package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmoviesapp.adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {
    public static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    MoviesAdapter moviesAdapter;
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
                    Log.i(LOG_TAG,"Click event of List item ");
                    Intent invokeDetailActivity = new Intent(getActivity(),MovieDetailActivity.class).
                            putExtra(MoviesAppConstants.MOVIE_TITLE,movie.getmOriginalTitle())
                            .putExtra(MoviesAppConstants.MOVIE_RELEASE_YEAR,MoviesAppHelper.getYear(movie.getmReleaseDate()))
                            .putExtra(MoviesAppConstants.MOVIE_OVERVIEW,movie.getmOverview())
                            .putExtra(MoviesAppConstants.MOVIE_USER_RATING,movie.getmVoteAverage())
                            .putExtra(MoviesAppConstants.MOVIE_IMAGE_PATH,movie.getmPosterPath());

                    startActivity(invokeDetailActivity);
                }
            }
        );




        return rootView;
    }

    public void getMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_value_Popular));

        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(sort_by);

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
}