package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.Filter;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.model.Column;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class RecipeTableModel extends AbstractTableModel implements EntityTableModel<Recipe> {
    private final MainWindow mainWindow;
    private List<Recipe> recipes;
    private final CrudService<Recipe> recipeCrudService;
    private final DependencyProvider dependencyProvider;
    private boolean activeFiter;

    private final List<Column<Recipe, ?>> columns = List.of(
            Column.readonly("Name", String.class, Recipe::getName),
            Column.readonly("Calories", String.class, Recipe::getFormattedCalories),
            Column.readonly("Preparation time", String.class, Recipe::getFormattedPreparationTime),
            Column.readonly("Category", String.class, Recipe::getFormattedCategory)
    );

    public RecipeTableModel(CrudService<Recipe> recipeCrudService, DependencyProvider dependencyProvider, List<Recipe> recipes, MainWindow mainWindow) {
        this.recipeCrudService = recipeCrudService;
        this.recipes = recipes;
        this.dependencyProvider = dependencyProvider;
        this.mainWindow = mainWindow;
        activeFiter = false;
    }

    @Override
    public int getRowCount() {
        return recipes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
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

    public Recipe getEntity(int rowIndex) {
        return recipes.get(rowIndex);
    }

    public List<Recipe> getEntity() {
        return recipes;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var recipe = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(recipe);
    }

    public void addRow(Recipe recipe) {
        recipeCrudService.create(recipe)
                .intoException();
        addIngredients(recipe);
        int newRowIndex = recipes.size();
        recipes.add(recipe);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    private void addIngredients(Recipe recipe) {
        var recipeEntity = dependencyProvider.getRecipeDao().findByGuid(recipe.getGuid());
        for (var ingredientAmount : recipe.getIngredients()) {
            var ingredientEntity = dependencyProvider.getIngredientDao().findByGuid(ingredientAmount.getIngredient().getGuid());
            dependencyProvider.getIngredientWithAmountDao().addRecipeIngredient(ingredientAmount, recipeEntity.get().id(), ingredientEntity.get().id());
        }
    }

    public void updateRow(Recipe recipe) {
        recipeCrudService.update(recipe);
        int rowIndex = recipes.indexOf(recipe);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void updateAll() {
        recipes = new ArrayList<>(recipeCrudService.findAll());
    }

    public void updateRecipes() {
        recipes = recipeCrudService.findAll();

        fireTableDataChanged();
        setActiveFiter(false);
    }

    public void updateWithFilter(Filter filter) {
        List<Recipe> allRecipes = new ArrayList<>(recipeCrudService.findAll());
        List<Recipe> filteredRecipes = filter.getFilteredRecipes(allRecipes);

        recipes = new ArrayList<>();
        recipes.addAll(filteredRecipes);

        setActiveFiter(true);
        fireTableDataChanged();
    }

    public void deleteRow(int rowIndex) {
        var toDelete = getEntity(rowIndex);
        recipeCrudService.deleteByGuid(toDelete.getGuid());
        recipes.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void clear() {
        if (recipes.isEmpty()) {
            return;
        }
        int last_index = recipes.size();
        recipes.clear();
        recipeCrudService.deleteAll();
        fireTableRowsDeleted(0, last_index - 1);
    }

    public boolean isActiveFiter() {
        return activeFiter;
    }

    public void setActiveFiter(boolean activeFiter) {
        this.activeFiter = activeFiter;
        mainWindow.updateFilterStatus();
    }

    public void customizeTableCell(Component cell, Object value, int row, JTable table) {
    }

    public void customizeTable(JTable table) {
    }
}
