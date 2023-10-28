package cz.fi.muni.pv168.easyfood.data;


import cz.fi.muni.pv168.easyfood.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestDataGenerator {
    private static final List<Category> CATEGORY = List.of(
            new Category("Soups", Color.WHITE),
            new Category("Vegetarian", Color.WHITE),
            new Category("Seafood", Color.WHITE),
            new Category("Sweet", Color.WHITE),
            new Category("Snack", Color.WHITE),
            new Category("Dessert", Color.WHITE),
            new Category("Grilled", Color.WHITE),
            new Category("Vegan", Color.WHITE),
            new Category("Salad", Color.WHITE),
            new Category("Brunch", Color.WHITE)
    );
    private static final List<Unit> UNITS = List.of(
            new Unit("Gram", "g",  BaseUnit.GRAM, 1),
            new Unit("Milliliter", "ml", BaseUnit.MILLILITER, 1),
            new Unit("Piece", "pc", BaseUnit.PIECE, 1)
    );
    private static final List<Ingredient> INGREDIENTS = List.of(
            new Ingredient("Water", 1, UNITS.get(1)),
            new Ingredient("Meat", 1, UNITS.get(0)),
            new Ingredient("Milk", 1, UNITS.get(1)),
            new Ingredient("Egg", 1, UNITS.get(2)),
            new Ingredient("Sugar", 1, UNITS.get(0)),
            new Ingredient("Oil", 1, UNITS.get(1)),
            new Ingredient("Salt", 1, UNITS.get(0)),
            new Ingredient("Butter", 1, UNITS.get(0)),
            new Ingredient("Bread", 1, UNITS.get(2)),
            new Ingredient("Sausage", 1, UNITS.get(0))
    );
    private static final List<String> RECIPE_NAMES = List.of("Hot dog", "Steak", "Scrambled eggs", "Sandwich",
            "Hamburger", "Schnitzel", "Tofu", "Ramen");
    private static final List<List<String>> RECIPE_INGREDIENTS = List.of(
            List.of()
    );

    private final Random random = new Random();

    public Unit createTestUnit() {
        return selectRandom(UNITS);
    }

    public List<Unit> createTestUnits(int count) {
        List<Unit> units = new ArrayList<>();
        while (units.size() != count){
            units.addAll(UNITS.stream()
                    .limit(count - units.size())
                    .toList());
        }
        return units;
    }

    public Category createTestCategory() {
        return selectRandom(CATEGORY);
    }

    public List<Category> createTestCategories(int count) {
        return CATEGORY.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Ingredient createTestIngredient() {
        Ingredient ingredient = selectRandom(INGREDIENTS);
        return new Ingredient(ingredient.getName(),
                (random.nextInt(1000) + 1) / 10.0, ingredient.getUnit());
    }

    public IngredientWithAmount createTestIngredientWithAmount() {
        return new IngredientWithAmount(createTestIngredient(), random.nextInt(100) / 10.);
    }

    public List<Ingredient> createTestIngredients(int count) {
        return Stream
                .generate(this::createTestIngredient)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Recipe createTestRecipe() {
        String name = selectRandom(RECIPE_NAMES);
        List<IngredientWithAmount> ingredients =
                Stream.generate(this::createTestIngredientWithAmount).limit(5).collect(Collectors.toList());
        String description = "In a medium bowl, beat together egg whites, 1/4 cup butter and 1/4 teaspoon salt";
        return new Recipe(name, ingredients, description, random.nextInt(1, 20),
                random.nextInt(5) + 1, createTestCategory());
    }

    public List<Recipe> createTestRecipes(int count) {
        return Stream
                .generate(this::createTestRecipe)
                .limit(count)
                .collect(Collectors.toList());
    }

    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }
}
