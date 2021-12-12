package com.kbd.cockfit;

import android.net.Uri;

public class HotRecipe {
    public String name;
    public Uri imageUri;
    public HotRecipeInfo hotRecipeInfo;
    public Post post;

    HotRecipe(String name, Uri imageUri, HotRecipeInfo hotRecipeInfo, Post post) {
        this.name = name;
        this.imageUri = imageUri;
        this.hotRecipeInfo = hotRecipeInfo;
        this.post = post;
    }
}
