package com.example.shankan.popular_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    String mSortType;
    private final String MOVIEFRAGMENT_TAG = "MVTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs
                = PreferenceManager.getDefaultSharedPreferences(this);
        mSortType = prefs.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_rank));

        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment(), MOVIEFRAGMENT_TAG).
        commit();
    }

    protected void onRestart(){
        super.onRestart();
        startActivity(getIntent());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_menu_id)
        {
            Intent settingsIntent = new Intent(this , SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs
                = PreferenceManager.getDefaultSharedPreferences(this);
        String sorttype = prefs.getString(getString(R.string.pref_units_key),getString(R.string.pref_units_rank));
        // update the location in our second pane using the fragment manager
        if (sorttype != null && !sorttype.equals(mSortType)) {
            MainFragment mv = (MainFragment) getSupportFragmentManager().findFragmentByTag(MOVIEFRAGMENT_TAG);
            if ( null != mv ) {
                mv.onSortChanged();
            }
            mSortType = sorttype;
        }
    }
}