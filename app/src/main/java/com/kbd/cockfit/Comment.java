package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String text;
    private String nickname;
    private String uid;

    public Comment() {

    }

    public Comment(String text, String nickname, String uid) {
        this.text = text;
        this.nickname = nickname;
        this.uid = uid;
    }

    protected Comment(Parcel in) {
        text = in.readString();
        nickname = in.readString();
        uid = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) { return new Comment(in); }

        @Override
        public Comment[] newArray(int size) { return new Comment[size]; }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(nickname);
        dest.writeString(uid);
    }

    public String getText() { return text; }
    public String getNickname() { return nickname; }
    public String getUid() { return uid; }

    public void setText(String text) { this.text = text; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setUid(String uid) { this.uid = uid; }
}
