package com.kbd.cockfit;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.List;

public class Recipe implements Parcelable {
    private int number = 0;
    private String name = ""; //칵테일 이름
    private String proof = "0"; //칵테일 도수
    private String base = ""; //칵테일 기주
    private List<String> ingredient = null; //칵테일 재료
    private List<String> equipment = null; //칵테일 장비
    private String description = ""; //칵테일 제조에 대한 상세설명
    private List<String> tags = null;

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe() {
    }

    public Recipe(int number, String name, String proof, String base, List<String> ingredient, List<String> equipment, String description, List<String> tags) {
        this.number = number;
        this.name = name;
        this.proof = proof;
        this.base = base;
        this.ingredient = ingredient;
        this.equipment = equipment;
        this.description = description;
        this.tags = tags;
    }

    public Recipe(int number, String name, String proof, String base, String[] ingredient, String[] equipment, String description, String[] tags) {
        this.number = number;
        this.name = name;
        this.proof = proof;
        this.base = base;
        this.ingredient = Arrays.asList(ingredient);
        this.equipment = Arrays.asList(equipment);
        this.description = description;
        this.tags = Arrays.asList(tags);
    }

    protected Recipe(Parcel in) {
        number = in.readInt();
        name = in.readString();
        proof = in.readString();
        base = in.readString();
        ingredient = in.createStringArrayList();
        equipment = in.createStringArrayList();
        description = in.readString();
        tags = in.createStringArrayList();
    }

    public int getNumber() { return number; }
    public void setNumber() { this.number = number; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProof() { return proof; }
    public void setProof(String proof) { this.proof = proof; }
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public List<String> getIngredient() { return ingredient; }
    public void setIngredient(List<String> ingredient) { this.ingredient = ingredient; }
    public List<String> getEquipment() { return equipment; }
    public void setEquipment(List<String> equipment) { this.equipment = equipment; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(name);
        dest.writeString(proof);
        dest.writeString(base);
        dest.writeStringList(ingredient);
        dest.writeStringList(equipment);
        dest.writeString(description);
        dest.writeStringList(tags);
    }
}