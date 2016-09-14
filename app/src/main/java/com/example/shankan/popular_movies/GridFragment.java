package com.example.shankan.popular_movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by shankan on 9/13/2016.
 */
public class GridFragment extends Fragment {
    public GridFragment() {
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
                Toast.makeText(getContext(),newS, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}
