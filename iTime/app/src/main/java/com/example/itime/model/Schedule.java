package com.example.itime.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String title;
    private String date;
    private String time;
    private String remark;
    private String cycle;
    private String mark;
    private byte[] bitmapByte;

    public Schedule(String title, String date,String time, String remark, String cycle,String mark,byte[] bitmapByte) {
        this.setTitle(title);
        this.setDate(date);
        this.setTime(time);
        this.setRemark(remark);
        this.setCycle(cycle);
        this.setMark(mark);
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCycle(){
        return cycle;
    }

    public void setCycle(String cycle){
        this.cycle=cycle;
    }

    public String getMark(){
        return mark;
    }

    public void setMark(String mark){
        this.mark=mark;
    }

    public byte[] getbitmapByte() {
        return bitmapByte;
    }

    public void setbitmapByte(byte[] bitmapByte) {
        this.bitmapByte = bitmapByte;
    }
}
