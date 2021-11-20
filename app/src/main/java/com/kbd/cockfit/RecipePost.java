package com.kbd.cockfit;

import android.os.Parcel;

public class RecipePost extends Post {
    private String recipeId;

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
    }

    protected RecipePost(Parcel in) {
        super(in);
        recipeId = in.readString();
    }

    public String getRecipeId() { return this.recipeId; }

    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(recipeId);
    }
}
