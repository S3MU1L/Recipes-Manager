package cz.fi.muni.pv168.easyfood.model;

import java.awt.*;

/**
 * @author Tibor Pelegrin
 */
public class Category {
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
