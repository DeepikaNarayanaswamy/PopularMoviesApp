package com.example.android.popularmoviesapp.data;

import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by P01242 on 6/11/2016.
 * This class is used to define the database and the table.
 * The table contains the movie id , poster path, release date, overview
 * This table stores only the favorite movies marked by the user.
 */
public class MoviesContract {
    // The location of the db . Give the package name
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    // This class is used to define the table in the database
    public static final class MoviesEntry implements BaseColumns{

        public static final String TABLE_MOVIES = "movie";
        public static final String _ID  = "_id";
        public static final String COL_MOVIE_ID  = "movie_id";
        public static final String COL_POSTER_PATH  = "poster_path";
        public static final String COL_MOVIE_TITLE  = "title";
        public static final String COL_MOVIE_OVERVIEW  = "overview";
        public static final String COL_MOVIE_RATING  = "rating";
        public static final String COL_MOVIE_RELEASE_DATE  = "release_date";

        // To get the location of the table -> db location + table name
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // Returns multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

        // This method will return the URI of the newly inserted record
        public static Uri buildMoviesURI(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }




    }

}
