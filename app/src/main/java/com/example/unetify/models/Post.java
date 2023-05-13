package com.example.unetify.models;

public class Post {

    private String id, title, description, image, idUser;
    private long timestamp;

    public Post() {

    }

    public Post(String id, String title, String description, String image, String idUser, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.idUser = idUser;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
