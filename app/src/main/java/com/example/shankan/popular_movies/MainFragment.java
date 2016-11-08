package com.example.shankan.popular_movies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.example.shankan.popular_movies.data.MovieContract;
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
public class MainFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor>{

    public MainFragment() {
    }

    private static final int MOVIES_LOADER = 0 ;
    GridElement movieAdapter;
    GridView gridView;

    private static final String[] MOVIES_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_MOVIEKEY,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_FAVOURITE,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_MOVIEAPITYPE,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_OVERVIEW,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_POSTER_PATH,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_TITLE,
            MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_VOTEAVG
    };

// These are indices that are tied to the Forecast_Columns

    static final int COL_MOVIE_KEY = 0;
    static final int COL_FAVOURITE = 1;
    static final int COL_MOVIE_APITYPE = 2;
    static final int COL_OVERVIEW = 3;
    static final int COL_POSTER_PATH = 4;
    static final int COL_RELEASE_DATE= 5;
    static final int COL_TITLE = 6;
    static final int COL_VOTEAVG = 7;

    SharedPreferences prefs;
    String sorttype;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.grid_fragment, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_layout);
        movieAdapter = new GridElement(getActivity(), null, 0);
        gridView.setAdapter(movieAdapter);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sorttype = prefs.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_rank));
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
        FetchMovieTask weatherTask = new FetchMovieTask( getContext());
        weatherTask.execute();
    }

    @Override
    public  void onStart()
    {
        super.onStart();
  //      int movieCount = 0;
   //     pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        updateWeather();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Uri movieForSettingsUri = MovieContract.MoviesEntry.buildMovieUri(sorttype);
        Log.e("Weat",movieForSettingsUri.toString());
        return new CursorLoader(getActivity(),
                movieForSettingsUri,
                MOVIES_COLUMNS,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        movieAdapter.swapCursor(null);
    }


}
