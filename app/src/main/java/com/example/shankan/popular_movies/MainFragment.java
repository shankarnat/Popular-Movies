package com.example.shankan.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by shankan on 9/13/2016.
 */
//API Key to get the latest movie from Movie DB.
//https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=734430c2463674e1087d4aae938e966b
//https://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=734430c2463674e1087d4aae938e966b
public class MainFragment extends Fragment {
    public MainFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.grid_fragment, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_layout);
        gridView.setAdapter(new GridElement(getContext()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String newS = "sh" + i ;
                Intent newIntent = new Intent (getActivity(), MoviesDetail.class );
                Toast.makeText(getContext(),newS, Toast.LENGTH_SHORT).show();
                newIntent.putExtra(Intent.EXTRA_TEXT,newS);
                startActivity(newIntent);
            }
        });
        return rootView;
    }

    public class GridElement extends BaseAdapter {

        private Context mContext;

        public GridElement(Context newContext)
        {
            mContext = newContext;
        }
        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowmanager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
                int deviceWidth = displayMetrics.widthPixels;
                int deviceHeight = displayMetrics.heightPixels;
                imageView.getLayoutParams().height = deviceHeight/2  ;
                imageView.getLayoutParams().width = deviceWidth/2 ;


            }
            else
            {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }


        @Override
        public int getCount(){
            return 4;
        }

        public Integer[] mThumbIds = {
                R.drawable.jaws, R.drawable.lionking,
                R.drawable.star, R.drawable.taxidriver
        };
    }
    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[][] getMovieDataFromJson(String movieJsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted.
            final String MV_RESULTS = "results";
            final String MV_TITLE = "title";
            final String MV_OV = "overview";
            final String MV_PSTR = "poster_path";
            final String  MV_RATING  = "vote_average";
            final String MV_YEAR = "release_date";
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(MV_RESULTS);
            String[][] resultStrs = new String[25][5];

            //get the value for each of the elements
            for(int i = 0; i < movieArray.length(); i++) {
                // For now, using the format "Day, description, hi/low"
                String title;
                String summary;
                String releaseyr;
                String rating;
                String imagepath;
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
                //To just get the year, it is the first four characters; hence doing a substring
                releaseyr = releaseyr.substring(0,4);
                imagepath = aMovie.getString(MV_PSTR);
                imagepath = "http://image.tmdb.org/t/p/w185" + imagepath ;
                resultStrs[i][0] = title;
                resultStrs[i][1] = summary;
                resultStrs[i][2] = imagepath;
                resultStrs[i][3] = rating;
                resultStrs[i][4] = releaseyr;
            }
    /*      for (String s : resultStrs) {
                Log.v(LOG_TAG, "Forecast entry: " + s);
            }*/
            return resultStrs;
        }
        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String format = "json";
            String units = "metric";
            int numDays = 7;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/movie/";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";
                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
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
            } catch (IOException e)
            {
                Log.e("ForecastFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
            } finally
            {
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

                return getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String[] result) {
            if (result != null) {
                newAdapter.clear();
                newAdapter.addAll(result);
         /*       for(String dayForecastStr : result) {
                    newAdapter.add(dayForecastStr);
                }*/
                // New data is back from the server.  Hooray!
            }
        }
    }
}
