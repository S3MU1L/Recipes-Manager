package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;

import java.util.List;

public class IngredientWithAmountTableModel extends AbstractTableModel {
    private final List<IngredientWithAmount> ingredients;
    private final List<Column<?, IngredientWithAmount>> columns = List.of(
            Column.readOnly("Name", String.class, IngredientWithAmount::getName),
            Column.readOnly("Calories", String.class, IngredientWithAmount::getFormattedCalories),
            Column.readOnly("Amount", String.class, IngredientWithAmount::getFormattedAmount)
    );

    public IngredientWithAmountTableModel(List<IngredientWithAmount> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
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
        return columns.get(columnIndex).getValue(getEntity(rowIndex));
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    public void deleteRow(int rowIndex) {
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(IngredientWithAmount ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient ingredient) {
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public IngredientWithAmount getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }
}
