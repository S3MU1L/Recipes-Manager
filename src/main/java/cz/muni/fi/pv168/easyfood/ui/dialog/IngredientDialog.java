package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.EntityTableModel;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JSpinner caloriesField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Integer.MAX_VALUE, 0.5));
    private final JScrollPane unitsField = new JScrollPane();
    private final JList<String> unitsList;
    private final Ingredient ingredient;
    private final List<Ingredient> ingredients;
    private final EntityTableModel<Unit> unitTableModel;

    private final List<Unit> units;

    public IngredientDialog(List<Ingredient> ingredients, EntityTableModel<Unit> unitTableModel) {
        this(Ingredient.createEmptyIngredient(), ingredients, unitTableModel);
    }

    public IngredientDialog(Ingredient ingredient, List<Ingredient> ingredients, EntityTableModel<Unit> unitTableModel) {
        this.ingredient = ingredient;
        this.ingredients = ingredients;
        this.unitTableModel = unitTableModel;

        this.units = IntStream.range(0, unitTableModel.getRowCount())
                .mapToObj(unitTableModel::getEntity)
                .collect(Collectors.toList());

        unitsList = new JList<>(units.stream().map(Unit::getName).toArray(String[]::new));
        unitsField.setViewportView(unitsList);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloriesField.setValue(ingredient.getCalories());

        Dimension dimension = new Dimension(150, 100);
        unitsField.setMaximumSize(dimension);
        unitsList.setSelectedIndex(units.indexOf(ingredient.getUnit()));
    }

    private void addFields() {
        add("Name:", nameField);
        add("Nutritional value (kJ): ", caloriesField);
        add("Unit: ", unitsField);
    }

    @Override
    public Ingredient getEntity() {
        ingredient.setName(nameField.getText().trim());
        ingredient.setUnit(unitsList.getSelectedIndex() != -1 ? units.get(unitsList.getSelectedIndex()) : null);
        ingredient.setCalories((double) caloriesField.getValue());
        return ingredient;
    }

    @Override
    public boolean valid(Ingredient ingredient) {
        if (ingredient.getName().equals("")) {
            JOptionPane.showMessageDialog(null, "Empty Name", "Error", ERROR_MESSAGE, null);
            return false;
        }
        if (ingredient.getUnit() == null) {
            JOptionPane.showMessageDialog(null, "Unit not selected", "Error", ERROR_MESSAGE, null);
            return false;
        }
        if (ingredient.getCalories() == 0) {
            JOptionPane.showMessageDialog(null, "Nutritional value can't be zero", "Error", ERROR_MESSAGE, null);
            return false;
        }
        if (!ingredients.stream().filter(ingredient1 -> ingredient1 != ingredient &&
                ingredient1.getName().equals(ingredient.getName())).toList().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Duplicate name: " + ingredient.getName(), "Error", ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new IngredientDialog(ingredients, unitTableModel);
    }


    @Override
    public EntityDialog<Ingredient> createNewDialog(Ingredient entity, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new IngredientDialog(entity, ingredients, unitTableModel);
    }
}
