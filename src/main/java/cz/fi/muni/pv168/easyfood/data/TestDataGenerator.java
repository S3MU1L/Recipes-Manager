package cz.fi.muni.pv168.easyfood.data;


import cz.fi.muni.pv168.easyfood.bussiness.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class TestDataGenerator {
    private static final List<Category> CATEGORY =
            List.of(new Category(UUID.randomUUID().toString(), "Soups", Color.BLUE),
                    new Category(UUID.randomUUID().toString(), "Vegetarian", Color.RED),
                    new Category(UUID.randomUUID().toString(), "Seafood", Color.GREEN),
                    new Category(UUID.randomUUID().toString(), "Sweet", Color.WHITE),
                    new Category(UUID.randomUUID().toString(), "Snack", Color.YELLOW),
                    new Category(UUID.randomUUID().toString(), "Dessert", Color.CYAN),
                    new Category(UUID.randomUUID().toString(), "Grilled", Color.GRAY),
                    new Category(UUID.randomUUID().toString(), "Vegan", Color.ORANGE),
                    new Category(UUID.randomUUID().toString(), "Salad", Color.DARK_GRAY),
                    new Category(UUID.randomUUID().toString(), "Brunch", Color.WHITE));
    private static final List<Unit> UNITS =
            List.of(new Unit(UUID.randomUUID().toString(), "Gram", "g", BaseUnit.GRAM, 1),
                    new Unit(UUID.randomUUID().toString(), "Milliliter", "ml", BaseUnit.MILLILITER, 1),
                    new Unit(UUID.randomUUID().toString(), "Piece", "pc", BaseUnit.PIECE, 1),
                    new Unit(UUID.randomUUID().toString(), "Cup", "cp", BaseUnit.MILLILITER, 200),
                    new Unit(UUID.randomUUID().toString(), "Spoon", "sp", BaseUnit.MILLILITER, 15),
                    new Unit(UUID.randomUUID().toString(), "Kilogram", "kg", BaseUnit.GRAM, 1000),
                    new Unit(UUID.randomUUID().toString(), "Dozen", "dz", BaseUnit.PIECE, 12),
                    new Unit(UUID.randomUUID().toString(), "Half", "1/2", BaseUnit.PIECE, 0.5));

    private static final List<String> INGREDIENT_NAMES =
            List.of("Water", "Meat", "Milk", "Egg", "Sugar", "Oil", "Salt", "Butter", "Bread", "Sausage");
    private static final List<String> RECIPE_NAMES =
            List.of("Hot dog", "Steak", "Scrambled eggs", "Sandwich", "Hamburger", "Schnitzel", "Tofu", "Ramen");
    private static final List<String> CONTAINERS =
            List.of("Large Bowl", "Medium Bowl", "Small Bowl", "Pan", "Pot", "Small Box");
    private static final List<String> ACTION = List.of("Mix", "Smash", "Stir", "Boil", "Fry", "Bake");

    private final Random random = new Random();

    public Unit createTestUnit() {
        return selectRandom(UNITS);
    }

    public List<Unit> createTestUnits(int count) {
        return UNITS.stream().limit(count).toList();
    }

    public Category createTestCategory() {
        return selectRandom(CATEGORY);
    }

    public List<Category> createTestCategories(int count) {
        return CATEGORY.stream().limit(count).toList();
    }

    public Ingredient createTestIngredient(String name, Unit unit) {
        return new Ingredient(UUID.randomUUID().toString(), name, (random.nextInt(1000) + 1) / 10.0, unit);
    }

    public IngredientWithAmount createTestIngredientWithAmount(Ingredient ingredient) {
        return new IngredientWithAmount(UUID.randomUUID().toString(), ingredient, random.nextInt(100) / 10.);
    }

    public List<Ingredient> createTestIngredients(int count, List<Unit> units) {
        return INGREDIENT_NAMES.stream().limit(count).map(s -> createTestIngredient(s, selectRandom(units))).toList();
    }

    private String createDescription(List<IngredientWithAmount> ingredientWithAmounts) {
        int size = ingredientWithAmounts.size();
        int count = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (count != size) {
            stringBuilder.append("In a ").append(selectRandom(CONTAINERS)).append(", ").append(selectRandom(ACTION))
                         .append(" together");
            int i = size - count == 2 ? 2 : random.nextInt(2, size - count);
            if (count + i == size - 1) {
                i++;
            }
            for (; i > 0; i--, count++) {
                IngredientWithAmount ingredient = ingredientWithAmounts.get(count);
                stringBuilder.append(i == 1 ? " and " : ", ").append(ingredient.getAmount()).append(" ").append(ingredient.getName());
            }
            stringBuilder.append(count == size ? ".\n" : "\n");
        }
        stringBuilder.append("And then we ").append(selectRandom(ACTION)).append(" everything together");
        return stringBuilder.toString();
    }

    public Recipe createTestRecipe(String name, List<Ingredient> ingredients, Category category) {
        List<IngredientWithAmount> ingredientsWithAmount = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            IngredientWithAmount ingredient = createTestIngredientWithAmount(selectRandom(ingredients));
            if (ingredientsWithAmount.stream().noneMatch(
                    ingredient1 -> ingredient1.getIngredient().equals(ingredient.getIngredient()))) {
                ingredientsWithAmount.add(ingredient);
            }
        }
        String description = createDescription(ingredientsWithAmount);
        return new Recipe(UUID.randomUUID().toString(), name, ingredientsWithAmount, description, random.nextInt(1, 20),
                          random.nextInt(5) + 1, category);
    }

    public List<Recipe> createTestRecipes(int count, List<Ingredient> ingredients, List<Category> categories) {
        return RECIPE_NAMES.stream().limit(count).map(s -> createTestRecipe(s, ingredients, selectRandom(categories)))
                           .toList();
    }

    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }
}