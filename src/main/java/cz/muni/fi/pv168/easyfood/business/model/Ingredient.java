package cz.muni.fi.pv168.easyfood.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class Ingredient extends Entity {
    private String name;
    private double calories;
    private Unit unit;

    public Ingredient() {}

    public Ingredient(
            String guid,
            String name,
            double calories,
            Unit unit) {
        super(guid);
        this.name = name;
        this.calories = calories;
        this.unit = unit;
    }

    public Ingredient(
            String name,
            double calories,
            Unit unit) {
        this.name = name;
        this.calories = calories;
        this.unit = unit;
    }

    // Copy constructor
    public Ingredient(Ingredient i) {
        this(
                i.guid,
                i.name,
                i.calories,
                i.unit
        );
    }

    public static Ingredient createEmptyIngredient() {
        return new Ingredient("", 0, Unit.createEmptyUnit());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public double getCalories() {
        return calories;
    }

    @JsonIgnore
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
        return formattedCalories + " kJ (" + multiplier + " " + unit.getName() + ")";
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient that)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", unit=" + unit +
                '}';
    }
}
