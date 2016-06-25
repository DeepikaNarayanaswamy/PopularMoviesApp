package com.example.android.popularmoviesapp.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmoviesapp.MoviesFragment;
import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.data.MovieDBHelper;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by P01242 on 6/23/2016.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    public  final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    Context mContext;
    MoviesAdapter moviesAdapter;
    public FetchMoviesTask(Context context,MoviesAdapter adapter){
        mContext = context;
        moviesAdapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        List<Movie> movies = new ArrayList<>();
        Log.i(LOG_TAG, "Entering doInBackground method");
        // By default we show the popular movies
        String sort_by = "popular";
        if (params != null)
            sort_by = params[0];

        Log.i(LOG_TAG,"Sorting by "+sort_by);

        movies = MoviesAppHelper.fetchMovie(sort_by);
        insertRecordstoDB(movies,sort_by);

        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        moviesAdapter.clear();
        for(Movie movie : movies){

            moviesAdapter.add(movie);
        }
    }

    private void insertRecordstoDB(List<Movie> movies,String sort_by){
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movies.size());
        for (int i=0;i<movies.size();i++){
            ContentValues data = new ContentValues();
            // before inserting we have to check if the movie exists or not
            if (!checkMovieFound(movies.get(i))){
                data.put(MoviesContract.MoviesEntry.COL_MOVIE_ID, movies.get(i).getmId());
                data.put(MoviesContract.MoviesEntry.COL_MOVIE_TITLE, movies.get(i).getmOriginalTitle());
                data.put(MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW, movies.get(i).getmOverview());
                data.put(MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_DATE, movies.get(i).getmReleaseDate());
                data.put(MoviesContract.MoviesEntry.COL_MOVIE_RATING, movies.get(i).getmVoteAverage());
                data.put(MoviesContract.MoviesEntry.COL_POSTER_PATH, movies.get(i).getmPosterPath());
                if (sort_by.equals("top_rated"))
                    data.put(MoviesContract.MoviesEntry.COL_IS_TOPRATED,1);
                else
                    data.put(MoviesContract.MoviesEntry.COL_IS_POPULAR,1);
                cVVector.add(data);
            }
        }

        if ( cVVector.size() > 0 ) {
            // Student: call bulkInsert to add the weatherEntries to the database here
            SQLiteDatabase db = new MovieDBHelper(mContext).getWritableDatabase();
            ContentValues [] contentValues = new ContentValues[cVVector.size()];
            cVVector.toArray(contentValues);
            mContext.getContentResolver().bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI,contentValues);
        }
    }
    private boolean checkMovieFound(Movie movie){
        String [] selectionArgs = {movie.getmId()+""};
        Cursor cursor = mContext.getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, new String[]{MoviesContract.MoviesEntry._ID}, MoviesContract.MoviesEntry.COL_MOVIE_ID + " = ?", selectionArgs, null);
        if (cursor.moveToFirst()){
            return true;
        }
        cursor.close();
        return false;
    }
}
