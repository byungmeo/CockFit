package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Comment implements Parcelable {
    private String text;
    private String nickname;
    private String uid;
    private String date;
    private HashMap<String, String> likeUidMap;
    private String commentId;

    public Comment() {

    }

    public Comment(String text, String nickname, String uid, String date) {
        this.text = text;
        this.nickname = nickname;
        this.uid = uid;
        this.date = date;
        this.likeUidMap = null;
        this.commentId = null;
    }

    protected Comment(Parcel in) {
        text = in.readString();
        nickname = in.readString();
        uid = in.readString();
        date = in.readString();
        likeUidMap = in.readHashMap(HashMap.class.getClassLoader());
        commentId = in.readString();
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
        dest.writeString(date);
        dest.writeMap(likeUidMap);
        dest.writeString(commentId);
    }

    public String getText() { return text; }
    public String getNickname() { return nickname; }
    public String getUid() { return uid; }
    public String getDate() { return date; }
    public HashMap<String, String> getLikeUidMap() { return likeUidMap; }
    public String getCommentId() { return commentId; }

    public void setText(String text) { this.text = text; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setUid(String uid) { this.uid = uid; }
    public void setDate(String date) { this.date = date; }
    public void setLikeUidMap(HashMap<String, String> likeUidMap) { this.likeUidMap = likeUidMap; }
    public void setCommentId(String commentId) { this.commentId = commentId; }
}
