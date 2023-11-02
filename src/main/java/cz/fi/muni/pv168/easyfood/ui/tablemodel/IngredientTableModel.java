package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientTableModel extends EntityTableModel<Ingredient> {
    private final List<Ingredient> ingredients;

    public IngredientTableModel(List<Ingredient> ingredients) {
        super(List.of(
                Column.readOnly("Name", String.class, Ingredient::getName),
                Column.readOnly("Calories", String.class, Ingredient::getFormattedCalories)
        ));
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    public void addRow(Ingredient ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient ingredient) {
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, int row) {

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
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
