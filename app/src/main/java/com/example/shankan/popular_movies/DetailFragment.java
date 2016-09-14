package com.example.shankan.popular_movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
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
        Intent detailIntent = getActivity().getIntent();
        if (detailIntent != null && detailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String value = detailIntent.getStringExtra(Intent.EXTRA_TEXT);
            TextView newText = (TextView) rootView.findViewById(R.id.textView2);
            newText.setText(value);
        }
        return rootView;
    }
}