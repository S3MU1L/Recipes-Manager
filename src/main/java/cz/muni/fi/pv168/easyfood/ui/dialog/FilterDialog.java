package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Filter;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class FilterDialog extends EntityDialog<Filter> {
    private final Filter filter;
    private final List<Category> categories;
    private final List<Ingredient> ingredients;
    private static final JTextField nameField = new JTextField();
    private static final Box categoriesBox = Box.createVerticalBox();
    private static final JScrollPane categoriesField = new JScrollPane(categoriesBox);
    private static final Box ingredientsBox = Box.createVerticalBox();
    private static final JScrollPane ingredientsField = new JScrollPane(ingredientsBox);
    private static final JSpinner timeField = new JSpinner(new SpinnerNumberModel());
    private static final JSpinner minNutritionalValueField = new JSpinner(new SpinnerNumberModel());
    private static final JSpinner maxNutritionalValueField = new JSpinner(new SpinnerNumberModel());
    private static final JSlider portionsField = new JSlider();
    private static final JButton resetButton = new JButton();
    private static final Set<Category> filterCategories = new HashSet<>();
    private static final Set<Ingredient> filterIngredients = new HashSet<>();
    private final List<JCheckBox> categoriesCheckboxes = new ArrayList<>();
    private final List<JCheckBox> ingredientsCheckboxes = new ArrayList<>();

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
        filter.setName(nameField.getText());
        filter.setCategories(filterCategories);
        filter.setIngredients(filterIngredients);
        filter.setPreparationTime((int) timeField.getValue());
        filter.setMinimumNutritionalValue((int) minNutritionalValueField.getValue());
        filter.setMaximumNutritionalValue((int) maxNutritionalValueField.getValue());
        filter.setPortions(portionsField.getValue());
        return filter;
    }

    @Override
    public boolean valid(Filter filter) {
        if (filter.getMaximumNutritionalValue() > filter.getMaximumNutritionalValue()) {
            JOptionPane.showMessageDialog(null, "Minimum can't be higher than maximum", "Error", ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new FilterDialog(categories, ingredients);
    }

    @Override
    public EntityDialog<Filter> createNewDialog(Filter entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new FilterDialog(categories, ingredients, filter);
    }

    private void addFields() {
        add("Name:", nameField);
        add("Category:", categoriesField);
        add("Ingredients:", ingredientsField);
        add("Preparation time:", timeField);
        add("Min nutritional value:", minNutritionalValueField);
        add("Max nutritional value:", maxNutritionalValueField);
        add("Portions:", portionsField);
        add("", resetButton);
    }

    private void setValues() {
        Dimension dimension = new Dimension(150, 100);
        categoriesField.setMaximumSize(dimension);
        ingredientsField.setMaximumSize(dimension);

        for (Category category : categories) {
            JCheckBox checkBox = new JCheckBox(category.getName());
            categoriesCheckboxes.add(checkBox);
            categoriesBox.add(checkBox);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    filterCategories.add(category);
                } else {
                    filterCategories.remove(category);
                }
            });
        }

        for (Ingredient ingredient : ingredients) {
            JCheckBox checkBox = new JCheckBox(ingredient.getName());
            ingredientsCheckboxes.add(checkBox);
            ingredientsBox.add(checkBox);
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    filterIngredients.add(ingredient);
                } else {
                    filterIngredients.remove(ingredient);
                }
            });
        }

        ((SpinnerNumberModel) timeField.getModel()).setMinimum(0);
        ((SpinnerNumberModel) minNutritionalValueField.getModel()).setMinimum(0);
        ((SpinnerNumberModel) maxNutritionalValueField.getModel()).setMinimum(0);

        Hashtable<Integer, JComponent> labelTable = portionsField.createStandardLabels(1);
        labelTable.put(0, new JLabel("Any"));
        labelTable.put(6, new JLabel(">5"));
        portionsField.setLabelTable(labelTable);
        portionsField.setMinimum(0);
        portionsField.setMaximum(6);
        portionsField.setMajorTickSpacing(1);
        portionsField.setPaintLabels(true);
        portionsField.setPaintTicks(true);
        portionsField.setSnapToTicks(true);

        resetButton.setText("Reset");
        resetButton.addActionListener(e -> {
            nameField.setText("");
            for (JCheckBox checkBox : categoriesCheckboxes) checkBox.setSelected(false);
            for (JCheckBox checkBox : ingredientsCheckboxes) checkBox.setSelected(false);
            timeField.setValue(0);
            minNutritionalValueField.setValue(0);
            maxNutritionalValueField.setValue(0);
            portionsField.setValue(0);
            categories.forEach(filterCategories::remove);
            ingredients.forEach(filterIngredients::remove);
        });
    }
}