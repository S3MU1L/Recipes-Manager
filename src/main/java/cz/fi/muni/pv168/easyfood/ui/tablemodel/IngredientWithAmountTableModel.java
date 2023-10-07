package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import java.util.ArrayList;

import java.util.List;

public class IngredientWithAmountTableModel extends EntityTableModel<IngredientWithAmount> {
    private final List<IngredientWithAmount> ingredients;

    public IngredientWithAmountTableModel(List<IngredientWithAmount> ingredients) {
        super(List.of(
                Column.readOnly("Name", String.class, IngredientWithAmount::getName),
                Column.readOnly("Calories", String.class, IngredientWithAmount::getFormattedCalories),
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

    public void updateRow(Ingredient ingredient) {
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
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