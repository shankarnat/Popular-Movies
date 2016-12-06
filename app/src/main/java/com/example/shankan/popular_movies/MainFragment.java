package com.example.shankan.popular_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.shankan.popular_movies.data.MovieContract;


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
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            private static final String[] MOVIES_COLUMNS = {
                    MovieContract.MoviesEntry.TABLE_NAME + "." + MovieContract.MoviesEntry.COLUMN_MOVIEID,
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

    static final int COL_MOVIE_KEY = 1;
    static final int COL_FAVOURITE = 2;
    static final int COL_MOVIE_APITYPE = 3;
    static final int COL_OVERVIEW = 4;
    static final int COL_POSTER_PATH = 5;
    static final int COL_RELEASE_DATE= 6;
    static final int COL_TITLE = 7;
    static final int COL_VOTEAVG = 8;

    SharedPreferences prefs;
    String sorttype;
    private static final String sSettingSelection =
            MovieContract.MoviesEntry.TABLE_NAME+
                    "." + MovieContract.MoviesEntry.COLUMN_FAVOURITE + "=?";
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
                sSettingSelection,
                new String[] {sorttype},
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        movieAdapter.swapCursor(cursor);
    }

    void onSortChanged(){
        updateWeather();
        sorttype = prefs.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_rank));
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        movieAdapter.swapCursor(null);
    }
}
