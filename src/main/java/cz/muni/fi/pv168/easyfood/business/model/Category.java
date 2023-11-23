package cz.muni.fi.pv168.easyfood.business.model;

import java.awt.Color;

public class Category extends Entity {
    private String name;
    private Color color;

    public Category(
            String guid,
            String name,
            Color color
    ) {
        super(guid);
        setName(name);
        setColor(color);
    }

    public Category(
            String name,
            Color color
    ) {
        setName(name);
        setColor(color);
    }

    public static Category createEmptyCategory() {
        return new Category("", Color.WHITE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
