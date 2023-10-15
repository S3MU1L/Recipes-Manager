package cz.fi.muni.pv168.easyfood.ui.action;

import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author Tibor Pelegrin
 */
public class AddCategoryAction extends AbstractAction {
    public AddCategoryAction(JTable categoryTable) {
        super("Add Category", Icons.ADD_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new category");
        putValue(MNEMONIC_KEY, KeyEvent.VK_C);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl C"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
