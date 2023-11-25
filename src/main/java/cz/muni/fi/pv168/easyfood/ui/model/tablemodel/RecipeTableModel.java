package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.Column;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

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

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }

    protected void updateEntity(Recipe entity) {
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(recipe);
    }

    public void addRow(Recipe recipe) {
        recipeCrudService.create(recipe)
                .intoException();
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Recipe oldRecipe, Recipe newRecipe) {
        recipeCrudService.update(newRecipe);
        int rowIndex = recipes.indexOf(oldRecipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }


    public void deleteRow(int rowIndex) {
        var toDelete = getEntity(rowIndex);
        recipeCrudService.deleteByGuid(toDelete.getGuid());
        recipes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void clear() {
        if (recipes.isEmpty()) {
            return;
        }
        int last_index = recipes.size();
        recipes.clear();
        recipeCrudService.deleteAll();
        fireTableRowsDeleted(0, last_index - 1);
    }

    public void customizeTableCell(Component cell, Object value, int row, JTable table) {
    }

    public void customizeTable(JTable table) {
    }
}
