package com.example.shankan.popular_movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by shankan on 9/13/2016.
 */
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
            imageView.setPadding(8, 8, 8, 8);
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