package com.example.shankan.popular_movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by shankan on 11/6/2016.
 */
public class GridElement extends CursorAdapter {

    private Context mContext;
    ImageView imageView;

    public GridElement(Context newContext,  Cursor c, int flags)
    {
        super(newContext, c, flags);
        mContext = newContext;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        imageView =  new ImageView(context);
        imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        imageView.getLayoutParams().height = deviceHeight/2  ;
        imageView.getLayoutParams().width = deviceWidth/2 ;
        return imageView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView view1 = (ImageView) view;
        Picasso.with(context).load( cursor.getString(MainFragment.COL_POSTER_PATH)).into(view1);
    }



}
