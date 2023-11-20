package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.MainWindow;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class AddAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    private final MainWindow mainWindow;

    public AddAction(MainWindow mainWindow, TabContainer tabContainer, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        super("Add", Icons.ADD_ICON);
        this.mainWindow = mainWindow;
        this.tabContainer = tabContainer;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var table = tabContainer.getSelectedTab().getTable();
        var model = tabContainer.getSelectedTab().getModel();

        StringBuilder title = new StringBuilder("Add ").append(tabContainer.getSelectedTab().getTitle());
        title.deleteCharAt(title.length() - 1);

        var dialog = tabContainer.getSelectedTab().getDialog().createNewDialog(ingredients, categories, units);
        var result = dialog.show(table, title.toString());

        result.ifPresent(recipe -> {
            model.addRow(recipe);
            mainWindow.updateRecipeCountLabel();
        });
    }
}
