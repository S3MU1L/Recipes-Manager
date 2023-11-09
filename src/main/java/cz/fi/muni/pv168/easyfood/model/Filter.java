package cz.fi.muni.pv168.easyfood.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Filter {
    private String name;
    private Set<Category> categories;
    private Set<Ingredient> ingredients;
    private int preparationTime;
    private int minimumNutritionalValue;
    private int maximumNutritionalValue;
    private int portions;

    public static Filter createEmptyFilter() {
        return new Filter();
    }

    public List<Recipe> getFilteredRecipes(Collection<Recipe> recipes) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (name != null && !name.isEmpty() && !name.equals(recipe.getName())) continue;
            if (preparationTime != 0 && recipe.getPreparationTime() > preparationTime) continue;
            double nutritionalValue = recipe.getCalories();
            if (nutritionalValue < minimumNutritionalValue || (maximumNutritionalValue != 0 && nutritionalValue > maximumNutritionalValue)) continue;
            if ((portions == 6 && recipe.getPortions() <= 5) || portions != recipe.getPortions()) continue;
//            if (!categories.contains(recipe.getCategory())) continue;
//            if (!ingredients.containsAll(
//                    recipe.getIngredients()
//                            .stream()
//                            .map(IngredientWithAmount::getIngredient)
//                            .collect(Collectors.toSet())
//                    )
//            ) continue;
            filteredRecipes.add(recipe);
        }
        return filteredRecipes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getMinimumNutritionalValue() {
        return minimumNutritionalValue;
    }

    public void setMinimumNutritionalValue(int minimumNutritionalValue) {
        this.minimumNutritionalValue = minimumNutritionalValue;
    }

    public int getMaximumNutritionalValue() {
        return maximumNutritionalValue;
    }

    public void setMaximumNutritionalValue(int maximumNutritionalValue) {
        this.maximumNutritionalValue = maximumNutritionalValue;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }
}
