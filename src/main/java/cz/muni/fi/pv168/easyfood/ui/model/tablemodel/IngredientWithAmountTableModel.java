package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.Column;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class IngredientWithAmountTableModel extends AbstractTableModel implements EntityTableModel<IngredientWithAmount> {
    private final List<IngredientWithAmount> ingredients;
    private final CrudService<IngredientWithAmount> ingredientCrudService;

    private final List<Column<IngredientWithAmount, ?>> columns = List.of(
            Column.readonly("Name", String.class, IngredientWithAmount::getName),
            Column.readonly("Amount", String.class, IngredientWithAmount::getFormattedAmount)
    );

    public IngredientWithAmountTableModel(
            List<IngredientWithAmount> ingredients,
            CrudService<IngredientWithAmount> ingredientCrudService
    ) {
        this.ingredients = new ArrayList<>(ingredients);
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

    @Override
    public void updateRow(IngredientWithAmount ingredient) {
        ingredientCrudService.update(ingredient)
                .intoException();
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        var toDelete = ingredients.get(rowIndex);
        ingredientCrudService.deleteByGuid(toDelete.getGuid());
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
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

}
