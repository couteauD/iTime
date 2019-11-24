package com.example.itime.model;

import android.graphics.Bitmap;

public class Schedule {
    private String title;
    private String date;
    private String remark;
    private Bitmap img;

    public Schedule(String title, String date, String remark, Bitmap img) {
        this.setTitle(title);
        this.setDate(date);
        this.setRemark(remark);
        this.setImg(img);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
