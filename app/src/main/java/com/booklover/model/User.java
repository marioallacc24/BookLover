package com.booklover.model;

public class User {
    private String name;
    private String rola;
    private int numberOfBooks;
    private String email;
    private String profilePicture;

    public User() {
    }

    public User(String name, String rola, int numberOfBooks, String email, String profilePicture) {
        this.name = name;
        this.rola = rola;
        this.numberOfBooks = numberOfBooks;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
