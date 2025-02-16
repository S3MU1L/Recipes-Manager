package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.EntityTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.IngredientWithAmountTableModel;
import cz.muni.fi.pv168.easyfood.ui.renderers.CategoryListCellRenderer;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;

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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.round;
import static javax.swing.JOptionPane.ERROR_MESSAGE;


public final class RecipeDialog extends EntityDialog<Recipe> {

    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private static final JSpinner prepTimeField = new JSpinner(new SpinnerNumberModel());
    private static final JSpinner portionField = new JSpinner(new SpinnerNumberModel());
    private final JSpinner amountField = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Integer.MAX_VALUE, 0.5));
    private final JTextArea descriptionArea = new JTextArea();
    private final JComboBox categoryComboBox;
    private final JButton addIngredientButton = new JButton("add Ingredient");
    private final JComboBox<Ingredient> ingredientJComboBox;
    private final JTable table;
    private final List<Recipe> recipes;
    private final Recipe recipe;
    private final EntityTableModel<Category> categoryTableModel;
    private final EntityTableModel<Ingredient> ingredientTableModel;
    private final List<Ingredient> ingredients;
    private final CrudService<IngredientWithAmount> ingredientWithAmountCrudService;
    private final EntityTableModel<IngredientWithAmount> withAmountTableModel;
    private final List<Category> categories;
    private final DependencyProvider dependencyProvider;

    public RecipeDialog(
            List<Recipe> recipes,
            EntityTableModel<Ingredient> ingredientTableModel,
            EntityTableModel<Category> categoryTableModel,
            CrudService<IngredientWithAmount> ingredientWithAmountCrudService,
            DependencyProvider dependencyProvider
    ) {
        this(Recipe.createEmptyRecipe(),
             recipes,
             ingredientTableModel,
             categoryTableModel,
             ingredientWithAmountCrudService,
             dependencyProvider
        );
    }

    public RecipeDialog(
            Recipe recipe,
            List<Recipe> recipes,
            EntityTableModel<Ingredient> ingredientTableModel,
            EntityTableModel<Category> categoryTableModel,
            CrudService<IngredientWithAmount> ingredientWithAmountCrudService,
            DependencyProvider dependencyProvider
    ) {
        this.recipe = recipe;
        this.recipes = recipes;
        this.categoryTableModel = categoryTableModel;
        this.ingredientTableModel = ingredientTableModel;
        this.ingredientWithAmountCrudService = ingredientWithAmountCrudService;
        this.dependencyProvider = dependencyProvider;

        this.categories = IntStream.range(0, categoryTableModel.getRowCount())
                                   .mapToObj(categoryTableModel::getEntity)
                                   .collect(Collectors.toList());

        this.ingredients = IntStream.range(0, ingredientTableModel.getRowCount())
                                    .mapToObj(ingredientTableModel::getEntity)
                                    .collect(Collectors.toList());

        categoryComboBox = new JComboBox<>(categories.stream().map(Category::getName).toArray(String[]::new));
        categoryComboBox.setMaximumRowCount(4);
        var categoryListCellRendered = new CategoryListCellRenderer(categories);
        categoryComboBox.setRenderer(categoryListCellRendered);
        categoryComboBox.addActionListener(
                e -> categoryComboBox.setBackground(
                        categoryListCellRendered.findCategoryByName(categoryComboBox.getSelectedItem().toString())
                                                .getColor())
        );

        if (recipe.getCategory() != null) {
            categoryComboBox.setSelectedItem(recipe.getCategory().getName());
            categoryComboBox.setBackground(recipe.getCategory().getColor());
        } else if (categoryComboBox.getItemCount() > 0) {
            categoryComboBox.setBackground(
                    categoryListCellRendered.findCategoryByName(categoryComboBox.getItemAt(0).toString()).getColor());
        }

        withAmountTableModel =
                new IngredientWithAmountTableModel(recipe.getIngredients(), ingredientWithAmountCrudService,
                                                   dependencyProvider);
        table = new JTable(withAmountTableModel);

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
    }

    private void addFields() {
        add("*Name: ", nameField);
        add("Description: ", createDescriptionScrollPane(new Dimension(300, 100)));
        add("*Time to prepare (minutes): ", prepTimeField);
        add("*Portions: ", portionField);
        add("*Category: ", categoryComboBox);

        JLabel chooseIngredientsLabel = new JLabel("Choose Ingredients");
        chooseIngredientsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        chooseIngredientsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseIngredientsLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add("", chooseIngredientsLabel);

        add("*Amount: ", amountField);
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
        recipe.setPreparationTime((Integer) prepTimeField.getValue());
        recipe.setPortions((Integer) portionField.getValue());
        recipe.setName(nameField.getText().trim());
        recipe.setDescription(descriptionArea.getText().trim());

        String categoryName = (String) categoryComboBox.getSelectedItem(); // Use the new JComboBox
        List<IngredientWithAmount> ingredientsInRecipe = new ArrayList<>();
        List<Category> categorySelected =
                categories.stream().filter(category1 -> category1.getName().equals(categoryName)).toList();

        recipe.setCategory(categorySelected.size() > 0 ? categorySelected.get(0) : Category.createEmptyCategory());
        int rows = withAmountTableModel.getRowCount();
        for (int i = 0; i < rows; i++) {
            ingredientsInRecipe.add(withAmountTableModel.getEntity(i));
        }

        recipe.setIngredients(ingredientsInRecipe);
        return recipe;
    }

    @Override
    public boolean valid(Recipe recipe) {
        int maxCharacters = 8192;
        StringBuilder stringBuilder = new StringBuilder();

        if (recipe.getName().trim().equals("")) {
            stringBuilder.append("Please enter a valid name\n\n");
        }
        if (!recipes.stream().filter(recipe1 -> !recipe1.getGuid().equals(recipe.getGuid()) &&
                recipe1.getName().equals(recipe.getName())).toList().isEmpty()) {
            stringBuilder.append("Duplicate name: ").append(recipe.getName()).append("\n\n");
        }
        if (recipe.getDescription().length() > maxCharacters) {
            stringBuilder.append("Description too long, max character: ").append(recipe.getDescription().length())
                         .append("/").append(maxCharacters).append("\n\n");
        }
        if (recipe.getPreparationTime() == 0) {
            stringBuilder.append("Preparation time can't be zero\n\n");
        }
        if (recipe.getPortions() == 0) {
            stringBuilder.append("Portions can't be zero\n\n");
        }

        if (stringBuilder.isEmpty()) {
            return true;
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
        return false;
    }


    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients,
                                           List<Category> categories, List<Unit> units) {
        return new RecipeDialog(
                Recipe.createEmptyRecipe(),
                recipes,
                ingredientTableModel,
                categoryTableModel,
                ingredientWithAmountCrudService,
                dependencyProvider
        );
    }

    @Override
    public EntityDialog<Recipe> createNewDialog(Recipe recipe, List<Recipe> recipes, List<Ingredient> ingredients,
                                                List<Category> categories, List<Unit> units) {
        return new RecipeDialog(
                recipe,
                recipes,
                ingredientTableModel,
                categoryTableModel,
                ingredientWithAmountCrudService,
                dependencyProvider
        );
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
        recipe.addIngredient(ingredientAndAmount);
        withAmountTableModel.addRow(ingredientAndAmount, recipe);
        amountField.setValue(0.0);
    }

    private boolean validAddIngredient() {
        StringBuilder stringBuilder = new StringBuilder();
        Ingredient selectedIngredient = (Ingredient) ingredientJComboBox.getSelectedItem();
        double amount = (double) amountField.getValue();
        if (amount <= 0) {
            stringBuilder.append("Invalid amount of ingredient\n\n");
        }
        //System.out.println("Ingredient Name " + selectedIngredient.getName());
        IngredientWithAmount ingredient = new IngredientWithAmount(selectedIngredient, (Double) amountField.getValue());
        int rows = withAmountTableModel.getRowCount();

        for (int i = 0; i < rows; i++) {
            if (withAmountTableModel.getEntity(i).getName().equals(ingredient.getName())) {
                stringBuilder.append("Ingredient already present\n\n");
            }
        }

        if (stringBuilder.isEmpty()) {
            return true;
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
        return false;
    }

    private void deleteSelected() {
        int[] selected = table.getSelectedRows();
        for (int i = selected.length - 1; i >= 0; i--) {
            withAmountTableModel.deleteRow(selected[i], recipe);
        }
    }


    private static class IngredientRenderer extends BasicComboBoxRenderer {

        public IngredientRenderer() {
            super();
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
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