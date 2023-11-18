package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.List;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class RecipeTableModel extends EntityTableModel<Recipe> {
    private final List<Recipe> recipes;

    public RecipeTableModel(List<Recipe> recipes) {
        super(List.of(
                Column.readOnly("Name", String.class, Recipe::getName),
                Column.readOnly("Calories", String.class, Recipe::getFormattedCalories),
                Column.readOnly("Preparation time", String.class, Recipe::getFormattedPreparationTime)
        ));
        this.recipes = recipes;
    }

    @Override
    public int getRowCount() {
        return recipes.size();
    }

    public void addRow(Recipe recipe) {
        if (recipes.stream().filter(recipe1 -> recipe1.getName().equals(recipe.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + recipe.getName(), "Error", INFORMATION_MESSAGE, null);
            return;
        }

        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);

    }

    public void updateRow(Recipe oldRecipe, Recipe newRecipe) {
        if (recipes.stream().filter(recipe -> recipe != oldRecipe && recipe.getName().equals(newRecipe.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + newRecipe.getName(), "Error", INFORMATION_MESSAGE, null);
            return;
        }

        int rowIndex = recipes.indexOf(oldRecipe);
        recipes.set(rowIndex, newRecipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, int row) {

    }

    @Override
    public void customizeTable(JTable table) {

    }

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }

    @Override
    protected void updateEntity(Recipe entity) {

    }

    @Override
    public void deleteRow(int rowIndex) {
        recipes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void clear() {
        if (recipes.isEmpty()) {
            return;
        }
        int last_index = recipes.size();
        recipes.clear();
        fireTableRowsDeleted(0, last_index - 1);
    }
}
