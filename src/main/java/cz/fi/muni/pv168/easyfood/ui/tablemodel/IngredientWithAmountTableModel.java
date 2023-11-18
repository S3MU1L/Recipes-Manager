package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JTable;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class IngredientWithAmountTableModel extends EntityTableModel<IngredientWithAmount> {
    private final List<IngredientWithAmount> ingredients;

    public IngredientWithAmountTableModel(List<IngredientWithAmount> ingredients) {
        super(List.of(
                Column.readOnly("Name", String.class, IngredientWithAmount::getName),
                Column.readOnly("Amount", String.class, IngredientWithAmount::getFormattedAmount)
        ));
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    public void addRow(IngredientWithAmount ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(IngredientWithAmount oldIngredient, IngredientWithAmount newIngredient) {
        int rowIndex = ingredients.indexOf(oldIngredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
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
    protected void updateEntity(IngredientWithAmount entity) {

    }

    @Override
    public void deleteRow(int rowIndex) {
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
