package cz.muni.fi.pv168.easyfood.business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Filter {
    private String name;
    private Set<Category> categories;
    private Set<Ingredient> ingredients;
    private boolean ingredientsPartialMatch;
    private int preparationTime;
    private int minimumNutritionalValue;
    private int maximumNutritionalValue;
    private int minPortion;
    private int maxPortion;

    public static Filter createEmptyFilter() {
        return new Filter();
    }

    public List<Recipe> getFilteredRecipes(Collection<Recipe> recipes) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (name != null && !name.isEmpty() && !name.equals(recipe.getName())) {
                continue;
            }
            if (preparationTime != 0 && recipe.getPreparationTime() > preparationTime) {
                continue;
            }
            double nutritionalValue = recipe.getCalories();
            if (nutritionalValue < minimumNutritionalValue ||
                    (maximumNutritionalValue != 0 && nutritionalValue > maximumNutritionalValue)) {
                continue;
            }
            int recipePortion = recipe.getPortions();
            if ((recipePortion < minPortion && minPortion != 0 && minPortion != 6) ||
                    (maxPortion != 0 && recipePortion > maxPortion && maxPortion != 6) ||
                    (minPortion == 6 && recipePortion < 5)){
                continue;
            }
            if (!categories.isEmpty() && !categories.contains(recipe.getCategory())) {
                continue;
            }
            if (!ingredients.isEmpty()){
                if (ingredientsPartialMatch) {
                    if (recipe.getIngredients()
                            .stream()
                            .map(IngredientWithAmount::getIngredient)
                            .noneMatch(ingredients::contains)) {
                        continue;
                    }
                }else{
                    if (!ingredients.containsAll(
                            recipe.getIngredients()
                                    .stream()
                                    .map(IngredientWithAmount::getIngredient)
                                    .collect(Collectors.toSet())
                    )) {
                        continue;
                    }
                }
            }
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

    public void setIngredientsPartialMatch(boolean ingredientsPartialMatch) {
        this.ingredientsPartialMatch = ingredientsPartialMatch;
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

    public void setMinPortion(int minPortion) {
        this.minPortion = minPortion;
    }

    public void setMaxPortion(int maxPortion) {
        this.maxPortion = maxPortion;
    }

    public int getMinPortion() {
        return minPortion;
    }

    public int getMaxPortion() {
        return maxPortion;
    }
}
