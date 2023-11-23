package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.List;


public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JScrollPane unitsField = new JScrollPane();
    private final JList<String> unitsList;
    private final List<Unit> units;
    private final Ingredient ingredient;

    public IngredientDialog(List<Unit> units) {
        this(Ingredient.createEmptyIngredient(), units);
    }

    public IngredientDialog(Ingredient ingredient, List<Unit> units) {
        this.ingredient = ingredient;
        this.units = units;
        unitsList = new JList<>(units.stream().map(Unit::getName).toArray(String[]::new));
        unitsField.setViewportView(unitsList);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloriesField.setText(String.valueOf(ingredient.getCalories()));

        Dimension dimension = new Dimension(150, 100);
        unitsField.setMaximumSize(dimension);
        unitsList.setSelectedIndex(units.indexOf(ingredient.getUnit()));
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories (kJ): ", caloriesField);
        add("Unit: ", unitsField);
    }

    @Override
    public Ingredient getEntity() {
        String name = nameField.getText();
        double calories = Double.parseDouble(caloriesField.getText());
        Unit unit = units.get(unitsList.getSelectedIndex());
        return new Ingredient(name, calories, unit);
    }

    @Override
    public boolean valid(Ingredient entity) {
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new IngredientDialog(units);
    }


    @Override
    public EntityDialog<Ingredient> createNewDialog(Ingredient entity, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new IngredientDialog(entity, units);
    }
}
