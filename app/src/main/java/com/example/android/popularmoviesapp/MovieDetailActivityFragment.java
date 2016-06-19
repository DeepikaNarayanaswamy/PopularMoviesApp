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
        rootView.setTag(holder);
        if (extras != null) {

            final Movie movie = extras.getParcelable(MoviesAppConstants.MOVIE_OBJECT);
            Log.v(LOG_TAG,movie.toString());
            if (movie != null) {

                // Movie details are populated
                fillMovieDetails(holder, movie);

                // here we are going to call fetch review task to get the reviews
                // Now no adapter we are gonna use,  as we are using Nested Scroll view so
                addReviews(holder,movie);

               //   here we are going to call fetch trailers task to get the trailers
                addTrailers(holder, movie);
               // Here favorite button will be used and the on-click listener for that is defined.
                configFavoriteButton(holder,movie);


            }
        }else{
            holder.movieTitlePlaceHolder.setBackgroundColor(Color.TRANSPARENT);
            holder.movieDetails.setBackgroundColor(Color.TRANSPARENT);
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
        int deletedRows = getActivity().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COL_MOVIE_ID + "= ?", selectionArgs);

        Toast.makeText(getContext(), MoviesAppConstants.REMOVE_FAVORITE, Toast.LENGTH_SHORT).show();

    }

    private void fillMovieDetails(ViewHolder holder,Movie movie){
        holder.movieTitleView.setText(movie.getmOriginalTitle());
        holder.movieOverviewView.setText(movie.getmOverview());
        holder.movieReleaseYrView.setText("Release Year:" + MoviesAppHelper.getYear(movie.getmReleaseDate()));
        holder.movieRatingView.setText("User Rating: " + movie.getmVoteAverage());

        Picasso.with(getContext())
                .load(MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + movie.getmPosterPath())
                .into(holder.movieThumbnailView);

        if (checkFavorite(movie)) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    // Define click event listener for favorite button
    private void configFavoriteButton(final ViewHolder holder,final Movie movie){
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
    private void addTrailers(ViewHolder holder,Movie movie){
        FetchTrailersForMovieTask fetchTrailersForMovieTask = new FetchTrailersForMovieTask();

        fetchTrailersForMovieTask.execute(movie.getmId() + "");
         try {
            final List<MovieTrailer> movieTrailers = fetchTrailersForMovieTask.get();
            // If there are no trailer videos, show there are no trailers
            for (int i = 0; i < movieTrailers.size(); i++) {
                final MovieTrailer trailer = movieTrailers.get(i);
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.trailer_item, null);
                TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
                view.setTag(holder);
                String thumbnailUrl = MoviesAppConstants.YOUTUBE_THUMBNAIL + movieTrailers.get(i).getVideoKey() + "/0.jpg";
                Picasso.with(getContext())
                        .load(thumbnailUrl)
                        .into(trailerViewHolder.videoView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        watchYoutubeVideo(trailer.getVideoKey());
                    }
                });
                holder.trailerLayout.addView(view);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
    }

    private void addReviews(ViewHolder holder , Movie movie){
        FetchReviewsForMovieTask fetchReviewsForMovieTask = new FetchReviewsForMovieTask();
        fetchReviewsForMovieTask.execute(movie.getmId() + "");


        try {
            List<MovieReview> movieReviews = fetchReviewsForMovieTask.get();
            // If there are no reviews , then show in a view
            for (int i = 0; i < movieReviews.size(); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.review_item, null);
                ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
                view.setTag(holder);
                reviewViewHolder.authorView.setText(movieReviews.get(i).getMauthor());
                reviewViewHolder.rCOntentView.setText(movieReviews.get(i).getMreviewContent());
               holder.reviewContainer.addView(view);
            }
            Log.v(LOG_TAG+" size of list", fetchReviewsForMovieTask.get().size() + "");


        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }

    }
    // View holder pattern for the entire movie detail screen
    private class ViewHolder{

        TextView movieTitleView;
        TextView movieOverviewView;
        TextView movieReleaseYrView;
        TextView movieRatingView;
        ImageView movieThumbnailView;
        ImageView favoriteIcon;
        LinearLayout reviewContainer;
        LinearLayout trailerLayout;
        LinearLayout movieTitlePlaceHolder;
        LinearLayout movieDetails;
        public ViewHolder(View view){
            movieTitleView = (TextView) view.findViewById(R.id.movie_title);
            movieOverviewView = (TextView) view.findViewById(R.id.movie_overview);
            movieReleaseYrView = (TextView) view.findViewById(R.id.movie_release_year);
            movieRatingView = (TextView) view.findViewById(R.id.movie_user_rating);
            movieThumbnailView = (ImageView) view.findViewById(R.id.movie_thumbnail);
            favoriteIcon = (ImageView) view.findViewById(R.id.favorite_icon);
            reviewContainer = (LinearLayout) view.findViewById(R.id.review_container);
            trailerLayout = (LinearLayout) view.findViewById(R.id.trailer_linear);
            movieTitlePlaceHolder  = (LinearLayout)view.findViewById(R.id.movieTitlePlaceHolder);
            movieDetails =(LinearLayout) view.findViewById(R.id.movie_details);
        }

    }
    // View holder for reviews
    private class ReviewViewHolder{
        TextView authorView;
        TextView rCOntentView;
        public  ReviewViewHolder (View view){
            authorView = (TextView) view.findViewById(R.id.author);
            rCOntentView = (TextView) view.findViewById(R.id.content);
        }

    }
    // View holder for trailers
    private class TrailerViewHolder{
        ImageView videoView;
        public TrailerViewHolder(View view){
            videoView = (ImageView) view.findViewById(R.id.thumbnail_video);
        }
    }


}
