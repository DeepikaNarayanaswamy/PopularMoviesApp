package com.example.android.popularmoviesapp;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmoviesapp.adapter.MoviesAdapter;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteMoviesFragment#} factory method to
 * create an instance of this fragment.
 */
public class FavoriteMoviesFragment extends Fragment  implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



    public FavoriteMoviesFragment() {
        // Required empty public constructor
    }

    FavoriteMoviesAdapter favoriteMoviesAdapter;
    public int MOVIES_LOADER = 1;

    private static final String[] MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MoviesContract.MoviesEntry.TABLE_MOVIES + "." + MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COL_MOVIE_ID,
            MoviesContract.MoviesEntry.COL_MOVIE_TITLE,
            MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW,
            MoviesContract.MoviesEntry.COL_POSTER_PATH,
            MoviesContract.MoviesEntry.COL_MOVIE_RATING,
            MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_DATE
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ArrayList<Movie> movies = new ArrayList<>();
        favoriteMoviesAdapter = new FavoriteMoviesAdapter(getActivity(),null,0);


        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(favoriteMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Cursor cursor = favoriteMoviesAdapter.getCursor();


                                               /* Intent invokeDetailActivity = new Intent(getActivity(),MovieDetailActivity.class).
                                                        putExtra("MovieObject",cursor);

                                                startActivity(invokeDetailActivity);*/
                                            }
                                        }
        );
        return rootView;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if (intent == null || intent.getData() == null){
            return null;
        }
        Loader<Cursor> cursorLoader = new CursorLoader(getContext(),MoviesContract.MoviesEntry.CONTENT_URI,MOVIE_COLUMNS,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        favoriteMoviesAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoriteMoviesAdapter.swapCursor(null);
    }


}
