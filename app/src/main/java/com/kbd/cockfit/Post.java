package com.kbd.cockfit;

public class Post {
    private int postNumber;
    private String postName;
    private String postAbstract;
    private String postDate;
    private String postContent;
    private String writerNickname;

    public Post() {

    }

    public Post(String postName, String postAbstract, String postDate) {
        this.postName = postName;
        this.postAbstract = postAbstract;
        this.postDate = postDate;
    }

    public Post(int postNumber, String postName, String postAbstract, String postDate, String postContent, String writerNickname) {
        this.postNumber = postNumber;
        this.postName = postName;
        this.postAbstract = postAbstract;
        this.postDate = postDate;
        this.postContent = postContent;
        this.writerNickname = writerNickname;
    }

    public int getPostNumber() { return postNumber; }
    public String getPostName() {
        return postName;
    }
    public String getPostAbstract() {
        return postAbstract;
    }
    public String getPostDate() {
        return postDate;
    }
    public String getPostContent() { return postContent; }
    public String getWriterNickname() { return writerNickname; }

    public void setPostNumber(int number) { this.postNumber = number; }
    public void setPostName(String postName) { this.postName = postName; }
    public void setPostAbstract(String postAbstract) { this.postAbstract = postAbstract; }
    public void setPostDate(String postDate) { this.postDate = postDate; }
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
    public void setWriterNickname(String nickname) { this.writerNickname = nickname; }
}
