package com.example.ifind.userInfoFunction;

import android.graphics.Bitmap;

public class UserInfo {
    String id;
    String pw;
    String addr;
    String name;
    String phone;
    Bitmap photo;

    public UserInfo (String id, String pw, String addr, String name, String phone) {
        this.id = id;
        this.pw = pw;
        this.addr = addr;
        this.name = name;
        this.phone = phone;
    }
    public UserInfo (String id, String pw, String addr, String name, String phone, Bitmap photo) {
        this.id = id;
        this.pw = pw;
        this.addr = addr;
        this.name = name;
        this.photo = photo;
        this.phone = phone;
    }

    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    public void setPw(String pw) { this.pw = pw; }
    public String getPw() { return pw; }
    public void setAddr(String addr) { this.addr = addr; }
    public String getAddr() { return addr; }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPhone() { return phone; }
    public void setPhoto(Bitmap photo) {this.photo = photo; }
    public Bitmap getPhoto() { return photo; }
}
