package cz.fi.muni.pv168.easyfood.ui.actions;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * AddIngredientAction class
 *
 * @author Tomáš Jančička
 */
public class AddIngredientAction extends AbstractAction {

    private static final I18N I18N = new I18N(AddIngredientAction.class);

    protected final Service<Ingredient> ingredientService;

    public AddIngredientAction(Service<Ingredient> ingredientService) {
        super(I18N.getString("title"), Icons.ADD_INGREDIENT_ICON);
        this.ingredientService = ingredientService;
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ingredientService.openAddWindow();
    }
}
