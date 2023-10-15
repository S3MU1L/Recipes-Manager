package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tibor Pelegrin
 */
public class CategoryTableModel extends EntityTableModel<Category>{
    private final List<Category> categories;

    public CategoryTableModel(List<Category> categories) {
        super(List.of(
                Column.readOnly("Name", String.class, Category::getName)
        ));
        this.categories = new ArrayList<>(categories);
    }

    @Override
    public int getRowCount() {
        return categories.size();
    }

    public void addRow(Category ingredient) {
        int newRowIndex = categories.size();
        categories.add(ingredient);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Category ingredient) {
        int rowIndex = categories.indexOf(ingredient);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public Category getEntity(int rowIndex) {
        return categories.get(rowIndex);
    }

    @Override
    protected void updateEntity(Category entity) {

    }

    @Override
    public void deleteRow(int rowIndex) {
        categories.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
