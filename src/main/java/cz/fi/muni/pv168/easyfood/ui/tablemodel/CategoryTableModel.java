package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.services.StatisticsService;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;


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
        if (categories.stream().filter(category1 -> category1.getName().equals(category.getName())).toList().size() !=
                0) {
            JOptionPane.showMessageDialog(null, "Unable to add Row -> Name <" + category.getName() +
                    "> duplicity", "Error", INFORMATION_MESSAGE, null);
            return;
        }

        int newRowIndex = categories.size();
        categories.add(category);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category oldCategory, Category newCategory) {
        if (categories.stream().filter(category -> category != oldCategory &&
                category.getName().equals(newCategory.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Unable to edit Row -> Name <" + newCategory.getName() +
                    "> duplicity", "Error", INFORMATION_MESSAGE, null);
            return;
        }
        int rowIndex = categories.indexOf(oldCategory);
        categories.set(rowIndex, newCategory);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, int row) {
        cell.setBackground(getEntity(row).getColor());
        cell.setForeground(Color.BLACK);
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
        if(StatisticsService.calculateCategoryStatistics(categories.get(rowIndex), recipes) > 0){
            JOptionPane.showMessageDialog(null, "Unable to delete category " + categories.get(rowIndex).getName() +
                    ". The category is assigned to the recipe.", "Error", INFORMATION_MESSAGE, null);
            return;
        }
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
