package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.RecipeTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public final class DeleteRecipeAction extends AbstractAction {
    private JTable table;

    public DeleteRecipeAction(JTable recipeTable) {
        super("Delete", Icons.DELETE_ICON);
        setEnabled(false);
        this.table = recipeTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected row/s");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var tableModel = (RecipeTableModel) table.getModel();
        Arrays.stream(table.getSelectedRows())
                .map(table::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(tableModel::deleteRow);
    }
}
