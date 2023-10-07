package cz.fi.muni.pv168.dietaryAssistantApp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Recipe {
    private Long ID;
    private String name;
    private List<IngredientAndAmount> ingredients;
    private String directions;
    private int prepTime;
    private int portions;

    public Recipe(String name, List<IngredientAndAmount> ingredients, String directions, int prepTime, int portions) {
        this.name = name;
        this.ingredients = ingredients;
        this.directions = directions;
        this.prepTime = prepTime;
        this.portions = portions;
    }

    public static Recipe getEmptyRecipe() {
        return new Recipe("name", new ArrayList<>(), "directions", 0, 0);
    }

    public void addIngredient(Ingredient ingredient, double amount) {
        ingredients.add(new IngredientAndAmount(ingredient, amount));
    }

    public void updateIngredient(Ingredient ingredient, double amount) {
        int i = ingredients.indexOf(new IngredientAndAmount(ingredient, 0)); // the same IDs
        if (i == -1) {
            throw new IllegalArgumentException("There is no ingredient " + ingredient + " in " + this + "to update");
        }
        ingredients.get(i).setAmount(amount);
        ingredients.get(i).setIngredient(ingredient);
    }

    public int deleteIngredient(Ingredient ingredient) {
        int i = ingredients.indexOf(new IngredientAndAmount(ingredient, 0)); // the same IDs
        if (i != -1) {
            ingredients.remove(i);
        }
        return i;
    }

    public List<IngredientAndAmount> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    public IngredientAndAmount getIngredientAndAmount(Ingredient ingredient) {
        int i = ingredients.indexOf(new IngredientAndAmount(ingredient, 0)); // the same IDs
        if (i == -1) {
            return null;
        }
        return ingredients.get(i);
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setIngredients(List<IngredientAndAmount> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getAmounts() {
        return ingredients.stream().map(IngredientAndAmount::getAmount).collect(Collectors.toUnmodifiableList());
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public boolean contains(Ingredient ingredient) {
        return ingredients.contains(new IngredientAndAmount(ingredient, 0));
    }

    public Recipe getShallowCopy() { // ingredients are not copied
        Recipe recipe = new Recipe(
                name,
                ingredients.stream()
                        .map(IngredientAndAmount::getShallowCopy)
                        .collect(Collectors.toList()),
                directions,
                prepTime,
                portions);
        recipe.setID(getID());
        return recipe;
    }

    public void setTo(Recipe recipe) {
        if (!this.equals(recipe)) { // by ID
            throw new IllegalArgumentException(
                    "Cannot set (" + this + ") to (" + recipe + "): recipes of different IDs");
        }
        setName(recipe.getName());
        setIngredients(recipe.getIngredients());
        setDirections(recipe.getDirections());
        setPrepTime(recipe.getPrepTime());
        setPortions(recipe.getPortions());
    }

    public String getFormattedTime() {
        String time = "";
        if (prepTime >= 60) {
            time += (prepTime / 60) + " h";
        }
        if (prepTime % 60 != 0) {
            if (!time.isEmpty()) time += " ";
            time += (prepTime % 60) + " m";
        }
        return time;
    }

    public String getFormattedCalories() {
        return Double.valueOf(getRecipeCalories()).intValue() + " kJ";
    }

    public double getRecipeCalories() {
        double total = 0;
        for (IngredientAndAmount ingredient : ingredients) {
            total += ingredient.getIngredient().getCalories() * ingredient.getAmount();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Recipe:" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", directions='" + directions + '\'' +
                ", prepTime=" + prepTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(ID, recipe.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }
}
