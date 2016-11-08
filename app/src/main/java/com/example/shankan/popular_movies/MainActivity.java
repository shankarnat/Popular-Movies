package com.example.shankan.popular_movies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//Things to add:
//1. Add some kind of loading view
//2. Get the variables done
//3. Reformat the movies detail screen


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).
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