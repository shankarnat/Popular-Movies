package com.example.shankan.popular_movies.data;


/**
 * Created by shankan on 11/3/2016.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.shankan.popular_movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Create Strings for each of the Tables - Movies, Reviews and Trailers

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS  = "videos";

    public static final class MoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIEKEY = "moviekey";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_FAVOURITE = "favourite";
        public static final String COLUMN_RELEASE_DATE = "releasedate";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_VOTEAVG = "voteavg";
        public static final String COLUMN_MOVIEAPITYPE = "movieapitype";


        public static Uri buildMovieUri(String id) {
            return Uri.withAppendedPath(CONTENT_URI, id);
        }
        public static Uri buildMovieUriwithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEWS_ID = "reviewid";
        public static final String COLUMN_REVIEWS_AUTHOR = "reviewauthor";
        public static final String COLUMN_REVIEWS_CONTENT = "reviewcontent";
        public static final String COLUMN_MOVIEKEY = "moviekey";


        public static Uri buildReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
    public static final class VideoEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEOS).build();
        public static final String TABLE_NAME = "videos";
        public static final String COLUMN_VIDEOS_URL = "videourl";
        public static final String COLUMN_VIDEOS_ID = "videoid";
        public static final String COLUMN_VIDEOS_NAME = "videoname";
        public static final String COLUMN_MOVIEKEY = "moviekey";

        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}