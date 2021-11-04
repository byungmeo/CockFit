package com.kbd.cockfit;

public class Recipe {
    public String name=""; //칵테일 이름
    public int proof=0; //칵테일 도수
    public String base=""; //칵테일 기주
    public String ingredient=""; //칵테일 재료
    public String equipment=""; //칵테일 장비
    public String description=""; //칵테일 제조에 대한 상세설명
    public String[] tags = {};

    public Recipe(String name, int proof, String base, String ingredient, String equipment, String description, String[] tags) {
        this.name = name;
        this.proof = proof;
        this.base = base;
        this.ingredient = ingredient;
        this.equipment = equipment;
        this.description = description;
        this.tags = tags;
    }
}