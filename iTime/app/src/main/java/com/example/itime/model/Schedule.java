package com.example.itime.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String title;
    private String date;
    private String remark;
    private byte[] bitmapByte;

    public Schedule(String title, String date, String remark, byte[] bitmapByte) {
        this.setTitle(title);
        this.setDate(date);
        this.setRemark(remark);
        this.setbitmapByte(bitmapByte);
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

    public byte[] getbitmapByte() {
        return bitmapByte;
    }

    public void setbitmapByte(byte[] bitmapByte) {
        this.bitmapByte = bitmapByte;
    }
}