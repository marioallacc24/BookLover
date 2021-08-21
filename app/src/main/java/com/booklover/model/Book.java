package com.booklover.model;

public class Book {

    String name;
    String writer;
    String category;
    String edition;
    String description;
    String image;
    String donwload;

    public Book() {
    }

    public Book(String name, String writer, String category, String edition, String description, String image, String donwload) {
        this.name = name;
        this.writer = writer;
        this.category = category;
        this.edition = edition;
        this.description = description;
        this.image = image;
        this.donwload = donwload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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

    public String getDonwload() {
        return donwload;
    }

    public void setDonwload(String donwload) {
        this.donwload = donwload;
    }
}
