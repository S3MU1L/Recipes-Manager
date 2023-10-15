package cz.fi.muni.pv168.easyfood.data;


import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestDataGenerator {
    private static final List<Category> CATEGORY = List.of(
            new Category("Soups"),
            new Category("Vegetarian"),
            new Category("Seafood"),
            new Category("Sweet"),
            new Category("Snack"),
            new Category("Dessert"),
            new Category("Grilled"),
            new Category("Vegan"),
            new Category("Salad"),
            new Category("Brunch")
    );
    private static final List<Ingredient> INGREDIENTS = List.of(
            new Ingredient("Water", 1, Unit.MILLILITER),
            new Ingredient("Meat", 1, Unit.GRAM),
            new Ingredient("Milk", 1, Unit.MILLILITER),
            new Ingredient("Egg", 1, Unit.PIECE),
            new Ingredient("Sugar", 1, Unit.GRAM),
            new Ingredient("Oil", 1, Unit.MILLILITER),
            new Ingredient("Salt", 1, Unit.GRAM),
            new Ingredient("Butter", 1, Unit.GRAM),
            new Ingredient("Bread", 1, Unit.PIECE),
            new Ingredient("Sausage", 1, Unit.GRAM)
    );
    private static final List<String> RECIPE_NAMES = List.of("Hot dog", "Steak", "Scrambled eggs", "Sandwich",
            "Hamburger", "Schnitzel", "Tofu", "Ramen");
    private static final List<List<String>> RECIPE_INGREDIENTS = List.of(
            List.of()
    );

    private final Random random = new Random();

    public Category createTestCategory(){
        Category category = selectRandom(CATEGORY);
        return new Category(category.getName());
    }
    public List<Category> createTestCategories(int count){
        return Stream
                .generate(this::createTestCategory)
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
        List<IngredientWithAmount> ingredients = Stream.generate(this::createTestIngredientWithAmount).limit(5).collect(Collectors.toList());
        String description = "In a medium bowl, beat together egg whites, 1/4 cup butter and 1/4 teaspoon salt";
        return new Recipe(name, ingredients, description, random.nextInt(1, 20), random.nextInt(5) + 1, createTestCategory());
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
