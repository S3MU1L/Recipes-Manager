package cz.muni.fi.pv168.easyfood.ui.action;


import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ExportAction extends AbstractAction {

    public ExportAction() {
        super("Export", Icons.EXPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Exports recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
