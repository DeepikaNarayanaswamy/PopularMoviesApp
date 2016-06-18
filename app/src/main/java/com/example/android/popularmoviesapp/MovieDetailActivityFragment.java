package com.example.android.popularmoviesapp;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.Image;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.adapter.MovieReviewAdapter;
import com.example.android.popularmoviesapp.adapter.MovieTrailerAdapter;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.data.MoviesContract;
import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.pojo.MovieTrailer;
import com.example.android.popularmoviesapp.tasks.FetchReviewsForMovieTask;
import com.example.android.popularmoviesapp.tasks.FetchTrailersForMovieTask;
import com.example.android.popularmoviesapp.util.MoviesAppHelper;
import com.squareup.okhttp.Protocol;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import  android.support.v4.content.Loader;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    MovieReviewAdapter movieReviewAdapter;
    MovieTrailerAdapter movieTrailerAdapter;
    public MovieDetailActivityFragment() {
    }
    String movieId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle extras = null;
      /*  Intent intent = getActivity().getIntent();
        Bundle extras = null;
        // for phone
        extras = intent.getExtras();
      */  // for tablet
        if (extras == null) {
            System.out.println("for tablet !!");
            extras = getArguments();
        }
        System.out.println("Extras ="+extras);
        if (extras != null) {

            final Movie movie = extras.getParcelable("MovieObject");
            System.out.println("Movie object = "+movie);
            if (movie != null) {
                // setting the member variable here so that it can be accessed by the favorite button
                movieId = movie.getmId() + "";
                String movieTitle = movie.getmOriginalTitle();
                String movieOverview = movie.getmOverview();
                String movieRelYear = movie.getmReleaseDate();
                Double movieUserRating = movie.getmVoteAverage();
                final String movieImagePath = movie.getmPosterPath();

                ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieTitle);
                ((TextView) rootView.findViewById(R.id.movie_overview)).setText(movieOverview);
                ((TextView) rootView.findViewById(R.id.movie_release_year)).setText("Release Year:" + movieRelYear);
                ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText("User Rating: " + movieUserRating.toString() + "/10");
                ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
                Log.v("MovieDetailFragment", MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + movieImagePath);
                Picasso.with(getContext())
                        .load(MoviesAppConstants.IMAGE_URL + MoviesAppConstants.IMAGE_SIZE + movieImagePath)
                        .into(imageView);
                ImageView favoriteIcon = (ImageView) rootView.findViewById(R.id.favorite_icon);
                if (checkFavorite(movie)) {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_white_24dp);
                } else {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
                // here we are going to call fetch review task to get the reviews
                // Now no adapter we are gonna use,  as we are using Nested Scroll view so
                FetchReviewsForMovieTask fetchReviewsForMovieTask = new FetchReviewsForMovieTask();
                fetchReviewsForMovieTask.execute(movie.getmId() + "");
                LinearLayout reviewContainer = (LinearLayout) rootView.findViewById(R.id.review_container);
                try {
                    List<MovieReview> movieReviews = fetchReviewsForMovieTask.get();
                    for (int i = 0; i < movieReviews.size(); i++) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.review_item, null);
                        TextView authorView = (TextView) view.findViewById(R.id.author);
                        authorView.setText(movieReviews.get(i).getMauthor());

                        TextView rCOntentView = (TextView) view.findViewById(R.id.content);
                        rCOntentView.setText(movieReviews.get(i).getMreviewContent());
                        reviewContainer.addView(view);
                    }
                    Log.v("size of list", fetchReviewsForMovieTask.get().size() + "");


                } catch (Exception ex) {
                    ex.printStackTrace();
                    ;
                }
           /* movieReviewAdapter = new MovieReviewAdapter(getContext(),0,new ArrayList<MovieReview>());
            fetchReviewsForMovieTask.setMmovieReviewAdapter(movieReviewAdapter);
*/

                //ListView reviewListView = (ListView) rootView.findViewById(R.id.review_listview);
          /* final ScrollView scrollView = (ScrollView)rootView;
            reviewListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollView.requestDisallowInterceptTouchEvent(true);

                    int action = event.getActionMasked();

                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            scrollView.requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    return false;
                }
            });*/

                // reviewListView.setAdapter(movieReviewAdapter);

                // here we are going to call fetch trailers to get the trailer videos

                // movieTrailerAdapter = new MovieTrailerAdapter(getContext(),0,new ArrayList<MovieTrailer>());
                FetchTrailersForMovieTask fetchTrailersForMovieTask = new FetchTrailersForMovieTask();
            /*fetchTrailersForMovieTask.setMovieTrailerAdapter(movieTrailerAdapter);*/
                fetchTrailersForMovieTask.execute(movie.getmId() + "");

                // ListView trailerListView = (ListView) rootView.findViewById(R.id.trailer_listview);

                LinearLayout trailerContainer = (LinearLayout) rootView.findViewById(R.id.trailer_container);
                try {
                    final List<MovieTrailer> movieTrailers = fetchTrailersForMovieTask.get();
                    for (int i = 0; i < movieTrailers.size(); i++) {
                        final MovieTrailer trailer = movieTrailers.get(i);
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.trailer_item, null);
                        ImageView videoView = (ImageView) view.findViewById(R.id.thumbnail_video);
                        String thumbnailUrl = "http://img.youtube.com/vi/" + movieTrailers.get(i).getVideoKey() + "/0.jpg";
                        Picasso.with(getContext())
                                .load(thumbnailUrl)
                                .into(videoView);
                        TextView videonameView = (TextView) view.findViewById(R.id.video_name);
                        videonameView.setText(movieTrailers.get(i).getVideoName());
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                watchYoutubeVideo(trailer.getVideoKey());
                            }
                        });
                        trailerContainer.addView(view);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    ;
                }
           /* trailerListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollView.requestDisallowInterceptTouchEvent(true);

                    int action = event.getActionMasked();

                    switch (action) {
                        case MotionEvent.ACTION_UP:
                            scrollView.requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    return false;
                }
            });
*/
           /* trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieTrailer movie = movieTrailerAdapter.getItem(position);
                    watchYoutubeVideo(movie.getVideoKey());


                }
            });


            trailerListView.setAdapter(movieTrailerAdapter);
*/
                // Here favorite button will be used and the on-click listener for that is defined.
                final ImageView favoriteButton = (ImageView) rootView.findViewById(R.id.favorite_icon);
                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkFavorite(movie)) {
                            setAsFavorite(v, movie);
                            favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                        } else {
                            removeAsFavorite(movie);
                            favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                        }
                    }
                });

            }
        }

        return rootView;
    }

    public  void watchYoutubeVideo(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

       // Set the movie as favorite
    public void setAsFavorite (View v,Movie movie){

            ContentValues data = new ContentValues();
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_ID, movie.getmId());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_TITLE, movie.getmOriginalTitle());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_OVERVIEW, movie.getmOverview());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_RELEASE_DATE, movie.getmReleaseDate());
            data.put(MoviesContract.MoviesEntry.COL_MOVIE_RATING, movie.getmVoteAverage());
            data.put(MoviesContract.MoviesEntry.COL_POSTER_PATH, movie.getmPosterPath());
            Uri uri = getActivity().getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, data);
            Long id = ContentUris.parseId(uri);
            Log.v("id =", id.toString());
            Toast.makeText(getContext(),"Marked as favorite",Toast.LENGTH_SHORT).show();
        }




    private boolean checkFavorite(Movie movie){
        String [] selectionArgs = {movie.getmId()+""};
        Cursor cursor = getContext().getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI, new String[] {MoviesContract.MoviesEntry._ID}, MoviesContract.MoviesEntry.COL_MOVIE_ID +" = ?",selectionArgs,null);
        if (cursor.moveToFirst()){
            return true;
        }
        cursor.close();
        return false;
    }

    private void  removeAsFavorite(Movie movie){
        String [] selectionArgs = {movie.getmId()+""};
        int deletedRows = getActivity().getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI, MoviesContract.MoviesEntry.COL_MOVIE_ID + "= ?",selectionArgs);

        Toast.makeText(getContext(),"Removed as favorite",Toast.LENGTH_SHORT).show();

    }

}
