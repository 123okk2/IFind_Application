package com.example.ifind.replyFunction;

import android.graphics.Bitmap;

public class replyInfo {
    private String id;
    private String cid;
    private Bitmap pic;
    private String name;
    private String reply;
    private String usrName;

    public String getUsrName() { return usrName; }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    private String dates;

    public replyInfo(String id, String cid, Bitmap pic, String name, String reply, String dates, String usrName) {
        this.id = id;
        this.cid = cid;
        this.pic = pic;
        this.name = name;
        this.reply = reply;
        this.dates = dates;
        this.usrName = usrName;
    }

    public String getID() { return id; }
    public String getCid() { return cid; }
    public Bitmap getPic() { return pic; }
    public String getName() { return name; }
    public String getReply() { return reply; }
}
