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
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public class CategoryTableModel extends AbstractTableModel implements EntityTableModel<Category> {
    private final List<Category> categories;
    private List<Recipe> recipes;
    private final CrudService<Category> categoryCrudService;

    private final CrudService<Recipe> recipeCrudService;

    public CategoryTableModel(CrudService<Category> categoryCrudService, CrudService<Recipe> recipeCrudService) {
        this.categoryCrudService = categoryCrudService;
        this.recipeCrudService = recipeCrudService;
        this.recipes = recipeCrudService.findAll();
        this.categories = categoryCrudService.findAll();
    }

    private final List<Column<Category, ?>> columns = List.of(
            Column.readonly("Name", String.class, Category::getName),
            Column.readonly("Recipes per category", String.class, category -> StatisticsService.calculateCategoryStatistics(category, recipes).toString())
    );

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
        categoryCrudService.create(category)
                .intoException();
        int newRowIndex = categories.size();
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category oldCategory, Category newCategory) {
        categoryCrudService.update(newCategory)
                .intoException();
        int rowIndex = categories.indexOf(oldCategory);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void deleteRow(int rowIndex) {
        var toDelete = categories.get(rowIndex);
        List<Recipe> usedIn = new ArrayList<>();
        recipes.forEach(recipe -> {
            if (recipe.getCategory().equals(toDelete)) {
                usedIn.add(recipe);
            }
        });

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Category : ").append(toDelete.getName()).append("\nIt is used in Recipes:");
            for (Recipe recipe : usedIn) {
                stringBuilder.append(" ").append(recipe.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }
        categoryCrudService.deleteByGuid(toDelete.getGuid());
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
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


    @Override
    public void customizeTable(JTable table) {

    }

    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    protected void updateEntity(Category entity) {

    }

}
