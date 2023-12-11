package cz.muni.fi.pv168.easyfood.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.Color;
import java.util.Objects;
import java.util.UUID;

public class Category extends Entity {
    private String name;
    private Color color = new Color(255, 255, 255);
    private String htmlColor;

    public Category() {
    }

    public Category(
            String name,
            Color color) {
        setName(name);
        setColor(color);
    }

    public Category(
            String guid,
            String name,
            Color color) {
        super(guid);
        setName(name);
        setColor(color);
    }

    public String getHtmlColor() {
        return htmlColor;
    }


    public void setHTMLColor() {
        Color rectangleColor = getColor();
        String hexColor = String.format("#%02x%02x%02x", rectangleColor.getRed(), rectangleColor.getGreen(), rectangleColor.getBlue());
        String formattedString = "<html><div style='width: 1000px; height: 20px; background-color: " + hexColor + ";'></div></html>";
        htmlColor = formattedString;
    }

    public static Category createEmptyCategory() {
        return new Category(UUID.randomUUID().toString(), "", Color.WHITE);
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public Color getColor() {
        return color;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }


    public void setColor(Color color) {
        this.color = Objects.requireNonNull(color, "color must not be null");
        setHTMLColor();
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", color='" + color.toString() + '\'' +
                "}";
    }

}
