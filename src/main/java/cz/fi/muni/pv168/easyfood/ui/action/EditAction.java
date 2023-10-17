package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class EditAction extends AbstractAction {

    private final TabContainer tabContainer;

    public EditAction(TabContainer tabContainer) {
        super("Edit", Icons.EDIT_ICON);
        this.tabContainer = tabContainer;
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
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

        var model = tabContainer.getSelectedTab().getModel();
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        StringBuilder title = new StringBuilder("Edit ").append(tabContainer.getSelectedTab().getTitle());
        title.deleteCharAt(title.length() - 1);
        var dialog = tabContainer.getSelectedTab().getDialog().createNewDialog(tabContainer.getSelectedTab().getModel().getEntity(modelRow));
        dialog.show(table, title.toString()).ifPresent(model::updateRow);
    }
}
