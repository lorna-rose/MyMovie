package com.example.owner.MyMovies;


import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

/**
 Search activity implements layout of search results
 */
public class SearchActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();
    private MovieCustomAdapter mMovieCustomAdapter;
    private static int LOADER_ID = 2;
    private ContentResolver mContentResolver;
    private List<Movie> moviesRetrieved;
    private ListView listView;
    private EditText mSearchEditText;
    private Button mSearchMovieButton;
    private String matchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        listView = (ListView) findViewById(R.id.searchResultsList);
        mSearchEditText = (EditText) findViewById(R.id.searchName);
        mSearchMovieButton = (Button) findViewById(R.id.searchButton);
        mContentResolver = getContentResolver();
        mMovieCustomAdapter = new MovieCustomAdapter(SearchActivity.this, getSupportFragmentManager());
        listView.setAdapter(mMovieCustomAdapter);

        mSearchMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchText = mSearchEditText.getText().toString();
                getSupportLoaderManager().initLoader(LOADER_ID++, null, SearchActivity.this);
            }
        });
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MovieSearchListLoader(SearchActivity.this, MovieContract.URI_TABLE, this.mContentResolver, matchText);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mMovieCustomAdapter.setData(movies);
        this.moviesRetrieved = movies;
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMovieCustomAdapter.setData(null);
    }
}
