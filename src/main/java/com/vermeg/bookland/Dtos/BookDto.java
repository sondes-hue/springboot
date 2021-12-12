package com.vermeg.bookland.Dtos;

public class BookDto {
    private int rank;
    private String title,description,image;

    public BookDto(int rank, String title, String description, String image) {
        this.rank = rank;
        this.title = title;
        this.description = description;
        this.image = image;
    }
    public BookDto() {}

    public int getRank() {
        return rank;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
