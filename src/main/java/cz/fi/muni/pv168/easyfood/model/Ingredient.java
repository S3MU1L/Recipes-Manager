package cz.fi.muni.pv168.easyfood.model;

import java.util.Objects;

public class Ingredient {
    private String name;
    private double calories;
    private Unit unit;

    public Ingredient(String name, double calories, Unit unit) {
        this.name = name;
        this.calories = calories;
        this.unit = unit;
    }

    public static Ingredient createEmptyIngredient() {
        return new Ingredient("", 0, Unit.EMPTY_UNIT);
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
        if (calories == 0.0) {
            return "";
        }
        int multiplier = 1;
        if (calories < 10) {
            double log10Calories = Math.log10(calories);
            double exponent = 1 - Math.floor(log10Calories);
            multiplier = (int) Math.pow(10, exponent);
        }

        int formattedCalories = (int) Math.round(calories * multiplier);
        return formattedCalories + " kJ (" + multiplier + " " + unit + ")";
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient that)) return false;
        return Double.compare(calories, that.calories) == 0 && Objects.equals(name, that.name) && unit == that.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, calories, unit);
    }

}
