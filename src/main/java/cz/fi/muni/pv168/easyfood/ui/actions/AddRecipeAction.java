package cz.fi.muni.pv168.easyfood.ui.actions;

import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddRecipeAction extends AbstractAction {

    private static final I18N I18N = new I18N(AddRecipeAction.class);

    private final Service<Recipe> recipeService;

    public AddRecipeAction(Service<Recipe> recipeService) {
        super(I18N.getString("title"), Icons.ADD_ICON);
        this.recipeService = recipeService;
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_R);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl R"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        recipeService.openAddWindow();
    }
}
