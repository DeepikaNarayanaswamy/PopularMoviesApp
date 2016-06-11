package com.example.android.popularmoviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by P01242 on 6/11/2016.
 */
public class MovieReviewAdapter extends ArrayAdapter<MovieReview> {

    public MovieReviewAdapter(Context context, int resource, List<MovieReview> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieReview reivew = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }
        ViewHolder holder = new ViewHolder(convertView);
        holder.contentView.setText(reivew.getMreviewContent());
        holder.authorView.setText(reivew.getMauthor());
        return convertView;
    }

        // Using view holder pattern , to improve performance
    private class ViewHolder {

            TextView authorView;
            TextView contentView;

            public ViewHolder(View view) {

                authorView = (TextView) view.findViewById(R.id.author);
                contentView = (TextView) view.findViewById(R.id.content);


            }
        }
}

