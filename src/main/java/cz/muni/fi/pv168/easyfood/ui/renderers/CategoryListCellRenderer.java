package cz.muni.fi.pv168.easyfood.ui.renderers;

import cz.muni.fi.pv168.easyfood.business.model.Category;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;
import java.util.List;

public class CategoryListCellRenderer extends DefaultListCellRenderer {

    private final List<Category> categories;

    public CategoryListCellRenderer(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof String) {
            String categoryName = (String) value;
            Category category = findCategoryByName(categoryName);
            if (category != null) {
                setBackground(category.getColor());
            }
        }

        return this;
    }

    public Category findCategoryByName(String categoryName) {
        return categories.stream()
                .filter(category -> category.getName().equals(categoryName))
                .findFirst()
                .orElse(null);
    }
}