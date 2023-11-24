package cz.fi.muni.pv168.easyfood.ui.model.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.ui.model.Column;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class IngredientTableModel extends AbstractTableModel implements EntityTableModel<Ingredient> {
    private final List<Ingredient> ingredients;
    private final List<Recipe> recipes;

    private final CrudService<Ingredient> ingredientCrudService;

    private final List<Column<Ingredient, ?>> columns =
            List.of(Column.readonly("Name", String.class, Ingredient::getName),
                    Column.readonly("Calories", String.class, Ingredient::getFormattedCalories));

    public IngredientTableModel(CrudService<Ingredient> ingredientCrudService, List<Recipe> recipes, List<Ingredient> ingredients) {
        this.ingredientCrudService = ingredientCrudService;
        this.ingredients = ingredients;
        this.recipes = recipes;
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
        ingredientCrudService.create(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient oldIngredient, Ingredient newIngredient) {
        ingredientCrudService.deleteByGuid(oldIngredient.getGuid());
        ingredientCrudService.create(newIngredient);
        int rowIndex = ingredients.indexOf(oldIngredient);
        ingredients.set(rowIndex, newIngredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }

    public void deleteRow(int rowIndex) {
        Ingredient removedIngredient = ingredients.get(rowIndex);
        List<Recipe> usedIn = new ArrayList<>();
        recipes.forEach(recipe -> {
            if (recipe.getIngredients().stream().map(IngredientWithAmount::getIngredient)
                      .filter(ingredient -> ingredient.equals(removedIngredient)).toList().size() > 0) {
                usedIn.add(recipe);
            }
        });

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Ingredient : ").append(removedIngredient.getName())
                         .append("\nIt is used in Recipes: ");
            for (Recipe recipe : usedIn) {
                stringBuilder.append(" ").append(recipe.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }

        Ingredient ingredient = ingredients.get(rowIndex);
        ingredientCrudService.deleteByGuid(ingredient.getGuid());
        ingredients.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
