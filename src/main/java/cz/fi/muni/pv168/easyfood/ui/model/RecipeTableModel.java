package cz.fi.muni.pv168.easyfood.ui.model;


import cz.fi.muni.pv168.easyfood.model.Recipe;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel {

    private final List<Recipe> recipes;

    private final List<Column<Recipe, ?>> columns = List.of(
            Column.readonly("Name", String.class, Recipe::getName),
            Column.readonly("Calories", String.class, Recipe::getFormattedCalories),
            Column.readonly("Preparation time", String.class, Recipe::getFormattedPreparationTime)
    );

    public RecipeTableModel(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }

    @Override
    public int getRowCount() {
        return recipes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(recipe);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    public void deleteRow(int rowIndex) {
        recipes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
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

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }
}
