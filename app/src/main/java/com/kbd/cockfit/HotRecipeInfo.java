package com.kbd.cockfit;

public class HotRecipeInfo {
    public String postId;
    public String recipeId;
    public String writerId;
    HotRecipeInfo(String postId, String recipeId, String writerId) {
        this.postId = postId;
        this.recipeId = recipeId;
        this.writerId = writerId;
    }
}
