package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.Filter;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.Column;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class IngredientTableModel extends AbstractTableModel implements EntityTableModel<Ingredient> {
    private List<Ingredient> ingredients;
    private final RecipeTableModel recipeTableModel;
    private final CrudService<Ingredient> ingredientCrudService;

    private final List<Column<Ingredient, ?>> columns =
            List.of(Column.readonly("Name", String.class, Ingredient::getName),
                    Column.readonly("Calories", String.class, Ingredient::getFormattedCalories));

    public IngredientTableModel(CrudService<Ingredient> ingredientCrudService, RecipeTableModel recipeTableModel,
                                List<Ingredient> ingredients) {
        this.ingredientCrudService = ingredientCrudService;
        this.ingredients = ingredients;
        this.recipeTableModel = recipeTableModel;
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
        ingredientCrudService.create(ingredient)
                             .intoException();
        int newRowIndex = ingredients.size();
        ingredients.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Ingredient ingredient) {
        ingredientCrudService.update(ingredient)
                             .intoException();
        int rowIndex = ingredients.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void updateAll() {
        ingredients = new ArrayList<>(ingredientCrudService.findAll());
    }

    public void updateIngredients() {
        ingredients = ingredientCrudService.findAll();

        fireTableDataChanged();
    }

    public void deleteRows(int[] rowIndexes) {
        Filter filter =recipeTableModel.getActiveFiter();
        recipeTableModel.reset();
        List<Ingredient> toDelete =
                Arrays.stream(rowIndexes).sequential().mapToObj(rowIndex -> ingredients.get(rowIndex)).toList();
        StringBuilder stringBuilder = new StringBuilder();

        Map<Ingredient, List<Recipe>> usedIn = new HashMap<>();
        for (Recipe recipe : recipeTableModel.getEntity()) {
            List<IngredientWithAmount> ingredients = recipe.getIngredients();
            for (Ingredient ingredient : ingredients.stream().map(IngredientWithAmount::getIngredient).toList()) {
                if (toDelete.contains(ingredient)) {
                    usedIn.computeIfAbsent(ingredient, k -> new ArrayList<>());
                    usedIn.get(ingredient).add(recipe);
                }
            }

        }

        if (!usedIn.isEmpty()) {
            for (var entry : usedIn.entrySet()) {
                stringBuilder.append("Unable to delete Ingredient : ").append(entry.getKey().getName())
                             .append("\nIt is used in Recipes:");
                for (Recipe recipe : entry.getValue()) {
                    stringBuilder.append(" ").append(recipe.getName()).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append("\n\n");
            }
        }

        if (!stringBuilder.isEmpty()) {
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            recipeTableModel.updateWithFilter(filter);
            return;
        }

        for (Ingredient ingredient : toDelete) {
            ingredientCrudService.deleteByGuid(ingredient.getGuid());
        }
        ingredients.removeAll(toDelete);
        fireTableRowsDeleted(rowIndexes[0], rowIndexes[rowIndexes.length - 1]);
        recipeTableModel.updateWithFilter(filter);
    }

    public Ingredient getEntity(int rowIndex) {
        return ingredients.get(rowIndex);
    }

    @Override
    public List<Ingredient> getEntity() {
        return ingredients;
    }

}
