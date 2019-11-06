package com.example.itime.data.model;

public class information {
    //标题
    private String title;
    //备注
    private String remark;
    //日期
    private String data;
    //周期
    private String cycle;
    //图片id
    private int imgid;
    //标签
    private String tab;

    public information(String title, String remark, String data, String cycle, int imgid, String tab) {
        this.title = title;
        this.remark = remark;
        this.data = data;
        this.cycle = cycle;
        this.imgid = imgid;
        this.tab = tab;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }





}
