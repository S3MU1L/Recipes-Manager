package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.model.RecipeTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class AddRecipeAction extends AbstractAction {

    private final JTable recipeTable;
    private final TestDataGenerator testDataGenerator;

    public AddRecipeAction(JTable recipeTable, TestDataGenerator testDataGenerator) {
        super("Add", Icons.ADD_RECIPE_ICON);
        this.recipeTable = recipeTable;
        this.testDataGenerator = testDataGenerator;
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeTableModel = (RecipeTableModel) recipeTable.getModel();
        recipeTableModel.addRow(testDataGenerator.createTestRecipe());
    }
}
