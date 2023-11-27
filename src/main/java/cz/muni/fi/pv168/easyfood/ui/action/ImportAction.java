package cz.muni.fi.pv168.easyfood.ui.action;


import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ImportAction extends AbstractAction {

    public ImportAction() {
        super("Import", Icons.IMPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Import recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_M);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
