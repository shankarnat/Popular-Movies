package com.example.shankan.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
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

/**
 * Created by shankan on 9/13/2016.
 */
public class MainFragment extends Fragment {
    public MainFragment() {
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
                Intent newIntent = new Intent (getActivity(), MoviesDetail.class );
                Toast.makeText(getContext(),newS, Toast.LENGTH_SHORT).show();
                newIntent.putExtra(Intent.EXTRA_TEXT,newS);
                startActivity(newIntent);
            }
        });
        return rootView;
    }

    public class GridElement extends BaseAdapter {

        private Context mContext;

        public GridElement(Context newContext)
        {
            mContext = newContext;
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
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }


        @Override
        public int getCount(){
            return 4;
        }

        public Integer[] mThumbIds = {
                R.drawable.jaws, R.drawable.lionking,
                R.drawable.star, R.drawable.taxidriver
        };
    }
}
