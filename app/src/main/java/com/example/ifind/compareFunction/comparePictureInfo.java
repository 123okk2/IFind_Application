package com.example.ifind.compareFunction;

import android.graphics.Bitmap;

public class comparePictureInfo {
    private Bitmap postPic;
    private Bitmap usrPic;
    private int percentage;

    public Bitmap getPostPic() {
        return postPic;
    }

    public void setPostPic(Bitmap postPic) {
        this.postPic = postPic;
    }

    public Bitmap getUsrPic() {
        return usrPic;
    }

    public void setUsrPic(Bitmap usrPic) {
        this.usrPic = usrPic;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public comparePictureInfo(Bitmap postPic, Bitmap usrPic, int percentage) {
        this.postPic = postPic;
        this.usrPic = usrPic;
        this.percentage = percentage;
    }
}
