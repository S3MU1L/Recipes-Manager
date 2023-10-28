package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static javax.swing.JOptionPane.*;

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
        int selectedRows = table.getSelectedRows().length;
        if (selectedRows < 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows);
        }

        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("Delete " + selectedRows + ((selectedRows == 1) ? " row?" : " rows?")));

        int result = JOptionPane.showConfirmDialog(table, jPanel, "Delete",
                YES_NO_OPTION, QUESTION_MESSAGE, null);
        if (result == OK_OPTION) {
            tabContainer.getSelectedTab().delete();
        }
    }
}

