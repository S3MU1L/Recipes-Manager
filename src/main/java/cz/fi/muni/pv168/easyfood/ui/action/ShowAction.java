package cz.fi.muni.pv168.easyfood.ui.action;

import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.dialog.ShowDialog;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ShowAction extends AbstractAction {
    private final TabContainer tabContainer;

    public ShowAction(TabContainer tabContainer) {
        super("Show", Icons.SHOW_ICON);
        this.tabContainer = tabContainer;
        putValue(SHORT_DESCRIPTION, "Show the details of a recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = tabContainer.getSelectedTab().getTable();
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }

        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        var dialog = new ShowDialog((Recipe) tabContainer.getSelectedTab().getModel().getEntity(modelRow));
        dialog.show(tabContainer.getComponent(), "Show");
    }
}
