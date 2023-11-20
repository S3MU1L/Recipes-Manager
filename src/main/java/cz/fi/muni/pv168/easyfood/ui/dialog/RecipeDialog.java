package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.renderers.CategoryListCellRenderer;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.IngredientWithAmountTableModel;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static java.lang.Math.round;


public final class RecipeDialog extends EntityDialog<Recipe> {

    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private static final JSpinner prepTimeField = new JSpinner(new SpinnerNumberModel());
    private static final JSpinner portionField = new JSpinner(new SpinnerNumberModel());
    private final JSpinner amountField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Integer.MAX_VALUE, 0.5));
    private final JTextArea descriptionArea = new JTextArea();
    private final List<Category> categories;
    private final JScrollPane categoriesPane = new JScrollPane();
    private final JButton addIngredientButton = new JButton("add Ingredient");
    private final JComboBox<Ingredient> ingredientJComboBox;
    private final JTable table;
    private final Recipe recipe;
    private final IngredientWithAmountTableModel model;

    public RecipeDialog(List<Ingredient> ingredients, List<Category> categories) {
        this(Recipe.createEmptyRecipe(), ingredients, categories);
    }

    public RecipeDialog(Recipe recipe, List<Ingredient> ingredients, List<Category> categories) {
        this.recipe = recipe;
        this.categories = categories;

        JList<String> categoriesList = new JList<>(categories.stream().map(Category::getName).toArray(String[]::new));
        categoriesList.setCellRenderer(new CategoryListCellRenderer(categories));

        if (recipe.getCategory() != null) {
            categoriesList.setSelectedValue(recipe.getCategory().getName(), true);
        }

        categoriesPane.setViewportView(categoriesList);

        model = new IngredientWithAmountTableModel(recipe.getIngredients());
        table = new JTable(model);

        ingredientJComboBox = new JComboBox<>(new Vector<>(ingredients));
        ingredientJComboBox.setRenderer(new IngredientRenderer());

        Action deleteAction = new CustomDeleteAction(this::deleteSelected);
        table.setComponentPopupMenu(createPopUpMenu(deleteAction));

        addIngredientButton.addActionListener(e -> addIngredient());

        descriptionArea.setText(recipe.getDescription());
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        setValues();
        addFields();
    }

    private JPopupMenu createPopUpMenu(Action deleteAction) {
        var menu = new JPopupMenu();
        menu.add(deleteAction);
        return menu;
    }

    private void setValues() {
        nameField.setText(recipe.getName());
        caloriesField.setText(String.valueOf(round(recipe.getCalories())));
        prepTimeField.setValue(recipe.getPreparationTime());
        portionField.setValue(recipe.getPortions());
        amountField.setValue(0.0);

        ((SpinnerNumberModel) prepTimeField.getModel()).setMinimum(0);
        ((SpinnerNumberModel) portionField.getModel()).setMinimum(0);

        Dimension dimension = new Dimension(150, 100);
        categoriesPane.setMaximumSize(dimension);
    }

    private void addFields() {
        add("Name: ", nameField);
        add("Description: ", createDescriptionScrollPane(new Dimension(300, 100)));
        add("Time to prepare (minutes): ", prepTimeField);
        add("Portions: ", portionField);
        add("Category: ", categoriesPane);

        JLabel chooseIngredientsLabel = new JLabel("Choose Ingredients");
        chooseIngredientsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        chooseIngredientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseIngredientsLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add("", chooseIngredientsLabel);

        add("Amount: ", amountField);
        add("Ingredients: ", ingredientJComboBox);
        add("", addIngredientButton);
        add("", createTableScrollPane(new Dimension(300, 150)));
    }

    private JComponent createDescriptionScrollPane(Dimension size) {
        JScrollPane directionsScrollPane = new JScrollPane(descriptionArea);
        directionsScrollPane.setMinimumSize(size);
        directionsScrollPane.setPreferredSize(size);
        return directionsScrollPane;
    }

    private JScrollPane createTableScrollPane(Dimension size) {
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setMinimumSize(size);
        tableScrollPane.setPreferredSize(size);
        return tableScrollPane;
    }

    @Override
    public Recipe getEntity() {
        if (!validRecipe()) {
            return null;
        }

        int preparationTime = (Integer) prepTimeField.getValue();
        int portions = (Integer) portionField.getValue();
        String name = nameField.getText();
        String descriptionText = descriptionArea.getText();
        JList<String> categoriesNames = (JList<String>) categoriesPane.getViewport().getView();
        String categoryName = categoriesNames.getSelectedValue();

        if (categoryName == null) {
            JOptionPane.showMessageDialog(null, "Please pick a category");
            return null;
        }

        List<IngredientWithAmount> ingredientsInRecipe = new ArrayList<>();
        List<Category> categorySelected = categories.stream().filter(category1 -> category1.getName().equals(categoryName)).toList();
        Category category = categorySelected.size() > 0 ? categorySelected.get(0) : Category.createEmptyCategory();

        int rows = model.getRowCount();
        for (int i = 0; i < rows; i++) {
            ingredientsInRecipe.add(model.getEntity(i));
        }
        return new Recipe(name, ingredientsInRecipe, descriptionText, preparationTime, portions, category);
    }

    private boolean validRecipe() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a valid name");
            return false;
        }

        try {
            int preparationTime = Integer.parseInt(prepTimeField.getValue().toString());
            if (preparationTime < 0) {
                JOptionPane.showMessageDialog(null, "Preparation time must be a non-negative integer");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid preparation time");
            return false;
        }

        try {
            int portions = Integer.parseInt(portionField.getValue().toString());
            if (portions <= 0) {
                JOptionPane.showMessageDialog(null, "Portions must be a positive integer");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number of portions");
            return false;
        }

        return true;
    }


    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new RecipeDialog(ingredients, categories);
    }

    @Override
    public EntityDialog<Recipe> createNewDialog(Recipe entity, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new RecipeDialog(entity, ingredients, categories);
    }

    private void addIngredient() {
        if (!validAddIngredient()) {
            return;
        }

        double amount = (double) amountField.getValue();
        var ingredient = (Ingredient) ingredientJComboBox.getSelectedItem();
        if (ingredient == null) {
            return;
        }

        var ingredientAndAmount = new IngredientWithAmount(ingredient, amount);
        model.addRow(ingredientAndAmount);
        amountField.setValue(0.0);
    }

    private boolean validAddIngredient() {
        Ingredient selectedIngredient = (Ingredient) ingredientJComboBox.getSelectedItem();
        double amount = (double) amountField.getValue();
        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Invalid amount of ingredient");
            return false;
        }

        IngredientWithAmount ingredient = new IngredientWithAmount(selectedIngredient, (Double) amountField.getValue());
        int rows = model.getRowCount();

        for (int i = 0; i < rows; i++) {
            if (model.getEntity(i).equals(ingredient)) {
                JOptionPane.showMessageDialog(null, "Ingredient already present");
                return false;
            }
        }

        return true;
    }

    private void deleteSelected() {
        int[] selected = table.getSelectedRows();
        for (int i = selected.length - 1; i >= 0; i--) {
            model.deleteRow(selected[i]);
        }
    }


    private static class IngredientRenderer extends BasicComboBoxRenderer {

        public IngredientRenderer() {
            super();
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value == null) {
                setText("empty List");
                return this;
            }
            Ingredient ingredient = (Ingredient) value;
            setText(ingredient.getName() + " " + ingredient.getFormattedCalories());
            return this;
        }
    }

    private static class CustomDeleteAction extends AbstractAction {
        private final Runnable delete;

        private CustomDeleteAction(Runnable delete) {
            super("Delete", Icons.DELETE_ICON);
            this.delete = delete;
            setEnabled(true);
            putValue(MNEMONIC_KEY, KeyEvent.VK_D);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            delete.run();
        }
    }

}