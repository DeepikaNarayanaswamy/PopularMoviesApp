package com.example.android.popularmoviesapp.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;

/**
 * Created by P01242 on 6/20/2016.
 */
public class InsertFavoriteMoviesTask extends AsyncTask<Movie,Void,Long> {

    Context mContext;

    @Override
    protected Long doInBackground(Movie... movie) {

        ContentValues data = new ContentValues();
        long id = 0;
        if (movie[0] != null) {
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_ID, movie[0].getmId());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_TITLE, movie[0].getmOriginalTitle());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW, movie[0].getmOverview());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_DATE, movie[0].getmReleaseDate());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_RATING, movie[0].getmVoteAverage());
            data.put(MoviesContract.MoviesEntry.COL_POSTER_PATH, movie[0].getmPosterPath());

            Uri uri = mContext.getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, data);
            id = ContentUris.parseId(uri);
        }
        return new Long(id);
    }

    public InsertFavoriteMoviesTask(Context context){
        mContext = context;
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id != -1){
            Toast.makeText(mContext, MoviesAppConstants.MARK_FAVORITE, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext,"Error", Toast.LENGTH_SHORT).show();
        }
    }
}
