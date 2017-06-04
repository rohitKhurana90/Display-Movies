package com.movie.tmdb.moviedisplay.model;

/**
 * Created by rohit.khurana on 6/3/2017.
 */

public class MovieResponse {

    public String page;
    public Movie[] results;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Movie[] getResult() {
        return results;
    }

    public void setResult(Movie[] result) {
        this.results = result;
    }
}
