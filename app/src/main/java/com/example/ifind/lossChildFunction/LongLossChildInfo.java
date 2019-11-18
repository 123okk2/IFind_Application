package com.example.ifind.lossChildFunction;

import android.graphics.Bitmap;

public class LongLossChildInfo {
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String pid; //작성자 아이디
    private Bitmap pic;
    private Bitmap oldPic;
    private String name;
    private int age;
    private String sight;
    private String telNum;
    private String character;
    private String lossDate;

    public LongLossChildInfo(String pid, Bitmap pic, Bitmap oldPic, String name, int age, String sight, String lossDate) {
        //리스트용
        this.pid = pid;
        this.pic = pic;
        this.oldPic = oldPic;
        this.name = name;
        this.age = age;
        this.sight = sight;
        this.lossDate = lossDate;
    }

    public LongLossChildInfo(String pid, Bitmap pic, Bitmap oldPic, String name, int age, String sight, String telNum, String character, String lossDate){
        //그냥
        this.pid = pid;
        this.pic = pic;
        this.oldPic = oldPic;
        this.name = name;
        this.age = age;
        this.sight = sight;
        this.telNum = telNum;
        this.character = character;
        this.lossDate = lossDate;
    }

    public Bitmap getPic() { return pic; }

    public Bitmap getOldPic() { return oldPic; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getSight() { return sight; }
    public String getTelNum() { return telNum; }
    public String getCharacter() { return character; }
    public String getLossDate() { return lossDate; }

}