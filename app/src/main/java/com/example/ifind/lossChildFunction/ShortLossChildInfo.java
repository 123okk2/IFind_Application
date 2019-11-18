package com.example.ifind.lossChildFunction;

import android.graphics.Bitmap;

public class ShortLossChildInfo {
    private String pid;
    private Bitmap pic;
    private String name;
    private int age;
    private String sight;
    private String telNum;
    private String character;
    private String lossDate;

    public ShortLossChildInfo(String pid, Bitmap pic, String name, int age, String sight, String lossDate) {
        this.pid = pid;
        this.pic = pic;
        this.name = name;
        this.age = age;
        this.sight = sight;
        this.lossDate = lossDate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public ShortLossChildInfo(String pid, Bitmap pic, String name, int age, String sight, String telNum, String character, String lossDate){
        this.pid = pid;
        this.pic = pic;
        this.name = name;
        this.age = age;
        this.sight = sight;
        this.telNum =telNum;
        this.character = character;
        this.lossDate = lossDate;
    }

    public Bitmap getPic() { return pic; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSight() { return sight; }
    public String getTelNum() { return telNum; }
    public String getCharacter() { return character; }
    public String getLossDate() { return lossDate; }

}
