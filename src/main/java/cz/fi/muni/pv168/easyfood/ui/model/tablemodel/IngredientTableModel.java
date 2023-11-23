package cz.fi.muni.pv168.easyfood.ui.model.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class IngredientTableModel extends EntityTableModel<Ingredient> {
    private final List<Ingredient> ingredients;
    private final List<Recipe> recipes;

    public IngredientTableModel(List<Ingredient> ingredients, List<Recipe> recipes) {
        super(List.of(Column.readOnly("Name", String.class, Ingredient::getName), Column.readOnly("Calories", String.class, Ingredient::getFormattedCalories)));
        this.ingredients = ingredients;
        this.recipes = recipes;
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    public void addRow(Ingredient ingredient) {
        if (!ingredients.stream().filter(ingredient1 -> ingredient1.getName().equals(ingredient.getName())).toList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + ingredient.getName(),
                    "Error", ERROR_MESSAGE, null);
            return;
        }

        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient oldIngredient, Ingredient newIngredient) {
        if (!ingredients.stream().filter(ingredient -> ingredient != oldIngredient &&
                ingredient.getName().equals(newIngredient.getName())).toList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + newIngredient.getName(),
                    "Error", ERROR_MESSAGE, null);
            return;
        }

        int rowIndex = ingredients.indexOf(oldIngredient);
        ingredients.set(rowIndex, newIngredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {

    }

    @Override
    public void customizeTable(JTable table) {

    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }

    @Override
    protected void updateEntity(Ingredient entity) {

    }

    @Override
    public void deleteRow(int rowIndex) {
        Ingredient removedIngredient = ingredients.get(rowIndex);
        List<Recipe> usedIn = new ArrayList<>();
        recipes.forEach(recipe -> {
            if (recipe.getIngredients().stream().map(IngredientWithAmount::getIngredient).filter(ingredient -> ingredient.equals(removedIngredient)).toList().size() >
                    0) {
                usedIn.add(recipe);
            }
        });

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Ingredient : ").append(removedIngredient.getName()).append("\nIt is used in Recipes: ");
            for (Recipe recipe : usedIn){
                stringBuilder.append(" ").append(recipe.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }
        ingredients.remove(rowIndex);

        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
