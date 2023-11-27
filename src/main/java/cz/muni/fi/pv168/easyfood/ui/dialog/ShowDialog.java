package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.IngredientWithAmountTableModel;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class ShowDialog extends EntityDialog<Recipe> {
    private final JLabel nameField = new JLabel();
    private final JLabel portionField = new JLabel();
    private final JLabel caloriesField = new JLabel();
    private final JLabel prepareTimeField = new JLabel();
    private final Recipe recipe;
    private final JLabel categoriesField = new JLabel();
    private final JScrollPane ingredientsTable;
    private final JTextArea description = new JTextArea();

    public ShowDialog() {
        this(Recipe.createEmptyRecipe());
    }

    public ShowDialog(Recipe recipe) {
        this.recipe = recipe;
        List<IngredientWithAmount> ingredients = recipe.getIngredients();
        // we can just pass null since we won't be doing any crud operations in showDialog
        var model = new IngredientWithAmountTableModel(ingredients, null, null);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setCellSelectionEnabled(false);
        ingredientsTable = new JScrollPane(table);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        portionField.setText(String.valueOf(recipe.getPortions()));
        caloriesField.setText(String.valueOf(round(recipe.getCalories())));
        prepareTimeField.setText(String.valueOf(recipe.getPreparationTime()));
        categoriesField.setText(recipe.getCategory().getName());
        description.setText(recipe.getDescription());
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEnabled(false);
        description.setDisabledTextColor(Color.BLACK);
        description.setCaretPosition(0);

        Dimension dimension = new Dimension(300, 100);
        ingredientsTable.setMaximumSize(dimension);
        ingredientsTable.setEnabled(false);
    }

    private void addFields() {
        add("Name:", nameField);
        add("Portions:", portionField);
        add("Energy Value (kJ): ", caloriesField);
        add("Time to prepare (minutes): ", prepareTimeField);
        add("Category:", categoriesField);
        add("Ingredients:", ingredientsTable);
        add("Description:", createDescriptionScrollPane(new Dimension(300, 100)));
    }

    private JComponent createDescriptionScrollPane(Dimension size) {
        JScrollPane directionsScrollPane = new JScrollPane(description);
        directionsScrollPane.setMinimumSize(size);
        directionsScrollPane.setPreferredSize(size);
        return directionsScrollPane;
    }

    @Override
    public Recipe getEntity() {
        return recipe;
    }

    @Override
    public boolean valid(Recipe entity) {
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ShowDialog();
    }

    @Override
    public EntityDialog<Recipe> createNewDialog(Recipe recipe, List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new ShowDialog(recipe);
    }

    @Override
    public Optional<Recipe> show(JComponent parentComponent, String title) {
        JOptionPane.showMessageDialog(parentComponent, super.getPanel(), "Show", INFORMATION_MESSAGE, null);
        return Optional.ofNullable(recipe);
    }
}
