package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.services.StatisticsService;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public class CategoryTableModel extends EntityTableModel<Category> {
    private final List<Category> categories;
    private final List<Recipe> recipes;

    public CategoryTableModel(List<Category> categories, List<Recipe> recipes) {
        super(List.of(
                Column.readOnly("Name", String.class, Category::getName),
                Column.readOnly("Recipes per category", String.class, category -> StatisticsService.calculateCategoryStatistics(category, recipes).toString())
        ));
        this.categories = categories;
        this.recipes = recipes;
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    public void addRow(Category category) {
        int newRowIndex = categories.size();
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category category) {
        int rowIndex = categories.indexOf(category);
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

    @Override
    protected void updateEntity(Category entity) {

    }

    @Override
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
            stringBuilder.append("Unable to delete Category : ").append(removedCategory.getName()).append("\nIt is used in Recipes:");
            for (Recipe recipe : usedIn) {
                stringBuilder.append(" ").append(recipe.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
