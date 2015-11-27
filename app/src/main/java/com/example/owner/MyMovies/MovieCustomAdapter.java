package com.example.owner.MyMovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
Adapter class for my Movie List to display as fragment
 */
public class MovieCustomAdapter extends ArrayAdapter<Movie> {
    private LayoutInflater mLayoutInflater;
    private static FragmentManager sFragmentManager;

    public MovieCustomAdapter(Context context, FragmentManager fragmentManager){
        super(context, android.R.layout.simple_list_item_2);
        //sets up the xml for the current context
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sFragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if(convertView == null){
            view = mLayoutInflater.inflate(R.layout.custom_movie, parent, false);
        }else{
            view = convertView;
        }

        final Movie movie = getItem(position);
        final int _id = movie.getID();
        final String name = movie.getMovie_name();
        final String genre = movie.getMovie_genre();
        final String rating = movie.getMovie_rating();
        final String seen = movie.getMovie_seen();
        //textview for how the items will be displayed in the list
        ((TextView) view.findViewById(R.id.movie_name)).setText(name);
        ((TextView) view.findViewById(R.id.movie_genre)).setText("Movie Genre: " + genre);
        ((TextView) view.findViewById(R.id.movie_rating)).setText("Movie Rating: " + rating);
        ((TextView) view.findViewById(R.id.movie_seen)).setText(seen);
        //if the edit button is chosen the information is passed to the editactivity class.
        Button editButton = (Button) view.findViewById(R.id.edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra(MovieContract.MovieColumns.MOVIE_ID, String.valueOf(_id));
                intent.putExtra(MovieContract.MovieColumns.MOVIE_NAME, name);
                intent.putExtra(MovieContract.MovieColumns.MOVIE_GENRE, genre);
                intent.putExtra(MovieContract.MovieColumns.MOVIE_RATING, rating);
                intent.putExtra(MovieContract.MovieColumns.MOVIE_SEEN, seen);
                getContext().startActivity(intent);
            }
        });
        //if tht delete button in the list is chosen the movie is found by id
        //the user is prompted with a dialog message if they wish to delete or not
        //if the delet they movie is removed from the list and the list redisplays
        Button deleteButton = (Button) view.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDialog dialog = new MovieDialog();
                Bundle args = new Bundle();
                args.putString(MovieDialog.DIALOG_TYPE, MovieDialog.DELETE_RECORD);
                args.putInt(MovieContract.MovieColumns.MOVIE_ID, _id);
                args.putString(MovieContract.MovieColumns.MOVIE_NAME, name);
                dialog.setArguments(args);
                dialog.show(sFragmentManager, "delete-record");
            }
        });
        return view;
    }
    public void setData(List<Movie> movies){
        clear();
        if(movies != null ){
            for(Movie movie : movies){
                add(movie);
            }
        }
    }
}
