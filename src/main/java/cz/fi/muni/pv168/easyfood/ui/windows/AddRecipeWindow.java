package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.IngredientService;
import cz.fi.muni.pv168.easyfood.service.RecipeIngredientService;
import cz.fi.muni.pv168.easyfood.service.RecipeService;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.table.IngredientAndAmountTable;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.IngredientAndAmountTableModel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class AddRecipeWindow extends AbstractWindow {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final RecipeIngredientService recipeIngredientService;

    private static final I18N I18N = new I18N(AddRecipeWindow.class);

    private final JTextField nameField = new JTextField();
    private final JTextField prepTimeField = new JTextField();
    private final JTextField portionsField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JTextArea directionsArea = new JTextArea();
    private final JComboBox<Ingredient> ingredientJComboBox;
    private final JButton addIngredientButton = new JButton(I18N.getString("addIngredientButton"));
    private final JButton addRecipeButton = new JButton(I18N.getString("addRecipeButton"));

    private final IngredientAndAmountTable table;

    private final Recipe recipe;

    public AddRecipeWindow(RecipeService recipeService, IngredientService ingredientService, JFrame parentFrame, Recipe recipe, WindowsManager windowsManager) {
        super(I18N.getString(recipe.getID() == null ? "title" : "editTitle"), parentFrame);
        this.recipe = Objects.requireNonNull(recipe).getShallowCopy();
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.recipeIngredientService = new RecipeIngredientService(this.recipe, windowsManager);
        this.table = new IngredientAndAmountTable(
                new IngredientAndAmountTableModel(
                        recipeIngredientService));

        ingredientJComboBox = new JComboBox<>(getIngredients());
        ingredientJComboBox.setRenderer(new IngredientRenderer());

        Action deleteAction = new CustomDeleteAction(this::deleteSelected);
        table.addActions(List.of(deleteAction));

        addRecipeButton.addActionListener(e -> addRecipe());
        addIngredientButton.addActionListener(e -> addIngredient());

        initializeRecipeInfo();

        directionsArea.setLineWrap(true);
        directionsArea.setWrapStyleWord(true);

        frame.setMinimumSize(new Dimension(300, 450));
        frame.add(createLayout());
        frame.pack();
    }

    private JPanel createLayout() {
        GridPanelBuilder panelBuilder = new GridPanelBuilder();
        panelBuilder.addHeaderRow(I18N.getString("name") + ":", nameField);
        panelBuilder.addHeaderRow(I18N.getString("prepTime") + ": (min)", prepTimeField);
        panelBuilder.addHeaderRow(I18N.getString("portions") + ":", portionsField);
        panelBuilder.addHeaderRow(I18N.getString("amount")+ ":", amountField);
        panelBuilder.addHeaderRow(I18N.getString("ingredient")+ ":", ingredientJComboBox);
        panelBuilder.addHeaderRow("", addIngredientButton);
        panelBuilder.addFillingBlock(createTableScrollPane(new Dimension(300, 150)));
        panelBuilder.addHeaderRow(I18N.getString("directions") + ":", new JLabel(""));
        panelBuilder.addBlock(createDirectionsScrollPane(new Dimension(300, 100)));
        panelBuilder.addBlock(addRecipeButton);
        return panelBuilder.getPanel();
    }

    private JScrollPane createTableScrollPane(Dimension size) {
        JScrollPane tableScrollPane = table.wrapIntoScrollPane();
        tableScrollPane.setMinimumSize(size);
        tableScrollPane.setPreferredSize(size);
        return tableScrollPane;
    }

    private JScrollPane createDirectionsScrollPane(Dimension size) {
        JScrollPane directionsScrollPane = new JScrollPane(directionsArea);
        directionsScrollPane.setMinimumSize(size);
        directionsScrollPane.setPreferredSize(size);
        return directionsScrollPane;
    }

    private void initializeRecipeInfo(){
        nameField.setText(recipe.getName());
        prepTimeField.setText(String.valueOf(recipe.getPrepTime()));
        portionsField.setText(String.valueOf(recipe.getPortions()));
        directionsArea.setText(recipe.getDirections());
        addRecipeButton.setText(I18N.getString("saveRecipeButton"));
    }

    private void addRecipe() {
        if (! validAddRecipe()) {
            return;
        }

        recipe.setName(nameField.getText());
        recipe.setDirections(directionsArea.getText());
        recipe.setPrepTime(Integer.parseInt(prepTimeField.getText()));
        recipe.setPortions(Integer.parseInt(portionsField.getText()));

        if (recipe.getID() == null){
            recipeService.add(recipe);
        } else {
            recipeService.update(recipe);
        }

        frame.dispose();
    }

    private boolean validAddRecipe() {
        if (nameField.getText().isEmpty()) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidName"), parentFrame));
            return false;
        }
        if (prepTimeField.getText().isEmpty()) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("emptyPrepTime"), parentFrame));
            return false;
        } else {
            try {
                if (Integer.parseInt(prepTimeField.getText()) <= 0) {
                    WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidPrepTime"), parentFrame));
                    return false;
                }
            } catch (NumberFormatException e) {
                WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidPrepTime"), parentFrame));
                return false;
            }
        }
        if (portionsField.getText().isEmpty()) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("emptyPortions"), parentFrame));
            return false;
        } else {
            try {
                if (Integer.parseInt(portionsField.getText()) <= 0) {
                    WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidPortions"), parentFrame));
                    return false;
                }
            } catch (NumberFormatException e) {
                WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidPortions"), parentFrame));
                return false;
            }
        }
        if (directionsArea.getText().isEmpty()) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidDirections"), parentFrame));
            return false;
        }
        return true;
    }

    private Vector<Ingredient> getIngredients() {
        return new Vector<>(ingredientService.getEntityList());
    }

    private void addIngredient() {
        if (!validAddIngredient()) {
            return;
        }
        double amount = Double.parseDouble(amountField.getText());
        var ingredient = (Ingredient) ingredientJComboBox.getSelectedItem();
        if (ingredient == null) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("emptyIngredientList"), frame));
            return;
        }
        var ingredientAndAmount = new IngredientAndAmount(ingredient, amount);
        recipeIngredientService.add(ingredientAndAmount);
    }

    private boolean validAddIngredient() {
        if (recipe.contains((Ingredient) ingredientJComboBox.getSelectedItem())) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("duplicateIngredient"), frame));
            return false;
        }
        if (amountField.getText().isEmpty()) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidAmount"), frame));
            return false;
        }
        try {
            Double.parseDouble(amountField.getText());
        } catch (Exception e) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidAmountFormat"), frame));
            return false;
        }
        return true;
    }

    @Override
    public void onDeletedIngredient(Ingredient ingredient, int index) {
        ingredientJComboBox.removeItem(ingredient);
        recipeIngredientService.delete(new IngredientAndAmount(ingredient, 0)); // ID is the same
    }

    @Override
    public void onAddedRecipeIngredient(Recipe recipe) {
        if (recipe == this.recipe) { // yes we test for reference equality - recipe may be doesnt have ID
            table.add();
        }
    }

    @Override
    public void onDeletedRecipeIngredient(Recipe recipe, Ingredient ingredient, int index) {
        if (recipe == this.recipe) {
            table.delete(index);
        }
    }

    private void deleteSelected() {
        List<IngredientAndAmount> selected = table.getSelectedEntities();
        var iterator = selected.listIterator(selected.size());
        while (iterator.hasPrevious()) {
            recipeIngredientService.delete(iterator.previous());
        }
    }

    private static class IngredientRenderer extends BasicComboBoxRenderer {

        private static final I18N I18N = new I18N(IngredientRenderer.class);

        public IngredientRenderer() {
            super();
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value == null) {
                setText(I18N.getString("emptyList"));
                return this;
            }
            Ingredient ingredient = (Ingredient)value;
            setText(ingredient.getFormattedIngredient());
            return this;
        }
    }

    private static class CustomDeleteAction extends AbstractAction {

        private static final I18N I18N = new I18N(CustomDeleteAction.class);

        private final Runnable delete;

        private CustomDeleteAction(Runnable delete) {
            super(I18N.getString("delete"), Icons.DELETE_ICON);
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
