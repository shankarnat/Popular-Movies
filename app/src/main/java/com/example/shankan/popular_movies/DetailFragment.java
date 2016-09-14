package com.example.shankan.popular_movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * Created by shankan on 9/14/2016.
 */

public class DetailFragment extends Fragment {
    public DetailFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.movies_detail, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_layout);

        return rootView;
    }
}