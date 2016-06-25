package com.example.android.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * Created by P01242 on 6/12/2016.
 * This class takes care of creating and upgrading the db based on thet version
 */
public class MovieDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MovieDBHelper.class.getSimpleName();
    public static final String DB_NAME = "movie.db";
    //Added 3 columns for favorite,toprated,popular
    public static final int DB_VERSION = 3;

    public MovieDBHelper (Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    // This method will be called when we open a connection to db through getWritableDatabase using content resolver
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Write the create table query.
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_MOVIES + "( "
                + MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +MoviesContract.MoviesEntry.COL_MOVIE_ID + " TEXT UNIQUE NOT NULL,"
                +MoviesContract.MoviesEntry.COL_MOVIE_TITLE+" TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MoviesContract.MoviesEntry.COL_MOVIE_RATING + " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_DATE+ " TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_POSTER_PATH+" TEXT NOT NULL,"
                + MoviesContract.MoviesEntry.COL_IS_FAVORITE+" INTEGER, "
                + MoviesContract.MoviesEntry.COL_IS_TOPRATED+" INTEGER, "
                + MoviesContract.MoviesEntry.COL_IS_POPULAR + " INTEGER"
                + ")";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);


    }
    // This method will be called when there is a change to the db version due to schema changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.TABLE_MOVIES);
        onCreate(db);
    }
}
