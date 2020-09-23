package com.example.covidtest.data;

public class RssFeedModel {

    public String title;
    public String description;
    public String link;

    public RssFeedModel(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
