package cz.muni.fi.pv168.easyfood.business.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Recipe extends Entity {
    private String name;
    private List<IngredientWithAmount> ingredients;
    private String description;
    private int preparationTime;
    private int portions;
    private Category category;

    public Recipe() {
    }

    public Recipe(String guid,
                  String name,
                  List<IngredientWithAmount> ingredients,
                  String description,
                  int preparationTime,
                  int portions,
                  Category category) {
        super(guid);
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.preparationTime = preparationTime;
        this.portions = portions;
        this.category = category;
    }

    public Recipe(String name,
                  List<IngredientWithAmount> ingredients,
                  String description,
                  int preparationTime,
                  int portions,
                  Category category) {
        this.name = name;
        this.ingredients = ingredients;
        this.description = description;
        this.preparationTime = preparationTime;
        this.portions = portions;
        this.category = category;
    }

    public static Recipe createEmptyRecipe() {
        return new Recipe(UUID.randomUUID().toString(), "", new ArrayList<>(), "", 0, 0, null);
    }

    public void setCategory(Category category) {
        this.category = Objects.requireNonNull(category, "category must not be null");
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public List<IngredientWithAmount> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public void setIngredients(List<IngredientWithAmount> ingredients) {
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients must not be null");
    }

    @JsonIgnore
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
        this.description = Objects.requireNonNull(description, "description must not be null");
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

    @JsonIgnore
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

    @JsonIgnore
    public String getFormattedCalories() {
        return Double.valueOf(getCalories()).intValue() + " kJ";
    }

    public void addIngredient(IngredientWithAmount ingredientWithAmount) {
        ingredients.add(ingredientWithAmount);
    }

    public void addIngredient(Ingredient ingredient, double amount) {
        addIngredient(new IngredientWithAmount(ingredient, amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe recipe)) {
            return false;
        }
        return preparationTime == recipe.preparationTime && portions == recipe.portions &&
                Objects.equals(name, recipe.name) && Objects.equals(ingredients, recipe.ingredients) &&
                Objects.equals(description, recipe.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients, description, preparationTime, portions);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", description='" + description + '\'' +
                ", preparationTime=" + preparationTime +
                ", portions=" + portions +
                ", category=" + category +
                '}';
    }
}
