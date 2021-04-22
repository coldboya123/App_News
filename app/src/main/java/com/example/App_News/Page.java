package com.example.App_News;

import java.io.Serializable;

public class Page implements Serializable {
    private String id;
    private String title;
    private String date;
    private String content;
    private String img;
    private String view;


    public Page(String id, String title, String date, String content, String img, String view) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.img = img;
        this.view = view;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getImg() {
        return img;
    }

    public String getView() {
        return view;
    }
}
