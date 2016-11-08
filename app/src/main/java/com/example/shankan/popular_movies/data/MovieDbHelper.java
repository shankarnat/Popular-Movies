package com.example.shankan.popular_movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.shankan.popular_movies.data.MovieContract.MoviesEntry;
import com.example.shankan.popular_movies.data.MovieContract.ReviewsEntry;
import com.example.shankan.popular_movies.data.MovieContract.VideoEntry;


/**
 * Created by shankan on 11/3/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                MoviesEntry.COLUMN_MOVIEKEY  + " INTEGER PRIMARY KEY ," +

                // the ID of the location entry associated with this weather data
                MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_FAVOURITE + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL," +

                MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MoviesEntry.COLUMN_VOTEAVG  + " TEXT NOT NULL, " +
                " UNIQUE (" + MoviesEntry.COLUMN_MOVIEKEY +  ") ON CONFLICT REPLACE);"
                ;


        final String SQL_CREATE_VIDEOS_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME + " (" +

                VideoEntry.COLUMN_MOVIEKEY + " INTEGER NOT NULL ," +

                // the ID of the location entry associated with this weather data
                VideoEntry.COLUMN_VIDEOS_URL + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_VIDEOS_ID + " INTEGER PRIMARY KEY, " +
                VideoEntry.COLUMN_VIDEOS_NAME + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + VideoEntry.COLUMN_MOVIEKEY + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_MOVIEKEY + ")" + ");" ;

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewsEntry.TABLE_NAME + " (" +

                ReviewsEntry.COLUMN_MOVIEKEY + " INTEGER NOT NULL ," +

                // the ID of the location entry associated with this weather data
                ReviewsEntry.COLUMN_REVIEWS_AUTHOR + " TEXT NOT NULL, " +
                ReviewsEntry.COLUMN_REVIEWS_ID + " INTEGER PRIMARY KEY , " +
                ReviewsEntry.COLUMN_REVIEWS_CONTENT  + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ReviewsEntry.COLUMN_MOVIEKEY + ") REFERENCES " +
                MoviesEntry.TABLE_NAME + " (" + MoviesEntry.COLUMN_MOVIEKEY + ")" + ");" ;
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  VideoEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +   ReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
