package com.example.shankan.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by shankan on 9/14/2016.
 */
public class MoviesDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_main);
        getSupportFragmentManager().beginTransaction().add(R.id.moviecontainer, new DetailFragment()).
                commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.main_menu_id)
        {
            Intent settingsIntent = new Intent(this , SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
