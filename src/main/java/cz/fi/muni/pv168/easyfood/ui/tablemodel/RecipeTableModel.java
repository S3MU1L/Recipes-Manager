package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JTable;
import java.awt.Component;
import java.util.List;

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
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);

    }

    public void updateRow(Recipe recipe) {
        int rowIndex = recipes.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {

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
