package cz.fi.muni.pv168.easyfood.bussiness.model;

import java.util.Objects;
import java.util.UUID;

public class IngredientWithAmount extends Entity {
    private Ingredient ingredient;
    private double amount;

    public IngredientWithAmount(String name, double calories, Unit unit, double amount) {
        super(UUID.randomUUID().toString());
        this.ingredient = new Ingredient(name, calories, unit);
        this.amount = amount;
    }

    public IngredientWithAmount(Ingredient ingredient, double amount) {
        super(UUID.randomUUID().toString());
        this.ingredient = new Ingredient(ingredient.getName(), ingredient.getCalories(), ingredient.getUnit());
        this.amount = amount;
    }

    public String getName() {
        return ingredient.getName();
    }

    public double getAmount() {
        return amount;
    }

    public String getFormattedAmount() {
        return amount + " (" + ingredient.getUnit().getAbbreviation() + ")";
    }

    public Ingredient getIngredient() {
        return ingredient;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IngredientWithAmount that = (IngredientWithAmount) o;
        return ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient);
    }
}