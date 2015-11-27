package com.example.owner.MyMovies;

import android.net.Uri;
import android.provider.BaseColumns;

//this class sets the content authority for my provider to allow ease of access to my SQlite Databse.
//The class establishes a contract between the provider and other applications by ensuring that the provider can be correctly accessed.
//Also sets my uri to access my table.
//Reference help from : http://developer.android.com/guide/topics/providers/content-provider-creating.html
//Refernce help from : https://android.googlesource.com/platform/developers/samples/android/+/c0d4a898873b255441d725f507608a973327f897/networking/sync/BasicSyncAdapter/src/com/example/android/network/sync/basicsyncadapter/provider/FeedContract.java

public class MovieContract {

    interface MovieColumns {
        String MOVIE_ID = "_id";
        String MOVIE_NAME = "movie_name";
        String MOVIE_GENRE = "movie_genre";
        String MOVIE_RATING = "movie_rating";
        String MOVIE_SEEN = "movie_seen";
    }

    //the authority specifies the name of the content provider
    public static final String CONTENT_AUTHORITY = "com.example.owner.MyMovies.provider";
    //define content provider URI address which will be used to access the content
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_MOVIES = "movies";
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_MOVIES);

    public static final String[] TOP_LEVEL_PATHS = {
            PATH_MOVIES
    };

    public static class Movies implements MovieColumns, BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".movies";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".movies";

        public static Uri buildMovieUri(String movieId){
            return CONTENT_URI.buildUpon().appendEncodedPath(movieId).build();
        }

        public static String getMovieID(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}
