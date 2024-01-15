package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class RemoveFilterAction extends AbstractAction {
    private final RecipeTableModel recipeTableModel;
    private final MainWindow mainWindow;
    public RemoveFilterAction(MainWindow mainWindow, RecipeTableModel recipeTableModel){
        super("Add", Icons.FILTER_REMOVE_ICON);
        this.recipeTableModel = recipeTableModel;
        this.mainWindow = mainWindow;
        putValue(SHORT_DESCRIPTION, "Remove filter");
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
        setEnabled(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        recipeTableModel.reset();
        mainWindow.updateFilterStatus();
    }
}
