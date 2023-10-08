package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddIngredientAction extends AbstractAction {

    public AddIngredientAction(JTable ingredientTable) {
        super("Add Ingredient", Icons.ADD_INGREDIENT_ICON);
        putValue(SHORT_DESCRIPTION, "Adds new ingredient");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
