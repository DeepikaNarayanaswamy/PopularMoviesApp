package com.example.android.popularmoviesapp.tasks;

import android.os.AsyncTask;

import com.example.android.popularmoviesapp.adapter.MovieReviewAdapter;
import com.example.android.popularmoviesapp.pojo.MovieReview;

import java.util.List;

/**
 * Created by P01242 on 6/11/2016.
 */
public class FetchTrailersForMovieTask extends AsyncTask<
        String,Void,List<MovieReview>> {



    @Override
    protected List<MovieReview> doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(List<MovieReview> movieReviews) {

    }
}
