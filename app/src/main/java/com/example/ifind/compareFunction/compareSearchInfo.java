package com.example.ifind.compareFunction;

import android.graphics.Bitmap;

public class compareSearchInfo {
    private int type; //장단기 구분
    private String pid; //작성자
    private String name; //아이 이름
    private Bitmap pic; //사진
    private String percentage;

    public compareSearchInfo(int type, String pid, String name, Bitmap pic, String percentage) {
        this.type = type;
        this.pid = pid;
        this.name = name;
        this.pic = pic;
        this.percentage = percentage;
    }

    public int getType() { return type; }
    public String getPid() { return pid; }
    public String getName() { return name; }
    public Bitmap getPic() { return pic; }
    public String getPercentage() { return percentage; }
}
