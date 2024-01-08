package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.services.StatisticsService;
import cz.muni.fi.pv168.easyfood.ui.model.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public class CategoryTableModel extends AbstractTableModel implements EntityTableModel<Category> {
    private List<Category> categories;
    private RecipeTableModel recipeTableModel;
    private final CrudService<Category> categoryCrudService;

    public CategoryTableModel(CrudService<Category> categoryCrudService, RecipeTableModel recipeTableModel,
                              List<Category> categories) {
        this.categoryCrudService = categoryCrudService;
        this.recipeTableModel = recipeTableModel;
        this.categories = categories;
    }

    private final List<Column<Category, ?>> columns = List.of(Column.readonly("Name", String.class, Category::getName),
                                                              Column.readonly("Recipes per category", String.class,
                                                                              category -> StatisticsService.calculateCategoryStatistics(
                                                                                                                   category,
                                                                                                                   recipeTableModel.getEntity())
                                                                                                           .toString()));

    @Override
    public int getRowCount() {
        return categories.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var category = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(category);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value != null) {
            var category = getEntity(rowIndex);
            columns.get(columnIndex).setValue(value, category);
//            updateRow(category);
        }
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

    public void addRow(Category category) {
        categoryCrudService.create(category)
                           .intoException();
        int newRowIndex = categories.size();
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category category) {
        categoryCrudService.update(category)
                           .intoException();
        int rowIndex = categories.indexOf(category);
        fireTableRowsUpdated(rowIndex, rowIndex);
        recipeTableModel.updateAll();
    }

    @Override
    public void updateAll() {
        categories = new ArrayList<>(categoryCrudService.findAll());
    }

    public void deleteRows(int[] rowIndexes) {
        recipeTableModel.reset();
        List<Category> toDelete =
                Arrays.stream(rowIndexes).sequential().mapToObj(rowIndex -> categories.get(rowIndex)).toList();
        StringBuilder stringBuilder = new StringBuilder();

        Map<Category, List<Recipe>> usedIn = new HashMap<>();
        for (Recipe recipe : recipeTableModel.getEntity()) {
            Category category = recipe.getCategory();

            if (toDelete.contains(category)) {
                usedIn.computeIfAbsent(category, k -> new ArrayList<>());
                usedIn.get(category).add(recipe);
            }
        }

        if (!usedIn.isEmpty()) {
            for (var entry : usedIn.entrySet()) {
                stringBuilder.append("Unable to delete Category : ").append(entry.getKey().getName())
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
            return;
        }

        for (Category category : toDelete) {
            categoryCrudService.deleteByGuid(category.getGuid());
        }
        categories.removeAll(toDelete);
        fireTableRowsDeleted(rowIndexes[0], rowIndexes[rowIndexes.length - 1]);
    }


    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {
        Object fstCol = table.getValueAt(row, 0);
        if (fstCol instanceof String) {
            Category category = findCategoryByName((String) fstCol);
            cell.setBackground(category.getColor());
            cell.setForeground(Color.BLACK);
        }
    }

    public Category findCategoryByName(String categoryName) {
        return categories.stream()
                         .filter(category -> category.getName().equals(categoryName))
                         .findFirst()
                         .orElse(null);
    }


    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    @Override
    public List<Category> getEntity() {
        return categories;
    }

}
