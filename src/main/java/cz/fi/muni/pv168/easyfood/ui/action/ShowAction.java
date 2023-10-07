package cz.fi.muni.pv168.easyfood.ui.action;

import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ShowAction extends AbstractAction {

    private final JTable recipeTable;

    public ShowAction(JTable recipeTable) {
        super("Show", Icons.SHOW_ICON);
        this.recipeTable = recipeTable;
        putValue(SHORT_DESCRIPTION, "Show the details of a recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
