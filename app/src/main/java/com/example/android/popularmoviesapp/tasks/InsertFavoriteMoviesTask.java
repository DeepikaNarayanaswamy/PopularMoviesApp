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
public class InsertFavoriteMoviesTask extends AsyncTask<Movie,Void,Integer> {

    Context mContext;

    @Override
    protected Integer doInBackground(Movie... movie) {

        ContentValues data = new ContentValues();
        int nrows = -1;
        if (movie[0] != null) {
            String [] selectionArgs = {movie[0].getmId()+""};
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_ID, movie[0].getmId());
            data.put(MoviesContract.MoviesEntry.COL_IS_FAVORITE, 1);
            nrows = mContext.getContentResolver().update(MoviesContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath(MoviesContract.MoviesEntry.FAVORITE).build(), data, MoviesContract.MoviesEntry.COL_MOVIE_ID +" = ?",selectionArgs);

        }
        return nrows;
    }

    public InsertFavoriteMoviesTask(Context context){
        mContext = context;
    }

    @Override
    protected void onPostExecute(Integer rowsUpdated) {
        if (rowsUpdated != -1){
            Toast.makeText(mContext, MoviesAppConstants.MARK_FAVORITE, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext,"Error", Toast.LENGTH_SHORT).show();
        }
    }
}
