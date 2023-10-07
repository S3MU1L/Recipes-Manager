package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.model.RecipeTableModel;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

public final class DeleteAction extends AbstractAction {

    private final JTable recipeTable;

    public DeleteAction(JTable recipeTable) {
        super("Delete", Icons.DELETE_ICON);
        setEnabled(false);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Deletes selected row/s");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var recipeTableModel = (RecipeTableModel) recipeTable.getModel();
        Arrays.stream(recipeTable.getSelectedRows())
                .map(recipeTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .forEach(recipeTableModel::deleteRow);
    }
}
