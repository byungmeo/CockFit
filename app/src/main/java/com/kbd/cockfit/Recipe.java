package com.kbd.cockfit;

public class Recipe {
    private int number = 0;
    private String name = ""; //칵테일 이름
    private String proof = "0"; //칵테일 도수
    private String base = ""; //칵테일 기주
    private String ingredient = ""; //칵테일 재료
    private String equipment = ""; //칵테일 장비
    private String description = ""; //칵테일 제조에 대한 상세설명
    private String[] tags = {};

    public Recipe() {
    }

    public Recipe(int number, String name, String proof, String base, String ingredient, String equipment, String description, String[] tags) {
        this.number = number;
        this.name = name;
        this.proof = proof;
        this.base = base;
        this.ingredient = ingredient;
        this.equipment = equipment;
        this.description = description;
        this.tags = tags;
    }

    public Recipe(String name, String proof, String base, String ingredient, String equipment, String description) {
        this.number = 0;
        this.name = name;
        this.proof = proof;
        this.base = base;
        this.ingredient = ingredient;
        this.equipment = equipment;
        this.description = description;
        this.tags = null;
    }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProof() { return proof; }
    public void setProof(String proof) { this.proof = proof; }
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public String getIngredient() { return ingredient; }
    public void setIngredient(String ingredient) { this.ingredient = ingredient; }
    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }
}