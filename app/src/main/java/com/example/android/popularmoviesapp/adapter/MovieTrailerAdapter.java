package com.example.android.popularmoviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.pojo.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by P01242 on 6/11/2016.
 */
public class MovieTrailerAdapter extends ArrayAdapter<MovieTrailer> {

    public MovieTrailerAdapter(Context context, int resource, List<MovieTrailer> trailer) {
        super(context, resource, trailer);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieTrailer trailer = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);
        }
        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getVideoKey() + "/0.jpg";
        ViewHolder holder = new ViewHolder(convertView);
        holder.videoname_View.setText(trailer.getVideoName());
        Picasso.with(getContext())
                .load(thumbnailUrl)
                .into(holder.thumbnail_view);

        return convertView;


    }
    private class ViewHolder {

        ImageView thumbnail_view;
        TextView videoname_View;

        public ViewHolder(View view) {

            thumbnail_view = (ImageView) view.findViewById(R.id.thumbnail_video);
            videoname_View = (TextView) view.findViewById(R.id.video_name);


        }
    }

}
