package com.example.android.popularmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.squareup.picasso.Picasso;


/**
 * Created by P01242 on 6/12/2016.
 */
public class FavoriteMoviesAdapter extends CursorAdapter {
    int COL_POSTER_PATH = 4;
    public FavoriteMoviesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return  view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder)view.getTag();

        Picasso.with(context)
                .load(MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + cursor.getString(COL_POSTER_PATH))
                .into(holder.iconView);


    }

    public class ViewHolder{
        ImageView iconView;
        public ViewHolder(View view){
            iconView = (ImageView)view.findViewById(R.id.movie_image);
        }
    }
}
