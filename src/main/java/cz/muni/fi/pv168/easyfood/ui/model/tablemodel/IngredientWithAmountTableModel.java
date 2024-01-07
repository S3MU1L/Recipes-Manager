package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.Column;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class IngredientWithAmountTableModel extends AbstractTableModel
        implements EntityTableModel<IngredientWithAmount> {
    private final List<IngredientWithAmount> ingredients;
    private final CrudService<IngredientWithAmount> ingredientCrudService;

    private final DependencyProvider dependencyProvider;

    private final List<Column<IngredientWithAmount, ?>> columns = List.of(
            Column.readonly("Name", String.class, IngredientWithAmount::getName),
            Column.readonly("Amount", String.class, IngredientWithAmount::getFormattedAmount)
    );

    public IngredientWithAmountTableModel(
            List<IngredientWithAmount> ingredients,
            CrudService<IngredientWithAmount> ingredientCrudService,
            DependencyProvider dependencyProvider
    ) {
        this.ingredients = new ArrayList<>(ingredients);
        this.dependencyProvider = dependencyProvider;
        this.ingredientCrudService = ingredientCrudService;
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var ingredient = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(ingredient);
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


    public void addRow(IngredientWithAmount ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void addRow(IngredientWithAmount ingredient, Recipe recipe) {
        addRow(ingredient);
        var recipeEntity = dependencyProvider.getRecipeDao().findByGuid(recipe.getGuid());
        if (!recipeEntity.isPresent()) {
            return;
        }

        var ingredientEntity = dependencyProvider.getIngredientDao().findByGuid(ingredient.getIngredient().getGuid());
        dependencyProvider.getIngredientWithAmountDao()
                          .addRecipeIngredient(ingredient, recipeEntity.get().id(), ingredientEntity.get().id());
    }

    @Override
    public void updateRow(IngredientWithAmount ingredient) {
        ingredientCrudService.update(ingredient)
                             .intoException();
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void deleteRows(int[] rowIndexes) {
        for (int rowIndex : rowIndexes) {
            deleteRow(rowIndex);
        }
    }

    public void deleteRow(int rowIndex) {
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex, Recipe recipe) {
        var recipeEntity = dependencyProvider.getRecipeDao().findByGuid(recipe.getGuid());
        if (!recipeEntity.isPresent()) {
            return;
        }

        var toDelete = ingredients.get(rowIndex);
        ingredientCrudService.deleteByGuid(toDelete.getGuid());
        deleteRow(rowIndex);
    }

    @Override
    public void updateAll() {

    }


    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {
    }

    @Override
    public void customizeTable(JTable table) {
    }

    public IngredientWithAmount getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }

    @Override
    public List<IngredientWithAmount> getEntity() {
        return ingredients;
    }

}
