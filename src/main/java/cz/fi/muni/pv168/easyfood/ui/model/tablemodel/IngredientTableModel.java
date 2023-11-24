package cz.fi.muni.pv168.easyfood.ui.model.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
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

public class IngredientTableModel extends AbstractTableModel implements EntityTableModel<Ingredient> {
    private final List<Ingredient> ingredients;
    private final List<Recipe> recipes;

    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Recipe> recipeCrudService;

    private final List<Column<Ingredient, ?>> columns = List.of(
            Column.readonly("Name", String.class, Ingredient::getName),
            Column.readonly("Calories", String.class, Ingredient::getFormattedCalories)
    );

    public IngredientTableModel(CrudService<Ingredient> ingredientCrudService, CrudService<Recipe> recipeCrudService) {
        this.ingredientCrudService = ingredientCrudService;
        this.recipeCrudService = recipeCrudService;
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
        this.recipes = new ArrayList<>(recipeCrudService.findAll());
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
        var employee = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(employee);
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

    public void addRow(Ingredient ingredient) {
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient oldIngredient, Ingredient newIngredient) {
        int rowIndex = ingredients.indexOf(oldIngredient);
        ingredients.set(rowIndex, newIngredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }


    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {

    }

    @Override
    public void customizeTable(JTable table) {

    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }

    protected void updateEntity(Ingredient entity) {

    }

    public void deleteRow(int rowIndex) {
        Ingredient removedIngredient = ingredients.get(rowIndex);
        List<Recipe> usedIn = new ArrayList<>();
        recipes.forEach(recipe -> {
            if (recipe.getIngredients().stream().map(IngredientWithAmount::getIngredient).filter(ingredient -> ingredient.equals(removedIngredient)).toList().size() >
                    0) {
                usedIn.add(recipe);
            }
        });

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Ingredient : ").append(removedIngredient.getName()).append("\nIt is used in Recipes: ");
            for (Recipe recipe : usedIn) {
                stringBuilder.append(" ").append(recipe.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }
        ingredients.remove(rowIndex);

        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
