package com.kbd.cockfit;

import java.util.ArrayList;

public class RecentForum {
    private String forumTitle;
    private ArrayList<Post> recentPostArrayList;

    public RecentForum(String forumTitle) {
        this.forumTitle = forumTitle;
        this.recentPostArrayList = new ArrayList<>();
    }

    public String getForumTitle() { return this.forumTitle; }
    public ArrayList<Post> getRecentPostArrayList() { return this.recentPostArrayList; }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }
    public void setRecentPostArrayList(ArrayList<Post> recentPostArrayList) { this.recentPostArrayList = recentPostArrayList; }
}
