package com.example.owner.MyMovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by owner on 16/11/2015.
 */
public class MovieProvider extends ContentProvider {
    private MovieDatabase mOpenHelper;

    private static String TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MOVIES = 100;
    private static final int MOVIE_ID = 101;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "movies", MOVIES);
        matcher.addURI(authority, "movies/*", MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDatabase(getContext());
        return true;
    }

    private void deleteDatabase() {
        mOpenHelper.close();
        MovieDatabase.deleteDatabase(getContext());
        mOpenHelper = new MovieDatabase(getContext());
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.Movies.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.Movies.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    //query method return information to calling process
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDatabase.Tables.MOVIES);

        switch (match) {
            case MOVIES:
                //do nothing
                break;
            case MOVIE_ID:
                String id = MovieContract.Movies.getMovieID(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    //inserting into table
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                long recordId = db.insertOrThrow(MovieDatabase.Tables.MOVIES, null, values);
                return MovieContract.Movies.buildMovieUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    //updating table
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        String selectionCriteria = selection;
        switch (match) {
            case MOVIES:
                //do nothing - not changing value
                break;
            case MOVIE_ID:
                String id = MovieContract.Movies.getMovieID(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(MovieDatabase.Tables.MOVIES, values, selectionCriteria, selectionArgs);
    }

    @Override
    //deletion of database
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "update(uri=" + uri);
        //if you want to delete the whole database
        if (uri.equals(MovieContract.URI_TABLE)) {
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_ID:
                String id = MovieContract.Movies.getMovieID(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(MovieDatabase.Tables.MOVIES, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }
}