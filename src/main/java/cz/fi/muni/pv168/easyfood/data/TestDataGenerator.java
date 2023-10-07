package cz.fi.muni.pv168.dietaryAssistantApp.data;

import cz.fi.muni.pv168.dietaryAssistantApp.model.Ingredient;
import cz.fi.muni.pv168.dietaryAssistantApp.model.IngredientAndAmount;
import cz.fi.muni.pv168.dietaryAssistantApp.model.Recipe;
import cz.fi.muni.pv168.dietaryAssistantApp.model.Unit;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestDataGenerator {

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
    private static final List<String> RECIPE_NAMES = List.of("Hot dog", "Steak", "Scrambled eggs", "Sandwich");
    private static final List<List<String>> RECIPE_INGREDIENTS = List.of(
            List.of()
    );

    private final Random random = new Random(129867358486L);

    public Ingredient createTestIngredient() {
        Ingredient ingredient = selectRandom(INGREDIENTS);
        return new Ingredient(ingredient.getName(),
                (random.nextInt(1000) + 1) / 10.0, ingredient.getUnit());
    }

    public IngredientAndAmount createTestIngredientAndAmount() {
        return new IngredientAndAmount(createTestIngredient(), random.nextInt(100) / 10.);
    }

    public List<Ingredient> createTestIngredients(int count) {
        return Stream
                .generate(this::createTestIngredient)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Recipe createTestRecipe() {
        String name = selectRandom(RECIPE_NAMES);
        List<IngredientAndAmount> ingredients = Stream.generate(this::createTestIngredientAndAmount).limit(5).collect(Collectors.toList());
        String directions = "In a medium bowl, beat together egg whites, 1/4 cup butter and 1/4 teaspoon salt";
        return new Recipe(name, ingredients, directions, random.nextInt(20), random.nextInt(5) + 1);
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
