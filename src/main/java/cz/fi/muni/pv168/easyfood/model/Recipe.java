package cz.fi.muni.pv168.easyfood.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private String name;
    private List<IngredientWithAmount> ingredients;
    private String description;
    private int preparationTime;
    private int portions;

    public Recipe(String name, List<IngredientWithAmount> ingredients, String description, int preparationTime, int portions) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.preparationTime = preparationTime;
        this.portions = portions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientWithAmount> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public void setIngredients(List<IngredientWithAmount> ingredients) {
        this.ingredients = ingredients;
    }

    public double getCalories() {
        double total = 0;
        for (var ingredient : ingredients) {
            total += ingredient.getIngredient().getCalories() * ingredient.getAmount();
        }
        return total;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public String getFormattedPreparationTime() {
        StringBuilder result = new StringBuilder();
        if (preparationTime >= 60) {
            result.append(preparationTime / 60).append(" h");
            preparationTime %= 60;
        }
        if (preparationTime % 60 != 0) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(preparationTime % 60).append(" m");
        }
        return result.toString();
    }

    public String getFormattedCalories() {
        return Double.valueOf(getCalories()).intValue() + " kJ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe recipe)) return false;
        return preparationTime == recipe.preparationTime && portions == recipe.portions && Objects.equals(name, recipe.name) && Objects.equals(ingredients, recipe.ingredients) && Objects.equals(description, recipe.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, description, preparationTime, portions);
    }
}