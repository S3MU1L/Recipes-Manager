package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.UnitTableModel;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;


public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JSpinner caloriesField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Integer.MAX_VALUE, 0.5));
    private final JScrollPane unitsField = new JScrollPane();
    private final JList<String> unitsList;
    private final Ingredient ingredient;
    private final List<Ingredient> ingredients;
    private final UnitTableModel unitTableModel;

    public IngredientDialog(List<Ingredient> ingredients, UnitTableModel unitTableModel) {
        this(Ingredient.createEmptyIngredient(), ingredients, unitTableModel);
    }

    public IngredientDialog(Ingredient ingredient, List<Ingredient> ingredients, UnitTableModel unitTableModel) {
        this.ingredient = ingredient;
        this.ingredients = ingredients;
        this.unitTableModel = unitTableModel;

        unitsList = new JList<>(unitTableModel.getEntity().stream().map(Unit::getName).toArray(String[]::new));
        unitsField.setViewportView(unitsList);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloriesField.setValue(ingredient.getCalories());

        Dimension dimension = new Dimension(150, 100);
        unitsField.setMaximumSize(dimension);
        unitsList.setSelectedIndex(unitTableModel.getEntity().indexOf(ingredient.getUnit()));
    }

    private void addFields() {
        add("*Name:", nameField);
        add("*Nutritional value (kJ): ", caloriesField);
        add("*Unit: ", unitsField);
    }

    @Override
    public Ingredient getEntity() {
        ingredient.setName(nameField.getText().trim());
        ingredient.setUnit(unitsList.getSelectedIndex() != -1 ? unitTableModel.getEntity().get(unitsList.getSelectedIndex()) : null);
        ingredient.setCalories((double) caloriesField.getValue());
        return ingredient;
    }

    @Override
    public boolean valid(Ingredient ingredient) {
        StringBuilder stringBuilder = new StringBuilder();

        if (ingredient.getName().trim().equals("")) {
            stringBuilder.append("Please enter a valid name\n\n");
        }
        if (!ingredients.stream().filter(ingredient1 -> !ingredient1.getGuid().equals(ingredient.getGuid()) &&
                ingredient1.getName().equals(ingredient.getName())).toList().isEmpty()) {
            stringBuilder.append("Duplicate name: ").append(ingredient.getName()).append("\n\n");
        }
        if (ingredient.getUnit() == null) {
            stringBuilder.append("Please select a unit\n\n");
        }
        if (ingredient.getCalories() == 0) {
            stringBuilder.append("Nutritional value can't be zero\n\n");
        }

        if (stringBuilder.isEmpty()){
            return true;
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Ingredient Dialog Error", ERROR_MESSAGE, null);
        return false;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients,
                                           List<Category> categories, List<Unit> units) {
        return new IngredientDialog(ingredients, unitTableModel);
    }


    @Override
    public EntityDialog<Ingredient> createNewDialog(Ingredient entity, List<Recipe> recipes,
                                                    List<Ingredient> ingredients, List<Category> categories,
                                                    List<Unit> units) {
        return new IngredientDialog(entity, ingredients, unitTableModel);
    }
}
