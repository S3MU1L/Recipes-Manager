package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class QuitAction extends AbstractAction {

    public QuitAction() {
        super("Quit", Icons.QUIT_ICON);
        putValue(SHORT_DESCRIPTION, "Terminates the application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
