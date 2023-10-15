package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.dialog.IngredientDialog;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.IngredientTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddIngredientAction extends AbstractAction {

    private final JTable ingredientTable;
    private final TestDataGenerator testDataGenerator;
    private final ListModel<Unit> unitListModel;

    public AddIngredientAction(JTable ingredientTable, TestDataGenerator testDataGenerator, ListModel<Unit> unitListModel) {
        super("Add Ingredient", Icons.ADD_INGREDIENT_ICON);
        this.ingredientTable = ingredientTable;
        this.testDataGenerator = testDataGenerator;
        this.unitListModel = unitListModel;
        putValue(SHORT_DESCRIPTION, "Adds new ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var ingredientTableModel = (IngredientTableModel) ingredientTable.getModel();
        var dialog = new IngredientDialog(testDataGenerator.createTestIngredient(), unitListModel);
        dialog.show(ingredientTable, "Add ingredient").ifPresent(ingredientTableModel::addRow);
    }
}
