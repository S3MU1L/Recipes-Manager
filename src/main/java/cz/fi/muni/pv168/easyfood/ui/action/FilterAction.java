package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class FilterAction extends AbstractAction {

    public FilterAction() {
        super("Filter", Icons.FILTER_ICON);
        setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Filter recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
