package cz.fi.muni.pv168.easyfood.ui.model.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.services.StatisticsService;
import cz.fi.muni.pv168.easyfood.ui.model.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public class CategoryTableModel extends AbstractTableModel implements EntityTableModel<Category> {
    private final List<Category> categories;
    private List<Recipe> recipes;

    private final CrudService<Category> categoryCrudService;

    public CategoryTableModel(CrudService<Category> categoryCrudService, List<Recipe> recipes,
                              List<Category> categories) {
        this.categoryCrudService = categoryCrudService;
        this.recipes = recipes;
        this.categories = categories;
    }

    private final List<Column<Category, ?>> columns = List.of(Column.readonly("Name", String.class, Category::getName),
                                                              Column.readonly("Recipes per category", String.class,
                                                                              category -> StatisticsService.calculateCategoryStatistics(
                                                                                      category, recipes).toString()));

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
        int newRowIndex = categories.size();
        categories.add(category);
        categoryCrudService.create(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category oldCategory, Category newCategory) {
        categoryCrudService.deleteByGuid(oldCategory.getGuid());
        categoryCrudService.create(newCategory);
        int rowIndex = categories.indexOf(oldCategory);
        categories.set(rowIndex, newCategory);
        fireTableRowsUpdated(rowIndex, rowIndex);
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
        return categories.stream().filter(category -> category.getName().equals(categoryName)).findFirst().orElse(null);
    }

    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    public void deleteRow(int rowIndex) {
        Category removedCategory = categories.get(rowIndex);
        List<Recipe> usedIn = new ArrayList<>();
        recipes.forEach(recipe -> {
            if (recipe.getCategory().equals(removedCategory)) {
                usedIn.add(recipe);
            }
        });

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Category : ").append(removedCategory.getName())
                         .append("\nIt is used in Recipes:");
            for (Recipe recipe : usedIn) {
                stringBuilder.append(" ").append(recipe.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }

        Category category = categories.get(rowIndex);
        categoryCrudService.deleteByGuid(category.getGuid());
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
