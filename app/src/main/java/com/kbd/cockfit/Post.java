package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Post implements Parcelable {
    private String title; //글 제목
    private String nickname; //작성자의 닉네임
    private String uid; //작성자의 uid
    private String date; //글 작성일자
    private String content; //본문내용
    private List<String> likeUidList; //좋아요 한 사용자의 uid 리스트

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

    public Post() {

    }

    public Post(String title, String nickname, String uid, String date) {
        this.title = title;
        this.nickname = nickname;
        this.uid = uid;
        this.date = date;
        this.content = "";
        this.likeUidList = Collections.emptyList();
    }

    public Post(String title, String nickname, String uid, String date, String content, List<String> likeUidList) {
        this.title = title;
        this.nickname = nickname;
        this.uid = uid;
        this.date = date;
        this.content = content;
        this.likeUidList = likeUidList;
    }

    protected Post(Parcel in) {
        title = in.readString();
        nickname = in.readString();
        uid = in.readString();
        date = in.readString();
        content = in.readString();
        likeUidList = in.createStringArrayList();
    }



    public String getTitle() { return title; }
    public String getNickname() { return nickname; }
    public String getUid() { return uid; }
    public String getDate() { return date; }
    public String getContent() { return content; }
    public List<String> getLikeUidList() { return likeUidList; }

    public void setTitle(String title) { this.title = title; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setUid(String uid) { this.uid = uid; }
    public void setDate(String date) { this.date = date; }
    public void setContent(String content) { this.content = content; }
    public void setLikeUidList(List<String> likeUidList) { this.likeUidList = likeUidList; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(nickname);
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeStringList(likeUidList);
    }
}
