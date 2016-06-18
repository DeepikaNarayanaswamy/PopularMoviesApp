package com.example.android.popularmoviesapp.tasks;

import android.os.AsyncTask;

import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.List;

/**
 * Created by deepika on 6/11/2016.
 */
public class FetchReviewsForMovieTask extends AsyncTask<
        String,Void,List<MovieReview>> {


    @Override
    protected List<MovieReview> doInBackground(String... params) {
        return MoviesAppHelper.fetchMovieReviews(params[0]);

    }

}
