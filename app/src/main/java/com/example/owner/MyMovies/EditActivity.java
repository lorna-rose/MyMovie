package com.example.owner.MyMovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



//Very similar to add activity but user updates the database with any changes they would like

public class EditActivity extends FragmentActivity {
    private final String LOG_TAG = EditActivity.class.getSimpleName();
    private TextView mNameTextView, mGenreTextView, mRatingTextView, mSeenTextView;
    private Button mButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_movie);
        //to show the user that selecting home will return one level up rather than to the top level of the app.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mNameTextView = (TextView) findViewById(R.id.movieName);
        mGenreTextView = (TextView) findViewById(R.id.movieGenre);
        mRatingTextView = (TextView) findViewById(R.id.movieRating);
        mSeenTextView = (TextView) findViewById(R.id.movieSeen);

        mContentResolver = EditActivity.this.getContentResolver();

        Intent intent = getIntent();
        final String _id = intent.getStringExtra(MovieContract.MovieColumns.MOVIE_ID);
        String name = intent.getStringExtra(MovieContract.MovieColumns.MOVIE_NAME);
        String genre = intent.getStringExtra(MovieContract.MovieColumns.MOVIE_GENRE);
        String rating = intent.getStringExtra(MovieContract.MovieColumns.MOVIE_RATING);
        String seen = intent.getStringExtra(MovieContract.MovieColumns.MOVIE_SEEN);

        mNameTextView.setText(name);
        mGenreTextView.setText(genre);
        mRatingTextView.setText(rating);
        mSeenTextView.setText(seen);

        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieColumns.MOVIE_NAME, mNameTextView.getText().toString());
                values.put(MovieContract.MovieColumns.MOVIE_GENRE, mGenreTextView.getText().toString());
                values.put(MovieContract.MovieColumns.MOVIE_RATING, mRatingTextView.getText().toString());
                values.put(MovieContract.MovieColumns.MOVIE_SEEN, mSeenTextView.getText().toString());
                Uri uri = MovieContract.Movies.buildMovieUri(_id);
                //an update function is called on the record
                int recordsUpdated = mContentResolver.update(uri, values, null, null);
                Log.d(LOG_TAG, "number of records updated " + recordsUpdated);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return true;
    }
}