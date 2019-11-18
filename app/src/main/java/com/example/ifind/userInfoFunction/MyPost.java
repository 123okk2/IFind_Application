package com.example.ifind.userInfoFunction;

import android.graphics.Bitmap;

public class MyPost {
    private String id;
    private String name;
    private Bitmap pic;
    private String missingDate;
    private int type;

    public void setName(String name) {
        this.name = name;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public void setMissingDate(String missingDate) {
        this.missingDate = missingDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MyPost(String id, String name, Bitmap pic, String missingDate, int type) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.missingDate = missingDate;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPic() {
        return pic;
    }

    public String getMissingDate() {
        return missingDate;
    }
}
