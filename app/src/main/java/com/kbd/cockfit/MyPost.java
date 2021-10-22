package com.kbd.cockfit;

public class MyPost {
    private String postName;
    private String postAbstract;
    private String postDate;

    public MyPost(String postName, String postAbstract, String postDate) {
        this.postName = postName;
        this.postAbstract = postAbstract;
        this.postDate = postDate;
    }

    public String getPostName() {
        return postName;
    }

    public String getPostAbstract() {
        return postAbstract;
    }

    public String getPostDate() {
        return postDate;
    }
}
