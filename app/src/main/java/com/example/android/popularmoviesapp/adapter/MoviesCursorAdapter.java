package com.example.android.popularmoviesapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.squareup.picasso.Picasso;

/**
 * Created by P01242 on 6/24/2016.
 */
public class MoviesCursorAdapter extends CursorAdapter {

    public MoviesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        MovieImageViewHolder holder = new MovieImageViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int colIndex = cursor.getColumnIndex(MoviesContract.MoviesEntry.COL_POSTER_PATH);
        MovieImageViewHolder holder = (MovieImageViewHolder) view.getTag();
        Log.v("bindView", cursor.getString(colIndex));
        Picasso.with(context)
                .load(MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + cursor.getString(colIndex))
                .into(holder.movieImageView
                );
    }
    private class MovieImageViewHolder{
        ImageView movieImageView;
        public MovieImageViewHolder(View view){
            movieImageView = (ImageView) view.findViewById(R.id.movie_image);
        }
    }

}
