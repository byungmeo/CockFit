package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String title;
    private String writer;
    private String uid;
    private String date;
    private String content;

    public Post() {

    }

    public Post(String title, String writer, String uid, String date) {
        this.title = title;
        this.writer = writer;
        this.uid = uid;
        this.date = date;
        this.content = "";
    }

    public Post(String title, String writer, String uid, String date, String content) {
        this.title = title;
        this.writer = writer;
        this.uid = uid;
        this.date = date;
        this.content = content;
    }

    protected Post(Parcel in) {
        title = in.readString();
        writer = in.readString();
        uid = in.readString();
        date = in.readString();
        content = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getTitle() {
        return title;
    }
    public String getWriter() { return writer; }
    public String getUid() { return uid; }
    public String getDate() {
        return date;
    }
    public String getContent() { return content; }

    public void setTitle(String title) { this.title = title; }
    public void setWriter(String writer) { this.writer = writer; }
    public void setUid(String uid) { this.uid = uid; }
    public void setDate(String date) { this.date = date; }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(writer);
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeString(content);
    }
}
