package com.kbd.cockfit;

public class Recipe {
    private String name;
    private String base;
    private String[] tags;

    public Recipe(String name, String base, String[] tags) {
        this.name = name;
        this.base = base;
        this.tags = tags;
    }

    public String getName() {
        return this.name;
    }

    public String getBase() {
        return this.base;
    }

    public String[] getTags() {
        return  this.tags;
    }
}
