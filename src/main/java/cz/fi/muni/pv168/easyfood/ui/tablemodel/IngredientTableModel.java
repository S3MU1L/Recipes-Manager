package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.List;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class IngredientTableModel extends EntityTableModel<Ingredient> {
    private final List<Ingredient> ingredients;

    public IngredientTableModel(List<Ingredient> ingredients) {
        super(List.of(
                Column.readOnly("Name", String.class, Ingredient::getName),
                Column.readOnly("Calories", String.class, Ingredient::getFormattedCalories)
        ));
        this.ingredients = ingredients;
    }

    @Override
    public int getRowCount() {
        return ingredients.size();
    }

    public void addRow(Ingredient ingredient) {
        if (ingredients.stream().filter(ingredient1 -> ingredient1.getName().equals(ingredient.getName())).toList().size() !=
                0) {
            JOptionPane.showMessageDialog(null, "Unable to add Row -> Name <" + ingredient.getName() +
                    "> duplicity", "Error", INFORMATION_MESSAGE, null);
            return;
        }

        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient oldIngredient, Ingredient newIngredient) {
        if (ingredients.stream().filter(ingredient -> ingredient != oldIngredient &&
                ingredient.getName().equals(newIngredient.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Unable to edit Row -> Name <" + newIngredient.getName() +
                    "> duplicity", "Error", INFORMATION_MESSAGE, null);
            return;
        }

        int rowIndex = ingredients.indexOf(oldIngredient);
        ingredients.set(rowIndex, newIngredient);
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
