package com.example.shankan.popular_movies;


import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import com.example.shankan.popular_movies.MainFragment;
import com.example.shankan.popular_movies.data.MovieContract;

/**
 * Created by shankan on 11/6/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    Context myContext;
    final String APPID_PARAM;

    public FetchMovieTask(Context newContext){
        myContext = newContext;
        APPID_PARAM= "api_key";
    }
 // Insert reviews data into sqlite

 //Insert videos data into sqlite

    private Void insertReviewFromId(String movieID)
            throws JSONException {
        String baseURL = "https://api.themoviedb.org/3/movie/" + movieID + "/reviews";
        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                .build();
        String reviews_value = getConnection(builtUri);
        JSONObject reviewsJson = new JSONObject(reviews_value);
        final String RV_ID = "id";
        final String RV_AUTHOR = "author";
        final String RV_CONTENT = "content";
        final String RV_RESULTS = "results";
        ContentValues reviewValues = new ContentValues();

        JSONArray reviewArray = reviewsJson.getJSONArray(RV_RESULTS);

        for(int i = 0; i < reviewArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"

            int reviewsId;
            String reviewContent;
            String reviewAuthor;

            // Get the JSON object representing the day
            JSONObject aReview = reviewArray.getJSONObject(i);
            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            // description is in a child array called "weather", which is 1 element long.

            reviewsId = aReview.getInt(RV_ID);
            reviewContent = aReview.getString(RV_CONTENT);
            reviewAuthor = aReview.getString(RV_AUTHOR);



            //Need to have a function to build reviewValues and Video values here and that needs
            //buildReviewValues
            //Create and put all the movie values here
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_MOVIEKEY, movieID);
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEWS_AUTHOR, reviewAuthor);
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEWS_CONTENT, reviewContent);
            reviewValues.put(MovieContract.ReviewsEntry.COLUMN_REVIEWS_ID, reviewsId);
            Uri yUri = myContext.getContentResolver().insert(MovieContract.MoviesEntry.REVIEW_URI, reviewValues);

        }
        return null;
    }

    private ContentValues getVideofromId(String movieID)
            throws JSONException {
        String baseURL = "https://api.themoviedb.org/3/movie/" + movieID + "/videos";
        Uri builtUri = Uri.parse(baseURL).buildUpon()
                .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                .build();
        String video_value = getConnection(builtUri);
        JSONObject videoJson = new JSONObject(video_value);
        final String VD_ID = "id";
        final String VD_NAME = "author";
        final String VD_URL = "content";
        final String VD_SIZE = "size" ;
        final String VD_RESULTS = "results";

        ContentValues videoValues = new ContentValues();

        JSONArray videoArray = videoJson.getJSONArray(VD_RESULTS);

        for(int i = 0; i < videoArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"

            int videoId;
            String videoName;
            String videoURL;
            int videoSize;

            // Get the JSON object representing the day
            JSONObject aVideo = videoArray.getJSONObject(i);
            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            // description is in a child array called "weather", which is 1 element long.

            videoId = aVideo.getInt(VD_ID);
            videoName = aVideo.getString(VD_NAME);
            videoURL = "https://www.youtube.com/watch?v=" + aVideo.getString(VD_URL);
            videoSize = aVideo.getInt(VD_SIZE);



            //Need to have a function to build reviewValues and Video values here and that needs
            //buildReviewValues
            //Create and put all the movie values here
            videoValues.put(MovieContract.VideoEntry.COLUMN_MOVIEKEY, movieID);
            videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEOS_ID, videoId);
            videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEOS_NAME, videoName);
            videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEOS_URL, videoURL);
            videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEOS_SIZE, videoSize);
            Uri zUri = myContext.getContentResolver().insert(MovieContract.MoviesEntry.VIDEOS_URI, videoValues);

        }
        return null;
    }


    private Void getMovieDataFromJson(String movieJsonStr, String menuOption)
            throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String MV_RESULTS = "results";
        final String MV_TITLE = "title";
        final String MV_OV = "overview";
        final String MV_PSTR = "poster_path";
        final String  MV_RATING  = "vote_average";
        final String MV_YEAR = "release_date";
        final String MV_ID = "id";
        Uri newUri = Uri.EMPTY;
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);

        ContentValues movieValues = new ContentValues();
        //get the value for each of the elements
        for(int i = 0; i < movieArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String title;
            String summary;
            String releaseyr;
            String rating;
            String imagepath;
            int movieid;
            // Get the JSON object representing the day
            JSONObject aMovie = movieArray.getJSONObject(i);
            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            // description is in a child array called "weather", which is 1 element long.

            summary = aMovie.getString(MV_OV);
            title = aMovie.getString(MV_TITLE);
            releaseyr = aMovie.getString(MV_YEAR);
            rating = aMovie.getString(MV_RATING);
            movieid = aMovie.getInt(MV_ID);
            //To just get the year, it is the first four characters; hence doing a substring
            releaseyr = releaseyr.substring(0, 4);
            imagepath = "http://image.tmdb.org/t/p/w185" + aMovie.getString(MV_PSTR);

            //Need to have a function to build reviewValues and Video values here and that needs
            //buildReviewValues
            //Create and put all the movie values here
            movieValues.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, summary);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_TITLE, title);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, releaseyr);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_VOTEAVG, rating);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_POSTER_PATH, imagepath);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_MOVIEKEY, movieid);
            if (menuOption.contains("popular"))
            {
                movieValues.put(MovieContract.MoviesEntry.COLUMN_FAVOURITE,"popularity");
                newUri = MovieContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath("popularity").build();
                Uri xUri =  myContext.getContentResolver().insert(newUri, movieValues);

                }

            else{
                movieValues.put(MovieContract.MoviesEntry.COLUMN_FAVOURITE,"Rank");
                newUri = MovieContract.MoviesEntry.CONTENT_URI.buildUpon().appendPath("Rank").build();
                Uri xUri =  myContext.getContentResolver().insert(newUri, movieValues);
                }
            insertReviewFromId(MovieContract.MoviesEntry.COLUMN_MOVIEID);

            ContentValues videoValues = getVideofromId(MovieContract.MoviesEntry.COLUMN_MOVIEID);

             }
        return null;
       }


    @Override
    protected Void doInBackground(String... params) {
        String forecastJsonStr;

//        Log.v("units type1", params[0]);
        final String [] MOVIE_URL = {
                "https://api.themoviedb.org/3/movie/popular",
                "https://api.themoviedb.org/3/movie/top_rated"
        };
        for(String MOVIE_BASE_URL : MOVIE_URL) {


                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();

                forecastJsonStr = getConnection(builtUri);



            try {
                getMovieDataFromJson(forecastJsonStr, MOVIE_BASE_URL);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

        }
        return null;
    }

    public String getConnection (Uri newUri)
    {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;
        try {
            URL url = new URL(newUri.toString());

            Log.v(LOG_TAG, "Built URI " + newUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            forecastJsonStr = buffer.toString();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
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
                forecastJsonStr = null;
            }
        }
            catch (IOException e) {
            }
        finally
        {

        }


        return null;
    }

}