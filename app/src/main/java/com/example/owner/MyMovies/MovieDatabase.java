package com.example.owner.MyMovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by owner on 16/11/2015.
 */
public class MovieDatabase extends SQLiteOpenHelper {

    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "movies.db";
    //versions incase of upgrades-not not to wipe old users information
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables {
        String MOVIES = "movies";
    }

    public MovieDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    //create lateset version of tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + Tables.MOVIES + "("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + MovieContract.MovieColumns.MOVIE_NAME + " TEXT NOT NULL,"
                        + MovieContract.MovieColumns.MOVIE_GENRE + " TEXT NOT NULL,"
                        + MovieContract.MovieColumns.MOVIE_RATING + " TEXT NOT NULL,"
                        + MovieContract.MovieColumns.MOVIE_SEEN + " TEXT NOT NULL)"
        );
    }

    //built in helper
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if (version == 1) {
            //add some extra fields to database without deleting existing data
            version = 2;
        }
        if (version != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS" + Tables.MOVIES);
            onCreate(db);
        }
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
