package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String movieTitle = extras.getString(MoviesAppConstants.MOVIE_TITLE);
            String movieOverview = extras.getString(MoviesAppConstants.MOVIE_OVERVIEW);
            String movieRelYear = extras.getString(MoviesAppConstants.MOVIE_RELEASE_YEAR);
            Double movieUserRating = extras.getDouble(MoviesAppConstants.MOVIE_USER_RATING);
            String movieImagePath = extras.getString(MoviesAppConstants.MOVIE_IMAGE_PATH);

                    ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieTitle);
            ((TextView) rootView.findViewById(R.id.movie_overview)).setText(movieOverview);
            ((TextView) rootView.findViewById(R.id.movie_release_year)).setText("Release Year:"+movieRelYear);
            ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText("User Rating: "+movieUserRating.toString() + "/10");
            ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_thumbnail);

            Picasso.with(getContext())
                    .load(MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + movieImagePath)
                    .into(imageView);

        }
        return rootView;
    }


}
