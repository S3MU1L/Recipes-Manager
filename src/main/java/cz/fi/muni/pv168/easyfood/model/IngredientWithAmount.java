package cz.fi.muni.pv168.easyfood.model;

import java.util.Objects;

public class IngredientAndAmount{
    private Ingredient ingredient;
    private double amount;

    public IngredientAndAmount(String name, double calories, Unit unit, double amount) {
        this.ingredient = new Ingredient(name, calories, unit);
        this.amount = amount;
    }

    public IngredientAndAmount(Ingredient ingredient, double amount) {
        this.ingredient = new Ingredient(ingredient.getName(), ingredient.getCalories(), ingredient.getUnit());
        this.amount = amount;
        this.ingredient.setID(ingredient.getID());
    }

    public String getName() {
        return ingredient.getName();
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return "" + amount + ' ' + getIngredient().getUnit();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Long getID() {
        return ingredient.getID();
    }

    public String getFormattedCalories() {
        return getIngredient().getCalories() * amount + " kJ";
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public IngredientAndAmount getShallowCopy() { // ingredient is not copied
        return new IngredientAndAmount(ingredient, amount);
    }

    @Override
    public String toString() {
         return super.toString() +
                "amount=" + amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientAndAmount that = (IngredientAndAmount) o;
        return ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient);
    }
}