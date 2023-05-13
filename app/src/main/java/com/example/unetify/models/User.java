package com.example.unetify.models;

public class User {

    private String id, email,username, imageProfile, imageCover;

    public User(){

    }

    public User(String id, String email, String username, String imageProfile, String imageCover) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.imageProfile = imageProfile;
        this.imageCover = imageCover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }
}
