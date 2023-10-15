package cz.fi.muni.pv168.easyfood.ui.action;

import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.dialog.IngredientDialog;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.CategoryTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.IngredientTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * @author Tibor Pelegrin
 */
public class AddCategoryAction extends AbstractAction {
    private final JTable categoryTable;
    private final TestDataGenerator testDataGenerator;

    public AddCategoryAction(JTable categoryTable, TestDataGenerator testDataGenerator) {
        super("Add Category", Icons.ADD_INGREDIENT_ICON);
        this.categoryTable = categoryTable;
        this.testDataGenerator = testDataGenerator;
        putValue(SHORT_DESCRIPTION, "Adds new category");
        putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl C"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var categoryTableModel = (CategoryTableModel) categoryTable.getModel();
        var dialog = new categoryDialog(testDataGenerator.createTestCategory());
        dialog.show(categoryTable, "Add Category").ifPresent(categoryTableModel::addRow);
    }
}
