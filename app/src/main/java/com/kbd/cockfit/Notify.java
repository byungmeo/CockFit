package com.kbd.cockfit;

public class Notify {
    private String title;
    private String text;
    private String nickname;
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

    public Notify(String title, String text, String nickname, String date, String uid) {
        this.title=title;
        this.text=text;
        this.nickname=nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
