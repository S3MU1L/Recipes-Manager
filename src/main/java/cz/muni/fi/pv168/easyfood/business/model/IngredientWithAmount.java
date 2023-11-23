package cz.muni.fi.pv168.easyfood.business.model;

public class IngredientWithAmount extends Entity {
    private Ingredient ingredient;
    private double amount;

    public IngredientWithAmount(String guid, Ingredient ingredient, double amount) {
        super(guid);
        setIngredient(ingredient);
        setAmount(amount);
    }

    public IngredientWithAmount(Ingredient ingredient, double amount) {
        setIngredient(ingredient);
        setAmount(amount);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "IngredientWithAmount{" +
                "ingredient=" + ingredient +
                ", amount=" + amount +
                '}';
    }
}



