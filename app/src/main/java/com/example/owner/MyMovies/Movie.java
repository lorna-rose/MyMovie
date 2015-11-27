package com.example.owner.MyMovies;

/**
 Movie class constructing the movie object which is called in MovieListLoader and MovieSearchListLoader
 */
public class Movie {

    private int _id;
    private String movie_name;
    private String movie_genre;
    private String movie_rating;
    private String movie_seen;

    public Movie(int _id, String movie_name, String movie_genre, String movie_rating, String movie_seen) {
        this._id = _id;
        this.movie_name = movie_name;
        this.movie_genre = movie_genre;
        this.movie_rating = movie_rating;
        this.movie_seen = movie_seen;
    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_genre() {
        return movie_genre;
    }

    public void setMovie_genre(String movie_genre) {
        this.movie_genre = movie_genre;
    }

    public String getMovie_rating() {
        return movie_rating;
    }

    public void setMovie_rating(String movie_rating) {
        this.movie_rating = movie_rating;
    }

    public String getMovie_seen() {
        return movie_seen;
    }

    public void setMovie_seen(String movie_seen) {
        this.movie_seen = movie_seen;
    }
}
