package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RemoveFilterAction extends AbstractAction {
    private final MainWindow mainWindow;
    private final TabContainer tabContainer;
    public RemoveFilterAction(MainWindow mainWindow, TabContainer tabContainer){
        super("Add", Icons.FILTER_REMOVE_ICON);
        this.mainWindow = mainWindow;
        this.tabContainer = tabContainer;
        putValue(SHORT_DESCRIPTION, "Remove filter");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
        setEnabled(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        RecipeTableModel model = (RecipeTableModel) tabContainer.getSelectedTab().getModel();
        model.updateAll();
        model.setActiveFiter(false);
        model.fireTableDataChanged();
        mainWindow.updateRecipeCountLabel();
    }
}
