package com.example.rog.movlog;

public class Rating {
    private String UserId;
    private String movieId;
    private String ratevalue;
    private String Comment;

    public Rating(String userId, String movieId, String ratevalue, String comment) {
        UserId = userId;
        this.movieId = movieId;
        this.ratevalue = ratevalue;
        Comment = comment;
    }

    public Rating() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getRatevalue() {
        return ratevalue;
    }

    public void setRatevalue(String ratevalue) {
        this.ratevalue = ratevalue;
    }

    public String getComment() {

        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}


