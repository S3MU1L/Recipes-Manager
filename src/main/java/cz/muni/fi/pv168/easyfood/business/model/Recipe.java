package cz.muni.fi.pv168.easyfood.business.model;

import java.util.List;

public class Recipe extends Entity {
    private String name;
    private List<IngredientWithAmount> ingredients;
    private String description;
    private int preparationTime;
    private int portions;
    private Category category;

    public Recipe(
            String guid,
            String name,
            List<IngredientWithAmount> ingredients,
            String description,
            int preparationTime,
            int portions,
            Category category
    ) {
        super(guid);
        setName(name);
        setIngredients(ingredients);
        setDescription(description);
        setPreparationTime(preparationTime);
        setPortions(portions);
        setCategory(category);
    }

    public Recipe(
            String name,
            List<IngredientWithAmount> ingredients,
            String description,
            int preparationTime,
            int portions,
            Category category
    ) {
        setName(name);
        setIngredients(ingredients);
        setDescription(description);
        setPreparationTime(preparationTime);
        setPortions(portions);
        setCategory(category);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientWithAmount> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientWithAmount> ingredients) {
        this.ingredients = ingredients;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
