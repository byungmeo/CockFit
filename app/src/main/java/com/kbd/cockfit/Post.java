package com.kbd.cockfit;

public class Post {
    private int postNumber;
    private String postTitle;
    private String writer;
    private String postDate;
    private String postContent;

    public Post() {

    }

    public Post(String postTitle, String writer, String postDate) {
        this.postTitle = postTitle;
        this.writer = writer;
        this.postDate = postDate;
    }

    public Post(int postNumber, String postTitle, String postDate, String postContent, String writer) {
        this.postNumber = postNumber;
        this.postTitle = postTitle;
        this.writer = writer;
        this.postDate = postDate;
        this.postContent = postContent;
    }

    public int getPostNumber() { return postNumber; }
    public String getPostTitle() {
        return postTitle;
    }
    public String getWriter() { return writer; }
    public String getPostDate() {
        return postDate;
    }
    public String getPostContent() { return postContent; }

    public void setPostNumber(int number) { this.postNumber = number; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }
    public void setWriter(String nickname) { this.writer = nickname; }
    public void setPostDate(String postDate) { this.postDate = postDate; }
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

}
