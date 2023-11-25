package cz.muni.fi.pv168.easyfood.business.model;

import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

public class Category extends Entity {
    private String name;
    private Color color;

    public Category(
            String name,
            Color color) {
        this.name = name;
        this.color = color;
    }

    public Category(
            String guid,
            String name,
            Color color) {
        super(guid);
        this.name = name;
        this.color = color;
    }

    public static Category createEmptyCategory() {
        return new Category(UUID.randomUUID().toString(), "", Color.WHITE);
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }


    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", color='" + color.toString() + '\'' +
                "}";
    }
}
