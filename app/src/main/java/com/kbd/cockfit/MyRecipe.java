package com.kbd.cockfit;

import android.graphics.Bitmap;
import android.mtp.MtpConstants;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MyRecipe extends Recipe {
    private String uid;
    private String myRecipeId;
    private boolean isShare;
    private String sharePostId;

    public static final Creator<MyRecipe> CREATOR = new Creator<MyRecipe>() {
        @Override
        public MyRecipe createFromParcel(Parcel in) {
            return new MyRecipe(in);
        }

        @Override
        public MyRecipe[] newArray(int size) {
            return new MyRecipe[size];
        }
    };

    public String getUid() {
        return uid;
    }
    public String getMyRecipeId() {
        return myRecipeId;
    }
    public boolean getIsShare() { return isShare; }
    public String getSharePostId() {
        return sharePostId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setMyRecipeId(String myRecipeId) {
        this.myRecipeId = myRecipeId;
    }
    public void setIsShare(boolean isShare) { this.isShare = isShare; }
    public void setSharePostId(String sharePostId) { this.sharePostId = sharePostId; }

    public MyRecipe(){

    }

    public MyRecipe(int number, Bitmap src, String name, String proof, String base, List<String> ingredient, List<String> equipment, String description, List<String> tags) {
        super(number, src, name, proof, base, ingredient, equipment, description, tags);
        this.uid = null;
        this.myRecipeId = null;
        this.isShare = false;
        this.sharePostId = null;
    }

    public MyRecipe(int number, Bitmap src, String name, String proof, String base, String[] ingredient, String[] equipment, String description, String[] tags) {
        super(number, src, name, proof, base, ingredient, equipment, description, tags);
        this.uid = null;
        this.myRecipeId = null;
        this.isShare = false;
        this.sharePostId = null;
    }

    public MyRecipe(int number, String name, String proof, String base, String[] ingredient, String[] equipment, String description, String[] tags) {
        super(number, name, proof, base, ingredient, equipment, description, tags);
        this.uid = null;
        this.myRecipeId = null;
        this.isShare = false;
        this.sharePostId = null;
    }

    protected MyRecipe(Parcel in) {
        super(in);
        uid = in.readString();
        myRecipeId = in.readString();
        isShare = in.readByte() != 0; //min sdk버전때문에 in.readBoolean()을 사용 불가
        sharePostId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(uid);
        dest.writeString(myRecipeId);
        dest.writeByte((byte) (isShare ? 1 : 0)); //min sdk버전때문에 dest.writeBoolean()을 사용 불가
        dest.writeString(sharePostId);
    }
}
