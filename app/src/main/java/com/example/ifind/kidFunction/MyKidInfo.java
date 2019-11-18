package com.example.ifind.kidFunction;

import android.graphics.Bitmap;

public class MyKidInfo {
    private String name;
    private int age;
    private Bitmap photo;
    private String feature;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Bitmap getphoto() {
        return photo;
    }

    public void setphoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public MyKidInfo(String name, int age, Bitmap photo, String feature) {
        this.name = name;
        this.age = age;
        this.photo = photo;
        this.feature = feature;
    }

}
