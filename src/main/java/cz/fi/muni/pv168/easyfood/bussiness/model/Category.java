package cz.fi.muni.pv168.easyfood.model;

import cz.fi.muni.pv168.easyfood.bussiness.model.Entity;

import java.awt.Color;

public class Category extends Entity {
    private String name;
    private Color color;

    public Category(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static Category createEmptyCategory() {
        return new Category("", Color.WHITE);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
