package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RemoveFilterAction extends AbstractAction {
    private final MainWindow mainWindow;
    private final RecipeTableModel recipeTableModel;
    public RemoveFilterAction(MainWindow mainWindow, TabContainer tabContainer, RecipeTableModel recipeTableModel){
        super("Add", Icons.FILTER_REMOVE_ICON);
        this.mainWindow = mainWindow;
        this.recipeTableModel = recipeTableModel;
        putValue(SHORT_DESCRIPTION, "Remove filter");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
        setEnabled(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        recipeTableModel.updateAll();
        recipeTableModel.setActiveFiter(false);
        recipeTableModel.fireTableDataChanged();
        mainWindow.updateRecipeCountLabel();
    }
}
