package com.example.android.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


/**
 * Created by P01242 on 6/12/2016.
 */
public class MoviesProvider extends ContentProvider{
    MovieDBHelper movieDBHelper;
    private String LOG_TAG = MoviesProvider.class.getSimpleName();
    static UriMatcher sUriMatcher = buildUriMatcher();
    public static  final int MOVIES = 100;
    public static final int MOVIE_ID = 101;

    public static final int MOVIE_favorite = 102;
    public static final int MOVIE_POPULAR = 103;
    public static final int MOVIE_TOPRATED = 104;
    @Override
    public boolean onCreate() {
        Log.v(LOG_TAG,"onCreateMethod");
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRows;
        switch (sUriMatcher.match(uri)){
            case MOVIES : deletedRows = movieDBHelper.getWritableDatabase().delete(MoviesContract.MoviesEntry.TABLE_MOVIES, selection, selectionArgs);
                            break;
            default: throw new UnsupportedOperationException("Failed to insert into table"+uri);
        }
        if (deletedRows > 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return deletedRows;
    }


    @Override
    // This method is used to insert the record of favorite movie in Movies table.
    // and returns the URI of the newly inserted record.
    public Uri insert(Uri uri, ContentValues values) {

        Uri recordUri = null;
        long recordId;
        switch (sUriMatcher.match(uri)){
            case MOVIES :
                recordId = movieDBHelper.getWritableDatabase().insert(MoviesContract.MoviesEntry.TABLE_MOVIES, null, values);
                if (recordId > 0)
                    recordUri = MoviesContract.MoviesEntry.buildMoviesURI(recordId);
                else
                    throw new UnsupportedOperationException("Failed to insert into table"+uri);
                break;
            default:throw new UnsupportedOperationException("Failed to insert into table"+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return recordUri;
    }


    @Override
    // This method returns the result of the sql query in the form of cursor. Either all movies or a movie with one id
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIES : cursor = movieDBHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_MOVIES, projection, selection, selectionArgs, null, null, sortOrder);
                            break;
            // Have to complete this
            case MOVIE_ID : cursor = movieDBHelper.getReadableDatabase().query(MoviesContract.MoviesEntry.TABLE_MOVIES, projection, selection, selectionArgs, null, null, sortOrder);
                               break;
            default:throw new UnsupportedOperationException("Failed to insert into table"+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        cursor.close();
        return cursor;
    }


    @Override
    // This method returns the type of the result the query gives based on the URI
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case MOVIES : return MoviesContract.MoviesEntry.CONTENT_DIR_TYPE;

            case MOVIE_ID : return MoviesContract.MoviesEntry.CONTENT_ITEM_TYPE;

            default: throw new UnsupportedOperationException("Unknown uri"+uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int nrows = -1;
        switch (sUriMatcher.match(uri)){

            case MOVIES : nrows = db.update(MoviesContract.MoviesEntry.TABLE_MOVIES,values,selection,selectionArgs);
                break;
            case MOVIE_favorite: nrows = db.update(MoviesContract.MoviesEntry.TABLE_MOVIES,values,selection,selectionArgs);
                break;
            case MOVIE_POPULAR: nrows = db.update(MoviesContract.MoviesEntry.TABLE_MOVIES,values,selection,selectionArgs);
                break;
            case MOVIE_TOPRATED: nrows = db.update(MoviesContract.MoviesEntry.TABLE_MOVIES,values,selection,selectionArgs);
                Log.v(LOG_TAG,"No of rows updated = "+nrows);
                break;



        }
        if (nrows != 0)
           getContext().getContentResolver().notifyChange(uri,null);
        return nrows;

    }

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // The uri will be used to query the full table and get all the records
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIE,MOVIES);

        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIE+"/"+MoviesContract.MoviesEntry.FAVORITE,MOVIE_favorite);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIE+"/"+MoviesContract.MoviesEntry.TOP_RATED,MOVIE_TOPRATED);
        uriMatcher.addURI(MoviesContract.CONTENT_AUTHORITY,MoviesContract.PATH_MOVIE+"/"+MoviesContract.MoviesEntry.POPULAR,MOVIE_POPULAR);

        return uriMatcher;

    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Log.v("WP BULK Inserting",uri.toString());
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MoviesContract.MoviesEntry.TABLE_MOVIES, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }

    }
}
