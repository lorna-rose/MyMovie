package com.example.owner.MyMovies;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 My list implememtation on the search screen of whichever the user choses to search by
 */
public class MovieSearchListLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieSearchListLoader.class.getSimpleName();
    private List<Movie> mMovies;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private String mFilterText;
    

    public MovieSearchListLoader(Context context, Uri uri, ContentResolver contentResolver, String filterText) {
        super(context);
        mContentResolver = contentResolver;
        mFilterText = filterText;
    }

    @Override
    //my list to pass into my query
    public List<Movie> loadInBackground() {
        String[] projection = {BaseColumns._ID,
                MovieContract.MovieColumns.MOVIE_NAME,
                MovieContract.MovieColumns.MOVIE_GENRE,
                MovieContract.MovieColumns.MOVIE_RATING,
                MovieContract.MovieColumns.MOVIE_SEEN};
        List<Movie> entries = new ArrayList<Movie>();

        //The User can search films by Movie Title here.
        String selection = MovieContract.MovieColumns.MOVIE_NAME + " LIKE '" + mFilterText + "%'";
        //the uri for the table is passed in along with the projection and user selection
        mCursor = mContentResolver.query(MovieContract.URI_TABLE, projection, selection, null, null);
        //if data found then display
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String movie_name = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_NAME));
                    String movie_genre = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_GENRE));
                    String movie_rating = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RATING));
                    String movie_seen = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_SEEN));
                    //create an instance of the movie object
                    Movie movie = new Movie(_id, movie_name, movie_genre, movie_rating, movie_seen);
                    entries.add(movie);
                } while (mCursor.moveToNext());
            }
        }
        //the user can search by genre if they wish
        String selectionGenre = MovieContract.MovieColumns.MOVIE_GENRE + " LIKE '" + mFilterText + "%'";
        mCursor = mContentResolver.query(MovieContract.URI_TABLE, projection, selectionGenre, null, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String movie_name = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_NAME));
                    String movie_genre = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_GENRE));
                    String movie_rating = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RATING));
                    String movie_seen = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_SEEN));
                    //create an instance of the movie object
                    Movie movie = new Movie(_id, movie_name, movie_genre, movie_rating, movie_seen);
                    entries.add(movie);
                } while (mCursor.moveToNext());
            }
        }
        String selectionRating = MovieContract.MovieColumns.MOVIE_RATING + " LIKE '" + mFilterText + "%'";

        mCursor = mContentResolver.query(MovieContract.URI_TABLE, projection, selectionRating, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String movie_name = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_NAME));
                    String movie_genre = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_GENRE));
                    String movie_rating = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RATING));
                    String movie_seen = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_SEEN));
                    //create an instance of the movie object
                    Movie movie = new Movie(_id, movie_name, movie_genre, movie_rating, movie_seen);
                    entries.add(movie);
                } while (mCursor.moveToNext());
            }
        }
        //user can search by rating
        String selectionSeen = MovieContract.MovieColumns.MOVIE_SEEN + " LIKE '" + mFilterText + "%'";

        mCursor = mContentResolver.query(MovieContract.URI_TABLE, projection, selectionSeen, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String movie_name = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_NAME));
                    String movie_genre = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_GENRE));
                    String movie_rating = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RATING));
                    String movie_seen = mCursor.getString(
                            mCursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_SEEN));
                    //create an instance of the movie object
                    Movie movie = new Movie(_id, movie_name, movie_genre, movie_rating, movie_seen);
                    entries.add(movie);
                } while (mCursor.moveToNext());
            }
        }
        return entries;


    }

    @Override
    public void deliverResult(List<Movie> movies) {
        if (isReset()) {
            if (movies != null) {
                mCursor.close();
            }
        }
        List<Movie> oldMovieList = mMovies;
        //debugging log
        if (mMovies == null || mMovies.size() == 0) {
            Log.d(LOG_TAG, "+++++++ No Data Returned");
        }
        mMovies = movies;
        //has the load started
        if (isStarted()) {
            super.deliverResult(movies);
        }
        if (oldMovieList != null && oldMovieList != movies) {
            mCursor.close();
        }

    }

    @Override
    protected void onStartLoading() {
        if (mMovies != null) {
            deliverResult(mMovies);
        }

        if (takeContentChanged() || mMovies == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        //gracefull exit
        onStopLoading();
        if (mCursor != null) {
            mCursor.close();
        }
        mMovies = null;
    }

    @Override
    public void onCanceled(List<Movie> movies) {
        super.onCanceled(movies);
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
