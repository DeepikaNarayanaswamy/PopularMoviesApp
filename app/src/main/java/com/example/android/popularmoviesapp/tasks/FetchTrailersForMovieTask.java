package com.example.android.popularmoviesapp.tasks;

import android.os.AsyncTask;

import com.example.android.popularmoviesapp.adapter.MovieReviewAdapter;
import com.example.android.popularmoviesapp.adapter.MovieTrailerAdapter;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.pojo.MovieTrailer;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.List;

/**
 * Created by P01242 on 6/11/2016.
 */
public class FetchTrailersForMovieTask extends AsyncTask<
        String,Void,List<MovieTrailer>> {

    public MovieTrailerAdapter getMovieTrailerAdapter() {
        return movieTrailerAdapter;
    }

    public void setMovieTrailerAdapter(MovieTrailerAdapter movieTrailerAdapter) {
        this.movieTrailerAdapter = movieTrailerAdapter;
    }

    MovieTrailerAdapter movieTrailerAdapter;

    @Override
    protected List<MovieTrailer> doInBackground(String... params) {
        return MoviesAppHelper.fetchMovieTrailers(params[0]);
    }

    @Override
    protected void onPostExecute(List<MovieTrailer> movieTrailers) {
        if (movieTrailerAdapter != null) {
            for (MovieTrailer trailer : movieTrailers) {
                movieTrailerAdapter.add(trailer);
            }
            movieTrailerAdapter.notifyDataSetChanged();
        }
    }
}
