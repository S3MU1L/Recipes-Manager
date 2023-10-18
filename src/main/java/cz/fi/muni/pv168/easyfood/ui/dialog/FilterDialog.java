package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Filter;
import cz.fi.muni.pv168.easyfood.model.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FilterDialog extends EntityDialog<Filter> {
    private final Filter filter;
    private final List<Category> categories;
    private final List<Ingredient> ingredients;
    private final JTextField nameField = new JTextField();
    private final Box categoriesBox = Box.createVerticalBox();
    private final JScrollPane categoriesField = new JScrollPane(categoriesBox);
    private final Box ingredientsBox = Box.createVerticalBox();
    private final JScrollPane ingredientsField = new JScrollPane(ingredientsBox);
    private final JSlider timeField = new JSlider();
    private final JTextField nutritionalValueField = new JTextField();
    private final JSlider portionsField = new JSlider();

    public FilterDialog(List<Category> categories, List<Ingredient> ingredients, Filter filter) {
        this.filter = filter;
        this.categories = categories;
        this.ingredients = ingredients;
        addFields();
        setValues();
    }

    public FilterDialog(List<Category> categories, List<Ingredient> ingredients) {
        this(categories, ingredients, Filter.createEmptyFilter());
    }

    @Override
    public Filter getEntity() {
        return filter;
    }

    @Override
    public EntityDialog<?> createNewDialog() {
        return new FilterDialog(categories, ingredients);
    }

    @Override
    public EntityDialog<Filter> createNewDialog(Filter entity) {
        return new FilterDialog(categories, ingredients, filter);
    }

    private void addFields() {
        add("Name:", nameField);
        add("Category:", categoriesField);
        add("Ingredients:", ingredientsField);
        add("Preparation time:", timeField);
        add("Max nutritional value:", nutritionalValueField);
        add("Portions:", portionsField);
    }

    private void setValues() {
        nameField.setText("");
        Dimension dimension = new Dimension(150, 100);
        categoriesField.setMaximumSize(dimension);
        ingredientsField.setMaximumSize(dimension);

        for (Category category : categories) {
            categoriesBox.add(new JCheckBox(category.getName()));
        }

        for (Ingredient ingredient : ingredients) {
            ingredientsBox.add(new JCheckBox(ingredient.getName()));
        }

        timeField.setMinimum(0);
        timeField.setMaximum(12);
        timeField.setMajorTickSpacing(1);
        timeField.setPaintLabels(true);
        timeField.setPaintTicks(true);
        timeField.setSnapToTicks(true);

        portionsField.setMinimum(1);
        portionsField.setMaximum(6);
        portionsField.setMajorTickSpacing(1);
        portionsField.setPaintLabels(true);
        portionsField.setPaintTicks(true);
        portionsField.setSnapToTicks(true);
    }
}
