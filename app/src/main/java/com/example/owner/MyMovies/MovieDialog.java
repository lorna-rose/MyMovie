package com.example.owner.MyMovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 Dialogs for user when deleting a record, database or exiting to early when entering a record
 */
public class MovieDialog extends DialogFragment {
    private static final String LOG_TAG = MovieDialog.class.getSimpleName();
    private LayoutInflater mLayoutInflater;
    public static final String DIALOG_TYPE = "command";
    public static final String DELETE_RECORD = "deleteRecord";
    public static final String DELETE_DATABASE = "deleteDatabase";
    public static final String CONFIRM_EXIT = "confirmExit";

    @NonNull
    @Override
    //checking to see what the user has selected to so and display the relevant dialog messages
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Reference : The following code is based on http://www.wikihow.com/Show-Alert-Dialog-in-Android
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mLayoutInflater = getActivity().getLayoutInflater();
        final View view = mLayoutInflater.inflate(R.layout.movie_layout, null);
        String command = getArguments().getString(DIALOG_TYPE);
        if (command.equals(DELETE_RECORD)) {
            final int _id = getArguments().getInt(MovieContract.MovieColumns.MOVIE_ID);
            String name = getArguments().getString(MovieContract.MovieColumns.MOVIE_NAME);
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            //pass in the movie selected to check the user that they wnt to delete that particular record
            popupMessage.setText("Are you sure you want to delete " + name + " from movie list?");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Deleting Record", Toast.LENGTH_LONG).show();
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = MovieContract.Movies.buildMovieUri(String.valueOf(_id));
                    //delete the information from the table
                    contentResolver.delete(uri, null, null);
                    //set the screen back to the main screen
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    //If set, and the activity being launched is already running in the current task,
                    // then instead of launching a new instance of that activity,
                    // all of the other activities on top of it will be closed
                    // and this Intent will be delivered to the (now on top) old activity as a new Intent.
                    //Reference: The following code is from http://developer.android.com/reference/android/content/Intent.html
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //End Reference
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });//End Reference
            //changed the parameters to suit delete database command
        } else if (command.equals(DELETE_DATABASE)) {
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete the entire database?");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = MovieContract.URI_TABLE;
                    contentResolver.delete(uri, null, null);
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            ////changed the parameters to suit confirm exit of the add movie screen without saving the movie command
        } else if (command.equals(CONFIRM_EXIT)) {
            TextView popupMessage = (TextView) view.findViewById(R.id.popup_message);
            popupMessage.setText("Are you sure you wish to exit without saving?");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        } else {
            Log.d(LOG_TAG, "Invalid Command");
        }

        return builder.create();
    }
}
