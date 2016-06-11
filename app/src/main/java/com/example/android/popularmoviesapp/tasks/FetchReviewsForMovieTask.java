package com.example.android.popularmoviesapp.tasks;

import android.os.AsyncTask;

import com.example.android.popularmoviesapp.adapter.MovieReviewAdapter;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.List;

/**
 * Created by P01242 on 6/11/2016.
 */
public class FetchReviewsForMovieTask extends AsyncTask<
        String,Void,List<MovieReview>> {

    public MovieReviewAdapter getMmovieReviewAdapter() {
        return mmovieReviewAdapter;
    }

    public void setMmovieReviewAdapter(MovieReviewAdapter mmovieReviewAdapter) {
        this.mmovieReviewAdapter = mmovieReviewAdapter;
    }

    MovieReviewAdapter mmovieReviewAdapter;
    @Override
    protected List<MovieReview> doInBackground(String... params) {
        return MoviesAppHelper.fetchMovieReviews(params[0]);

    }

    @Override
    protected void onPostExecute(List<MovieReview> movieReviews) {


        for (MovieReview mReview : movieReviews){
            mmovieReviewAdapter.add(mReview);
        }
        mmovieReviewAdapter.notifyDataSetChanged();
    }
}
