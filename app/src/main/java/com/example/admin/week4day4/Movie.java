package com.example.admin.week4day4;

/**
 * Created by  Admin on 11/30/2017.
 */

public class Movie {
    String name;
    String genre;
    String year;

    public Movie() {
    }

    public Movie(String name, String genre, String year) {
        this.name = name;
        this.genre = genre;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
