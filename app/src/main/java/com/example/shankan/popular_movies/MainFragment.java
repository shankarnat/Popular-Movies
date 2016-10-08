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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by shankan on 9/13/2016.
 */
// Image and Grid View tutorial from http://www.androidhive.info/2012/02/android-gridview-layout-tutorial
// Adapted it for our scenarios
public class MainFragment extends Fragment {

    public MainFragment() {
    }

    public int apimovieCount;
    SharedPreferences pref ;
    GridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.grid_fragment, container, false);
         gridView = (GridView) rootView.findViewById(R.id.grid_layout);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent newIntent = new Intent (getActivity(), MoviesDetail.class );
                newIntent.putExtra(Intent.EXTRA_TEXT,i);
                startActivity(newIntent);
            }
        });
        return rootView;
    }
    public void updateWeather()
    {
        FetchMovieTask weatherTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorttype = prefs.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_rank));
        Log.v("units type", sorttype);
        weatherTask.execute(sorttype);
    }

    @Override
    public  void onStart()
    {
        super.onStart();
        int movieCount = 0;
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        updateWeather();
    }


    public class GridElement extends BaseAdapter {

        private Context mContext;

        public GridElement(Context newContext)
        {
            mContext = newContext;
            pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
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
            String imageName = pref.getString("image"+ position , null);
            Log.v("Image name", "Imagename: " + imageName);
            Log.v("positionimage","image" + position);
            Picasso.with(getContext()).load(imageName).into(imageView);
            return imageView;
        }

        @Override
        public int getCount(){
            apimovieCount = pref.getInt("count", 0);
            return apimovieCount;
        }


    }
    public class FetchMovieTask extends AsyncTask<String, Void, Integer> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
            int movieCount =  0;
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Integer getMovieDataFromJson(String movieJsonStr)
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

                SharedPreferences.Editor editor = pref.edit();
                editor.putString("image"+i,  imagepath);
                editor.putString("title"+i,  title);
                editor.putString("summary"+i,  summary);
                editor.putString("rating"+i,  rating);
                editor.putString("releaseyr"+i,  releaseyr);
                editor.commit();
                movieCount = movieCount + 1;
                Log.v("image", "image"+i);
               /*
                resultStrs[i][0] = title;
                resultStrs[i][1] = summary;
                resultStrs[i][2] = imagepath;
                resultStrs[i][3] = rating;
                resultStrs[i][4] = releaseyr;
*/

            }
            return movieCount;
        }

        @Override
        protected Integer doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;
            String format = "json";
            String units = "metric";
            int moviecnt = 0;
            final String MOVIE_BASE_URL;
            Log.v("units type1", params[0]);
            if (params[0].equals("popularity"))
            {
                 MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/popular";}
            else{
                 MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated";
            }
            try {
                final String APPID_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                URL url2 = new URL ("http://semanticbackend.azurewebsites.net/api/values");
                HttpURLConnection urlConnection1 = (HttpURLConnection)  url2.openConnection();
                urlConnection1.setRequestMethod("GET");
                urlConnection1.connect();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read th  e input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                InputStream inputStream1 = urlConnection1.getInputStream();
                StringBuffer buffer1 = new StringBuffer();
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));

                buffer1.append(reader1.readLine());
                Log.v("testing mike",buffer1.toString());
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

                 moviecnt =  getMovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return moviecnt;
        }

        protected void onPostExecute(Integer movieCount){
                Log.v("counts", "count"+ movieCount);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("count",  movieCount);
            editor.commit();
            gridView.setAdapter(new GridElement(getContext()));
          }
    }
}
