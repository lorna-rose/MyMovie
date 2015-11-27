package com.example.owner.MyMovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//Add Activity for the user to input their own movies into the database

public class AddActivity extends FragmentActivity {

    private final String LOG_TAG = AddActivity.class.getSimpleName();
    //Viewing text within the add movie screen for each item
    private TextView mNameTextView, mGenreTextView, mRatingTextView, mSeenTextView;
    //add button to add the movie to the database
    private Button mButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set view too add movie xml which contains textview for the add fields and the save button
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_movie);
        //to show the user that selecting home will return one level up rather than to the top level of the app.
        //Reference : The following code is from http://developer.android.com/training/implementing-navigation/ancestral.html
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //Reference complete

        mNameTextView = (TextView) findViewById(R.id.movieName);
        mGenreTextView = (TextView) findViewById(R.id.movieGenre);
        mRatingTextView = (TextView) findViewById(R.id.movieRating);
        mSeenTextView = (TextView) findViewById(R.id.movieSeen);

        mContentResolver = AddActivity.this.getContentResolver();

        mButton = (Button) findViewById(R.id.saveButton);
        //if everything entered is valid input then the new values will be added into the database
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieColumns.MOVIE_NAME, mNameTextView.getText().toString());
                    values.put(MovieContract.MovieColumns.MOVIE_GENRE, mGenreTextView.getText().toString());
                    values.put(MovieContract.MovieColumns.MOVIE_RATING, mRatingTextView.getText().toString());
                    values.put(MovieContract.MovieColumns.MOVIE_SEEN, mSeenTextView.getText().toString());
                    //once added to the database the screen will revert back to the mainscreen to view the full list of movies
                    //an insert function is called on MovieContract
                    Uri returned = mContentResolver.insert(MovieContract.URI_TABLE, values);
                    Log.d(LOG_TAG, "record id returned is " + returned.toString());
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    //if isValid returns flase a toast will appear telling the user so
                    Toast.makeText(getApplicationContext(), "Please make sure you have entered valid data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //check to see if the content added is valid and that all the fields contain data
    private boolean isValid() {
        if (mNameTextView.getText().toString().length() == 0 ||
                mGenreTextView.getText().toString().length() == 0 ||
                mRatingTextView.getText().toString().length() == 0 ||
                mSeenTextView.getText().toString().length() == 0) {
            return false;
        } else {
            return true;
        }
    }
    //check aslo to see if data was infact entered
    private boolean someDataEntered() {
        if (mNameTextView.getText().toString().length() > 0 ||
                mGenreTextView.getText().toString().length() > 0 ||
                mRatingTextView.getText().toString().length() > 0 ||
                mSeenTextView.getText().toString().length() > 0) {
            return true;
        } else {
            return false;
        }
    }
//if the user chooses to hit the back button or presses it by accident it will return a dialog asking if they wish to exit or not
    @Override
    public void onBackPressed() {
        if (someDataEntered()) {
            MovieDialog dialog = new MovieDialog();
            Bundle args = new Bundle();
            args.putString(MovieDialog.DIALOG_TYPE, MovieDialog.CONFIRM_EXIT);
            dialog.setArguments(args);
            dialog.show(getSupportFragmentManager(), "confirm exit");
        } else {
            super.onBackPressed();
        }
    }
}
