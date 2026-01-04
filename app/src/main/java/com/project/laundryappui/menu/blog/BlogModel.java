package com.project.laundryappui.menu.blog;

public class BlogModel {
    private int imageResource;
    private String title;
    private String excerpt;
    private String category;
    private String date;
    private String readTime;
    private String url;

    public BlogModel(int imageResource, String title, String excerpt, String category, String date, String readTime, String url) {
        this.imageResource = imageResource;
        this.title = title;
        this.excerpt = excerpt;
        this.category = category;
        this.date = date;
        this.readTime = readTime;
        this.url = url;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
