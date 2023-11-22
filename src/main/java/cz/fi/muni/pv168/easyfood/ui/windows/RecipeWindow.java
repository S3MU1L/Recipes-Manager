package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.RecipeIngredientService;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.table.IngredientAndAmountTable;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.IngredientAndAmountTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class RecipeWindow extends AbstractWindow {

    private static final I18N I18N = new I18N(RecipeWindow.class);

    private final JLabel nameLabel = new JLabel();
    private final JLabel caloriesLabel = new JLabel();
    private final JLabel prepTimeLabel = new JLabel();
    private final JLabel portionsLabel = new JLabel();
    private final JTextArea directionsArea = new JTextArea();

    private final Recipe recipe;
    private final IngredientAndAmountTable table;

    public RecipeWindow(Recipe recipe, JFrame parentFrame, WindowsManager windowsManager) {
        super(I18N.getString("title"), parentFrame);
        this.recipe = Objects.requireNonNull(recipe);
        this.table = new IngredientAndAmountTable(
                new IngredientAndAmountTableModel(
                        new RecipeIngredientService(recipe, windowsManager)));

        setRecipeInfo();

        directionsArea.setLineWrap(true);
        directionsArea.setWrapStyleWord(true);
        directionsArea.setEditable(false);

        frame.setMinimumSize(new Dimension(300, 400));
        frame.add(createLayout());
        frame.pack();
    }

    private JPanel createLayout() {
        GridPanelBuilder panelBuilder = new GridPanelBuilder();
        panelBuilder.addHeaderRow(I18N.getString("name") + ":", nameLabel);
        panelBuilder.addHeaderRow(I18N.getString("calories") + ":", caloriesLabel);
        panelBuilder.addHeaderRow(I18N.getString("prepTime") + ":", prepTimeLabel);
        panelBuilder.addHeaderRow(I18N.getString("portions") + ":", portionsLabel);
        panelBuilder.addHeaderRow(I18N.getString("ingredients") + ":", new JLabel(""));
        panelBuilder.addFillingBlock(createTableScrollPane(new Dimension(300, 150)));
        panelBuilder.addHeaderRow(I18N.getString("directions") + ":", new JLabel(""));
        panelBuilder.addBlock(createDirectionsScrollPane(new Dimension(300, 100)));
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

    private void setRecipeInfo() {
        nameLabel.setText(recipe.getName());
        caloriesLabel.setText(recipe.getFormattedCalories());
        prepTimeLabel.setText(recipe.getFormattedTime());
        portionsLabel.setText(String.valueOf(recipe.getPortions()));
        directionsArea.setText(recipe.getDirections());
    }

    @Override
    public void onUpdatedRecipe(Recipe recipe, int index) {
        if (this.recipe.equals(recipe)) {
            table.updateAllRows();
            setRecipeInfo();
        }
    }

    @Override
    public void onDeletedRecipe(Recipe recipe, int index) {
        if (recipe.equals(this.recipe)) {
            this.close();
        }
    }
}
