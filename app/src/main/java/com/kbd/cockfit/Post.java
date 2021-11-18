package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Post implements Parcelable {
    private String title; //글 제목
    private String nickname; //작성자의 닉네임
    private String uid; //작성자의 uid
    private String date; //글 작성일자
    private String content; //본문내용
    private HashMap<String, String> likeUidMap; //좋아요 한 사용자의 uid 리스트

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
        //firebase에서 getValue(Post.class)시, 이 생성자가 호출된 후,
        //setter로 할당합니다.
        //(firebase내의 key값이 setter명과 매칭이 되야 setter가호출됨)
        //ex) key값:uid -> setter명:setUid
    }

    public Post(String title, String nickname, String uid, String date) {
        this.title = title;
        this.nickname = nickname;
        this.uid = uid;
        this.date = date;
        this.content = "";
        this.likeUidMap = null;
    }

    public Post(String title, String nickname, String uid, String date, String content, HashMap<String,String> likeUidMap) {
        this.title = title;
        this.nickname = nickname;
        this.uid = uid;
        this.date = date;
        this.content = content;
        this.likeUidMap = likeUidMap;
    }

    protected Post(Parcel in) {
        title = in.readString();
        nickname = in.readString();
        uid = in.readString();
        date = in.readString();
        content = in.readString();
        likeUidMap = in.readHashMap(HashMap.class.getClassLoader());
    }



    public String getTitle() { return title; }
    public String getNickname() { return nickname; }
    public String getUid() { return uid; }
    public String getDate() { return date; }
    public String getContent() { return content; }
    public HashMap<String, String> getLikeUidMap() { return likeUidMap; }

    public void setTitle(String title) { this.title = title; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setUid(String uid) { this.uid = uid; }
    public void setDate(String date) { this.date = date; }
    public void setContent(String content) { this.content = content; }
    public void setLikeUidMap(HashMap<String, String> likeUidMap) { this.likeUidMap = likeUidMap; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(nickname);
        dest.writeString(uid);
        dest.writeString(date);
        dest.writeString(content);
        dest.writeMap(likeUidMap);
    }
}
