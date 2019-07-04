package com.example.rog.movlog;

public class Model {
    public String title, genre, image, rating, year, baner1, baner2, baner3, description, review;
    public String name, email, password;

    public static User currentUser;

    public static final String INTENT_MOVIE_ID = "MovieId";

    public Model() {

    }

    public Model(String title, String genre, String image, String rating, String year, String baner1, String baner2, String baner3, String description, String review, String name, String email, String password) {
        this.title = title;
        this.genre = genre;
        this.image = image;
        this.rating = rating;
        this.year = year;
        this.baner1 = baner1;
        this.baner2 = baner2;
        this.baner3 = baner3;
        this.description = description;
        this.review = review;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBaner1() {
        return baner1;
    }

    public void setBaner1(String baner1) {
        this.baner1 = baner1;
    }

    public String getBaner2() {
        return baner2;
    }

    public void setBaner2(String baner2) {
        this.baner2 = baner2;
    }

    public String getBaner3() {
        return baner3;
    }

    public void setBaner3(String baner3) {
        this.baner3 = baner3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Model.currentUser = currentUser;
    }
}
