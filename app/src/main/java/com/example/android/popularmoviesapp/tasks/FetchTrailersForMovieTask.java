package com.example.android.popularmoviesapp.tasks;

import android.os.AsyncTask;

import com.example.android.popularmoviesapp.pojo.MovieTrailer;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.List;

/**
 * Created by deepika on 6/11/2016.
 */
public class FetchTrailersForMovieTask extends AsyncTask<
        String,Void,List<MovieTrailer>> {


    @Override
    protected List<MovieTrailer> doInBackground(String... params) {
        return MoviesAppHelper.fetchMovieTrailers(params[0]);
    }


}
