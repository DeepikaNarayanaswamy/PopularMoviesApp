package com.example.android.popularmoviesapp;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.pojo.MovieTrailer;
import com.example.android.popularmoviesapp.tasks.FetchReviewsForMovieTask;
import com.example.android.popularmoviesapp.tasks.FetchTrailersForMovieTask;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle extras = null;
        extras = getArguments();
        final ViewHolder holder = new ViewHolder(rootView);
        if (extras != null) {

            final Movie movie = extras.getParcelable(MoviesAppConstants.MOVIE_OBJECT);
            Log.v(LOG_TAG,movie.toString());
            if (movie != null) {
                holder.movieTitleView.setText(movie.getmOriginalTitle());
                holder.movieOverviewView.setText(movie.getmOverview());
                holder.movieReleaseYrView.setText("Release Year:" + MoviesAppHelper.getYear(movie.getmReleaseDate()));
                holder.movieRatingView.setText("User Rating: " + movie.getmVoteAverage());
                rootView.setTag(holder);
                  Picasso.with(getContext())
                        .load(MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + movie.getmPosterPath())
                        .into(holder.movieThumbnailView);

                if (checkFavorite(movie)) {
                    holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                } else {
                    holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
                // here we are going to call fetch review task to get the reviews
                // Now no adapter we are gonna use,  as we are using Nested Scroll view so


                FetchReviewsForMovieTask fetchReviewsForMovieTask = new FetchReviewsForMovieTask();
                fetchReviewsForMovieTask.execute(movie.getmId() + "");
                LinearLayout reviewContainer = (LinearLayout) rootView.findViewById(R.id.review_container);
                try {
                    List<MovieReview> movieReviews = fetchReviewsForMovieTask.get();
                    // If there are no reviews , then show in a view
                    for (int i = 0; i < movieReviews.size(); i++) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.review_item, null);
                        TextView authorView = (TextView) view.findViewById(R.id.author);
                        authorView.setText(movieReviews.get(i).getMauthor());

                        TextView rCOntentView = (TextView) view.findViewById(R.id.content);
                        rCOntentView.setText(movieReviews.get(i).getMreviewContent());
                        reviewContainer.addView(view);
                    }
                    Log.v(LOG_TAG+" size of list", fetchReviewsForMovieTask.get().size() + "");


                } catch (Exception ex) {
                    ex.printStackTrace();
                    ;
                }


                FetchTrailersForMovieTask fetchTrailersForMovieTask = new FetchTrailersForMovieTask();

                fetchTrailersForMovieTask.execute(movie.getmId() + "");



                HorizontalScrollView trailerContainer = (HorizontalScrollView) rootView.findViewById(R.id.trailer_container);
                LinearLayout trailerLayout = (LinearLayout) rootView.findViewById(R.id.trailer_linear);
                try {
                    final List<MovieTrailer> movieTrailers = fetchTrailersForMovieTask.get();
                    // If there are no trailer videos, show there are no trailers
                    for (int i = 0; i < movieTrailers.size(); i++) {
                        final MovieTrailer trailer = movieTrailers.get(i);
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.trailer_item, null);
                        ImageView videoView = (ImageView) view.findViewById(R.id.thumbnail_video);
                        String thumbnailUrl = MoviesAppConstants.YOUTUBE_THUMBNAIL + movieTrailers.get(i).getVideoKey() + "/0.jpg";
                        Picasso.with(getContext())
                                .load(thumbnailUrl)
                                .into(videoView);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                watchYoutubeVideo(trailer.getVideoKey());
                            }
                        });
                        trailerLayout.addView(view);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    ;
                }

                // Here favorite button will be used and the on-click listener for that is defined.
                holder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkFavorite(movie)) {
                            setAsFavorite(v, movie);
                            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                        } else {
                            removeAsFavorite(movie);
                            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                        }
                    }
                });

            }
        }else{
            rootView.findViewById(R.id.movieTitlePlaceHolder).setBackgroundColor(Color.TRANSPARENT);
            rootView.findViewById(R.id.movie_details).setBackgroundColor(Color.TRANSPARENT);
        }

        return rootView;
    }

    public  void watchYoutubeVideo(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(MoviesAppConstants.YOUTUBE_WATCH + id));
            startActivity(intent);
        }
    }

       // Set the movie as favorite
    private void setAsFavorite (View v,Movie movie){

            ContentValues data = new ContentValues();
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_ID, movie.getmId());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_TITLE, movie.getmOriginalTitle());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW, movie.getmOverview());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_DATE, movie.getmReleaseDate());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_RATING, movie.getmVoteAverage());
            data.put(MoviesContract.MoviesEntry.COL_POSTER_PATH, movie.getmPosterPath());
            Uri uri = getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, data);
            Long id = ContentUris.parseId(uri);
            Log.v(LOG_TAG+"Inserted movie id =", id.toString());
            Toast.makeText(getContext(),MoviesAppConstants.MARK_FAVORITE,Toast.LENGTH_SHORT).show();
        }



    // Check if the given movie is already marked as favorite
    private boolean checkFavorite(Movie movie){
        String [] selectionArgs = {movie.getmId()+""};
        Cursor cursor = getContext().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, new String[]{MoviesContract.MoviesEntry._ID}, MoviesContract.MoviesEntry.COL_MOVIE_ID + " = ?", selectionArgs, null);
        if (cursor.moveToFirst()){
            return true;
        }
        cursor.close();
        return false;
    }
    // Remove the movie from favorite list
    private void  removeAsFavorite(Movie movie){
        String [] selectionArgs = {movie.getmId()+""};
        int deletedRows = getActivity().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COL_MOVIE_ID + "= ?",selectionArgs);

        Toast.makeText(getContext(),MoviesAppConstants.REMOVE_FAVORITE,Toast.LENGTH_SHORT).show();

    }

    private class ViewHolder{

        TextView movieTitleView;
        TextView movieOverviewView;
        TextView movieReleaseYrView;
        TextView movieRatingView;
        ImageView movieThumbnailView;
        ImageView favoriteIcon;
        public ViewHolder(View view){
            movieTitleView = (TextView) view.findViewById(R.id.movie_title);
            movieOverviewView = (TextView) view.findViewById(R.id.movie_overview);
            movieReleaseYrView = (TextView) view.findViewById(R.id.movie_release_year);
            movieRatingView = (TextView) view.findViewById(R.id.movie_user_rating);
            movieThumbnailView = (ImageView) view.findViewById(R.id.movie_thumbnail);
            favoriteIcon = (ImageView) view.findViewById(R.id.favorite_icon);
        }

    }


}
