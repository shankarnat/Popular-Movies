package com.example.shankan.popular_movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by shankan on 9/14/2016.
 */

public class DetailFragment extends Fragment {
    public DetailFragment() {
    }

    SharedPreferences pref;
    String image;
    String summary;
    String releaseyr;
    String moviename;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movies_detail, container, false);
        Intent detailIntent = getActivity().getIntent();


        // newImage.setImageResource(R.drawable.jaws);
        if (detailIntent != null && detailIntent.hasExtra(Intent.EXTRA_TEXT)) {
            int elementLocation = (int) detailIntent.getIntExtra(Intent.EXTRA_TEXT, 0);
            //get sharedpreferences and the values for them
            pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
            String imageLocation = pref.getString("image" + elementLocation, "");
            ImageView newImage = (ImageView) rootView.findViewById(R.id.imageView2);
            TextView summaryTxt = (TextView) rootView.findViewById(R.id.textView5);
            TextView titleTxt = (TextView) rootView.findViewById(R.id.textView);
            TextView releaseYr = (TextView) rootView.findViewById(R.id.textView2);
            TextView ratngTxt = (TextView) rootView.findViewById(R.id.textView4);
            summaryTxt.setText(pref.getString("summary" + elementLocation, ""));
            titleTxt.setText(pref.getString("title" + elementLocation, ""));
            releaseYr.setText(pref.getString("releaseyr" + elementLocation, ""));
            ratngTxt.setText(pref.getString("rating" + elementLocation, "") + "/10");
            Picasso.with(getContext()).load(imageLocation).into(newImage);
        }
        return rootView;
    }
}
