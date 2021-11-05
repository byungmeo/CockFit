package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private int number;
    private String title;
    private String writer;
    private String date;
    private String content;

    public Post() {

    }

    public Post(String title, String writer, String date) {
        this.number = 0;
        this.title = title;
        this.writer = writer;
        this.date = date;
        this.content = "";
    }

    public Post(int number, String title, String date, String content, String writer) {
        this.number = number;
        this.title = title;
        this.writer = writer;
        this.date = date;
        this.content = content;
    }

    protected Post(Parcel in) {
        number = in.readInt();
        title = in.readString();
        writer = in.readString();
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

    public int getNumber() { return number; }
    public String getTitle() {
        return title;
    }
    public String getWriter() { return writer; }
    public String getDate() {
        return date;
    }
    public String getContent() { return content; }

    public void setNumber(int number) { this.number = number; }
    public void setTitle(String title) { this.title = title; }
    public void setWriter(String writer) { this.writer = writer; }
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
        dest.writeInt(number);
        dest.writeString(title);
        dest.writeString(writer);
        dest.writeString(date);
        dest.writeString(content);
    }
}
