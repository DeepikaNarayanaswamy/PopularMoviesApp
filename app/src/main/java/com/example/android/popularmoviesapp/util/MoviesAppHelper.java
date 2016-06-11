package com.example.android.popularmoviesapp.util;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.android.popularmoviesapp.pojo.Movie;
import com.example.android.popularmoviesapp.constants.MoviesAppConstants;
import com.example.android.popularmoviesapp.pojo.MovieReview;
import com.example.android.popularmoviesapp.pojo.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by P01242 on 5/7/2016.
 */
public class MoviesAppHelper {

    public static final String LOG_TAG = MoviesAppHelper.class.getSimpleName();

    /*
    This method will invoke the movies db api and gets the results based on the
    users preference - Top rated or popular movies
    By default the popular movies are shown
    @param : sortby -  Top rated or popular
    @return : List of movies
 */
    public static List<Movie> fetchMovie(String sortBy) {

        List<Movie> movies = new ArrayList<>();
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        Uri builtURL = Uri.parse(MoviesAppConstants.MOVIEDB_URL + sortBy).buildUpon().
                appendQueryParameter(MoviesAppConstants.API_KEY, MoviesAppConstants.API_KEY_VALUE).build();
        URL url = null;
        String moviesJsonStr;
        try {
            url = new URL(builtURL.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.i(LOG_TAG, "Connected to the URL");
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();
            movies = getMoviesList(moviesJsonStr);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

            return movies;

        }
    }

    /*
        * This method parses the json string and gets the list of movies
        * @param : moviesJsonStr
        * @return : List of movies
     */
    private static List<Movie> getMoviesList(String moviesJsonStr) {
        List<Movie> moviesList = new ArrayList<>();
        try {
            // Check if the json string has value.
            if (moviesJsonStr != null) {
                JSONObject forecastJson = new JSONObject(moviesJsonStr);
                JSONArray array = forecastJson.getJSONArray(MoviesAppConstants.JSON_RESULTS_KEY);

                for (int i = 0; i < array.length(); i++) {

                    Movie movie = new Movie();
                    JSONObject OBJ = array.getJSONObject(i);
                    movie.setmId(OBJ.getInt(MoviesAppConstants.JSON_MOVIE_ID));
                    movie.setmPosterPath(OBJ.getString(MoviesAppConstants.JSON_POSTER_PATH_KEY));
                    movie.setmVoteAverage(OBJ.getDouble(MoviesAppConstants.JSON_VOTE_AVG_KEY));
                    movie.setmOriginalTitle(OBJ.getString(MoviesAppConstants.JSON_ORIGINAL_TITLE_KEY));
                    movie.setmReleaseDate(OBJ.getString(MoviesAppConstants.JSON_RELEASE_DATE_KEY));
                    movie.setmOverview(OBJ.getString(MoviesAppConstants.JSON_OVERVIEW_KEY));
                    moviesList.add(movie);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Some error has occurred" + e.getMessage());
            e.printStackTrace();
        }
        return moviesList;
    }

    public static String getYear(String strDate) {

        String[] dateArray = strDate.split("-");
        return dateArray[0];

    }


    // This function is to get the reviews of a movie

    public static List<MovieReview> fetchMovieReviews(String movieId) {

        List<MovieReview> moviesReview = new ArrayList<>();
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        Uri builtURL = Uri.parse(MoviesAppConstants.MOVIEDB_URL + movieId + MoviesAppConstants.URL_REVIEWS).buildUpon().
                appendQueryParameter(MoviesAppConstants.API_KEY, MoviesAppConstants.API_KEY_VALUE).build();
        URL url = null;
        String movieReviewJsonStr;
        try {
            url = new URL(builtURL.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.i(LOG_TAG, "Connected to the URL");
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieReviewJsonStr = buffer.toString();
            moviesReview = getMovieReviews(movieReviewJsonStr);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

            return moviesReview;

        }

    }

    private static List<MovieReview> getMovieReviews(String movieReviewStr) throws JSONException {

        List<MovieReview> movieReviews = new ArrayList<MovieReview>();
        if (movieReviewStr != null) {
            JSONObject forecastJson = new JSONObject(movieReviewStr);
            JSONArray array = forecastJson.getJSONArray(MoviesAppConstants.JSON_RESULTS_KEY);

            for (int i = 0; i < array.length(); i++) {
                MovieReview review = new MovieReview();
                JSONObject reviewObject = array.getJSONObject(i);
                review.setMauthor(reviewObject.getString(MoviesAppConstants.JSON_AUTHOR));
                review.setMreviewContent(reviewObject.getString(MoviesAppConstants.JSON_CONTENT));
                Log.v("Review authro", review.getMauthor());
                movieReviews.add(review);
            }

        }
        return movieReviews;

    }


    // Fetch movie trailer videos

    public static List<MovieTrailer> fetchMovieTrailers(String movieId) {
        List<MovieTrailer> trailers = new ArrayList<>();
        BufferedReader reader = null;
        HttpURLConnection urlConnection = null;
        Uri builtURL = Uri.parse(MoviesAppConstants.MOVIEDB_URL + movieId + MoviesAppConstants.URL_VIDEOS).buildUpon().
                appendQueryParameter(MoviesAppConstants.API_KEY, MoviesAppConstants.API_KEY_VALUE).build();
        URL url = null;
        String movieTrailerJsonStr;
        try {
            url = new URL(builtURL.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.i(LOG_TAG, "Connected to the URL");
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                //return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                //return null;
            }
            movieTrailerJsonStr = buffer.toString();
            trailers = getMovieTrailers(movieTrailerJsonStr);

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }


        }
        return trailers;
    }



    public static List<MovieTrailer> getMovieTrailers(String movieTrailerJsonStr){
        List<MovieTrailer> movieReviews = new ArrayList<MovieTrailer>();
        try {
            if (movieTrailerJsonStr != null) {
                JSONObject forecastJson = new JSONObject(movieTrailerJsonStr);
                JSONArray array = forecastJson.getJSONArray(MoviesAppConstants.JSON_RESULTS_KEY);

                for (int i = 0; i < array.length(); i++) {
                    MovieTrailer trailer = new MovieTrailer();
                    JSONObject trailerObject = array.getJSONObject(i);
                    trailer.setVideoId(trailerObject.getString(MoviesAppConstants.JSON_MOVIE_ID));
                    trailer.setVideoName(trailerObject.getString(MoviesAppConstants.VIDEO_NAME));
                    trailer.setVideoKey(trailerObject.getString(MoviesAppConstants.VIDEO_KEY));
                    Log.v("Video id", trailer.getVideoId());
                    movieReviews.add(trailer);
                }

            }
        }catch (Exception e){
            Log.e("Error:",e.getMessage());
        }
        Log.v("#reviews=",movieReviews.size()+"");
        return movieReviews;

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
