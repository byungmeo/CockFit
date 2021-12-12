package com.kbd.cockfit;

public class Notify {
    private String title;
    private String text;
    private String type;
    private String date;
    private String uid;
    private String notifyId;


    public Notify() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Notify(String title, String text, String type, String date, String uid) {
        this.title=title;
        this.text=text;
        this.type=type;
        this.date=date;
        this.uid = uid;
        this.notifyId = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
    }
}
