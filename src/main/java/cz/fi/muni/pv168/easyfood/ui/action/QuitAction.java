package cz.fi.muni.pv168.easyfood.ui.actions;

import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class QuitAction extends AbstractAction {

    private static final I18N I18N = new I18N(QuitAction.class);

    public QuitAction() {
        super(I18N.getString("title"), Icons.QUIT_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Q"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
