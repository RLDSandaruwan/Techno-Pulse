package com.example.technopulse1;

public class News {
    public String title, description, imageUrl, author, category;
    public long publishTime;

    public News() {} // required

    public News(String title, String description, String imageUrl, String author, long publishTime, String category) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.author = author;
        this.publishTime = publishTime;
        this.category = category;
    }
}

