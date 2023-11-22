package cz.fi.muni.pv168.easyfood.model;

import java.util.Objects;

public class Ingredient {
    private Long ID;
    private String name;
    private double calories;
    private Unit unit;

    public Ingredient(String name, double calories, Unit unit) {
        this.name = name;
        this.calories = calories;
        this.unit = unit;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public String getFormattedCalories() {
        int multiplier = 1;
        if (calories < 10) {
            multiplier = (int) Math.pow(10, 1 - Math.floor(Math.log10(calories)));
        }
        return Math.round(calories * multiplier) + " kJ (" + multiplier + " " + unit + ")";
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Unit getUnit() {
        return unit;
    }

    public String getFormattedIngredient() {
        return name + ": " + unit + " (" + calories + " kJ/" + unit +")";
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Ingredient getCopy() {
        Ingredient copy = new Ingredient(name, calories, unit);
        copy.setID(getID());
        return copy;
    }

    @Override
    public String toString() {
        return "Ingredient:" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", unit=" + unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(ID, that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
