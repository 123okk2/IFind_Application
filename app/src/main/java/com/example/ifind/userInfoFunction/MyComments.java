package com.example.ifind.userInfoFunction;

public class MyComments {
    String id;
    String name;
    String comments;
    String date;
    int type;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setType(int type) { this.type = type; }
    public int getType() { return type; }

    public MyComments(String id, String name, String comments, String date, int type) {
        this.id = id;
        this.name = name;
        this.comments = comments;
        this.date = date;
        this.type = type;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
