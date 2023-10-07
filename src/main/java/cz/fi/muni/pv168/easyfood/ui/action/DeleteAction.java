package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.IngredientTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.RecipeTableModel;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public final class DeleteAction extends AbstractAction {
    private final TabContainer tabContainer;

    public DeleteAction(TabContainer tabContainer) {
        super("Delete", Icons.DELETE_ICON);
        setEnabled(false);
        this.tabContainer = tabContainer;
        putValue(SHORT_DESCRIPTION, "Deletes selected row/s");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = tabContainer.getSelectedTab().getTable();

        if (table.getModel() instanceof RecipeTableModel) {
            var tableModel = (RecipeTableModel) table.getModel();
            Arrays.stream(table.getSelectedRows())
                    .map(table::convertRowIndexToModel)
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .forEach(tableModel::deleteRow);
        }
        if (table.getModel() instanceof IngredientTableModel) {
            var tableModel = (IngredientTableModel) table.getModel();
            Arrays.stream(table.getSelectedRows())
                    .map(table::convertRowIndexToModel)
                    .boxed()
                    .sorted(Comparator.reverseOrder())
                    .forEach(tableModel::deleteRow);
        }
    }

}

