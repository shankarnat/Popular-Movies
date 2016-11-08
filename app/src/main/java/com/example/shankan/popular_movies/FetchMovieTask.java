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
    int movieCount =  0;

    Context myContext;

    public FetchMovieTask(Context newContext    ){
        myContext = newContext;
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
        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);
        Vector<ContentValues> cVVector_rank = new Vector<ContentValues>(movieArray.length());
        Vector<ContentValues> cVVector_popularity = new Vector<ContentValues>(movieArray.length());


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
            releaseyr = releaseyr.substring(0,4);
            imagepath = "http://image.tmdb.org/t/p/w185" +  aMovie.getString(MV_PSTR);
            ContentValues movieValues = new ContentValues();

            //Create and put all the movie values here
            movieValues.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, summary);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_TITLE, title);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, releaseyr);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_VOTEAVG, rating);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_POSTER_PATH, imagepath);
            movieValues.put(MovieContract.MoviesEntry.COLUMN_MOVIEKEY, movieid);
            if (menuOption.contains("popularity"))
            {
                movieValues.put(MovieContract.MoviesEntry.COLUMN_FAVOURITE,"popularity");
                cVVector_popularity.add(movieValues);
                if ( cVVector_popularity.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector_popularity.size()];
                    cVVector_popularity.toArray(cvArray);

                    movieCount = myContext.getContentResolver().bulkInsert(MovieContract.MoviesEntry.CONTENT_URI, cvArray);
                }
            }
            else{
                movieValues.put(MovieContract.MoviesEntry.COLUMN_FAVOURITE,"rank");
                cVVector_rank.add(movieValues);
                if ( cVVector_rank.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector_rank.size()];
                    cVVector_rank.toArray(cvArray);
                    movieCount = myContext.getContentResolver().bulkInsert(MovieContract.MoviesEntry.CONTENT_URI, cvArray);
                }
            }



              /*
                resultStrs[i][0] = title;
                resultStrs[i][1] = summary;
                resultStrs[i][2] = imagepath;
                resultStrs[i][3] = rating;
                resultStrs[i][4] = releaseyr;
*/

        }
        return null;
    }

    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;

        BufferedReader reader = null;
        String forecastJsonStr = null;

        Log.v("units type1", params[0]);
        final String [] MOVIE_URL = {
                "https://api.themoviedb.org/3/movie/popular",
                "https://api.themoviedb.org/3/movie/top_rated"
        };
        for(String MOVIE_BASE_URL : MOVIE_URL) {
            try {
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read th  e input stream into a String
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
                forecastJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e("ForecastFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("ForecastFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                getMovieDataFromJson(forecastJsonStr, MOVIE_BASE_URL);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return null;
    }


}