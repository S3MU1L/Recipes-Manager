package cz.muni.fi.pv168.easyfood.ui.dialog;


import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.util.List;
import java.util.Optional;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class ShowDialog extends EntityDialog<Recipe> {
    private final JTextField nameField = new JTextField();
    private final JTextField portionField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JTextField prepareTimeField = new JTextField();
    private final Recipe recipe;
    private final JTextField categoriesField = new JTextField();
    private final JScrollPane ingredientsTable;
    private final Box descriptionBox = Box.createVerticalBox();
    private final JTextArea description = new JTextArea(5, 20);
    public ShowDialog() {
        this(Recipe.createEmptyRecipe());
    }

    public ShowDialog(Recipe recipe) {
        this.recipe = recipe;
        List<IngredientWithAmount> ingredients = recipe.getIngredients();
        var model = new IngredientWithAmountTableModel(ingredients);
         var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(false);
        ingredientsTable = new JScrollPane(table);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        nameField.setEditable(false);
        portionField.setText(String.valueOf(recipe.getPortions()));
        portionField.setEditable(false);
        caloriesField.setText(String.valueOf(round(recipe.getCalories())));
        caloriesField.setEditable(false);
        prepareTimeField.setText(String.valueOf(recipe.getPreparationTime()));
        prepareTimeField.setEditable(false);
        categoriesField.setText(recipe.getCategory().getName());
        categoriesField.setEditable(false);
        description.setText(recipe.getDescription());
        description.setEditable(false);
        descriptionBox.add(description);

        Dimension dimension = new Dimension(250, 100);
        ingredientsTable.setMaximumSize(dimension);
    }

    private void addFields() {
        add("Name:", nameField);
        add("Portions:", portionField);
        add("Energy Value (kJ): ", caloriesField);
        add("Time to prepare (minutes): ", prepareTimeField);
        add("Category:", categoriesField);
        add("Ingredients:", ingredientsTable);
        add("Description:", descriptionBox);
    }

    @Override
    public Recipe getEntity() {
        return recipe;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ShowDialog();
    }

    @Override
    public EntityDialog<Recipe> createNewDialog(Recipe recipe, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ShowDialog(recipe);
    }

    @Override
    public Optional<Recipe> show(JComponent parentComponent, String title) {
        JOptionPane.showMessageDialog(parentComponent, super.getPanel(), "Show", INFORMATION_MESSAGE, null);
        return Optional.ofNullable(recipe);
    }
}
