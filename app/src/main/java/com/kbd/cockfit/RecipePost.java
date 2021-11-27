package com.kbd.cockfit;

import android.os.Parcel;

import java.util.HashMap;

public class RecipePost extends Post {
    private String recipeId;
    private HashMap<String, String> bookmarkUidMap; //레시피를 즐겨찾기 한 사용자의 uid 리스트

    public static final Creator<RecipePost> CREATOR = new Creator<RecipePost>() {
        @Override
        public RecipePost createFromParcel(Parcel in) {
            return new RecipePost(in);
        }

        @Override
        public RecipePost[] newArray(int size) {
            return new RecipePost[size];
        }
    };

    public RecipePost() {

    }

    public RecipePost(String title, String nickname, String uid, String date) {
        super(title, nickname, uid, date);
        recipeId = null;
        bookmarkUidMap = null;
    }

    protected RecipePost(Parcel in) {
        super(in);
        recipeId = in.readString();
        bookmarkUidMap = in.readHashMap(HashMap.class.getClassLoader());
    }

    public String getRecipeId() { return this.recipeId; }
    public HashMap<String, String> getBookmarkUidMap() { return bookmarkUidMap; }

    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }
    public void setBookmarkUidMap(HashMap<String, String> bookmarkUidMap) { this.bookmarkUidMap = bookmarkUidMap; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(recipeId);
        dest.writeMap(bookmarkUidMap);
    }
}
