package cz.fi.muni.pv168.easyfood.ui.model.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.ui.model.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class RecipeTableModel extends AbstractTableModel implements EntityTableModel<Recipe> {
    private final List<Recipe> recipes;
    private final CrudService<Recipe> recipeCrudService;

    private final List<Column<Recipe, ?>> columns = List.of(
            Column.readonly("Name", String.class, Recipe::getName),
            Column.readonly("Calories", String.class, Recipe::getFormattedCalories),
            Column.readonly("Preparation time", String.class, Recipe::getFormattedPreparationTime)
    );

    public RecipeTableModel(CrudService<Recipe> recipeCrudService) {
        this.recipeCrudService = recipeCrudService;
        this.recipes = new ArrayList<>(recipeCrudService.findAll());
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(recipe);
    }

    public void addRow(Recipe recipe) {
        if (recipes.stream().filter(recipe1 -> recipe1.getName().equals(recipe.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + recipe.getName(), "Error", ERROR_MESSAGE, null);
            return;
        }

        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);

    }

    public void updateRow(Recipe oldRecipe, Recipe newRecipe) {
        if (recipes.stream().filter(recipe -> recipe != oldRecipe && recipe.getName().equals(newRecipe.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + newRecipe.getName(), "Error", ERROR_MESSAGE, null);
            return;
        }

        int rowIndex = recipes.indexOf(oldRecipe);
        recipes.set(rowIndex, newRecipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void customizeTableCell(Component cell, Object value, int row, JTable table) {

    }

    public void customizeTable(JTable table) {

    }

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }

    protected void updateEntity(Recipe entity) {

    }

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
