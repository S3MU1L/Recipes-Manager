package cz.muni.fi.pv168.easyfood.data;


import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestDataGenerator {
    private static final List<Category> CATEGORY = List.of(
            new Category(UUID.randomUUID().toString(), "Soups", Color.BLUE),
            new Category(UUID.randomUUID().toString(), "Vegetarian", Color.RED),
            new Category(UUID.randomUUID().toString(), "Seafood", Color.GREEN),
            new Category(UUID.randomUUID().toString(), "Sweet", Color.WHITE),
            new Category(UUID.randomUUID().toString(), "Snack", Color.YELLOW),
            new Category(UUID.randomUUID().toString(), "Dessert", Color.CYAN),
            new Category(UUID.randomUUID().toString(), "Grilled", Color.GRAY),
            new Category(UUID.randomUUID().toString(), "Vegan", Color.ORANGE),
            new Category(UUID.randomUUID().toString(), "Salad", Color.DARK_GRAY),
            new Category(UUID.randomUUID().toString(), "Brunch", Color.WHITE)
    );
    private static final List<Unit> UNITS = List.of(
            new Unit(UUID.randomUUID().toString(), "Gram", "g", BaseUnit.GRAM, 1),
            new Unit(UUID.randomUUID().toString(), "Milliliter", "ml", BaseUnit.MILLILITER, 1),
            new Unit(UUID.randomUUID().toString(), "Piece", "pc", BaseUnit.PIECE, 1)
    );

    private static final List<String> INGREDIENT_NAMES = List.of("Water", "Meat", "Milk", "Egg", "Sugar", "Oil", "Salt", "Butter", "Bread", "Sausage");
    private static final List<String> RECIPE_NAMES = List.of("Hot dog", "Steak", "Scrambled eggs", "Sandwich",
            "Hamburger", "Schnitzel", "Tofu", "Ramen");

    private final Random random = new Random();

    public Unit createTestUnit() {
        return selectRandom(UNITS);
    }

    public List<Unit> createTestUnits(int count) {
        List<Unit> units = new ArrayList<>();
        while (units.size() != count) {
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

    public Ingredient createTestIngredient(Unit unit) {
        String ingredient = selectRandom(INGREDIENT_NAMES);
        return new Ingredient(UUID.randomUUID().toString(), ingredient,
                (random.nextInt(1000) + 1) / 10.0, unit);
    }

    public IngredientWithAmount createTestIngredientWithAmount(Ingredient ingredient) {
        return new IngredientWithAmount(UUID.randomUUID().toString(), ingredient, random.nextInt(100) / 10.);
    }

    public List<Ingredient> createTestIngredients(int count, List<Unit> units) {
        return Stream
                .generate(() -> createTestIngredient(selectRandom(units)))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Recipe createTestRecipe(List<Ingredient> ingredients, Category category) {
        String name = selectRandom(RECIPE_NAMES);
        List<IngredientWithAmount> ingredientsWithAmount = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            IngredientWithAmount ingredient = createTestIngredientWithAmount(selectRandom(ingredients));
//            if (ingredientsWithAmount.stream().noneMatch(ingredient1 -> ingredient1.getIngredient().equals(ingredient.getIngredient()))) {
//                ingredientsWithAmount.add(ingredient);
//            }
//        }
        String description = "In a medium bowl, beat together egg whites, 1/4 cup butter and 1/4 teaspoon salt";
        return new Recipe(UUID.randomUUID().toString(), name, ingredientsWithAmount, description, random.nextInt(1, 20),
                random.nextInt(5) + 1, category);
    }

    public List<Recipe> createTestRecipes(int count, List<Ingredient> ingredients, List<Category> categories) {
        return Stream
                .generate(() -> createTestRecipe(ingredients, selectRandom(categories)))
                .limit(count)
                .collect(Collectors.toList());
    }

    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }
}