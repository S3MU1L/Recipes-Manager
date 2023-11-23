package cz.muni.fi.pv168.easyfood.business.model;

public class Ingredient extends Entity {
    private String name;
    private double calories;
    private Unit unit;

    public Ingredient(String guid, String name, double calories, Unit unit) {
        super(guid);
        setName(name);
        setCalories(calories);
        setUnit(unit);
    }

    public Ingredient(String name, double calories, Unit unit) {
        setName(name);
        setCalories(calories);
        setUnit(unit);
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
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", unit=" + unit +
                '}';
    }
}



