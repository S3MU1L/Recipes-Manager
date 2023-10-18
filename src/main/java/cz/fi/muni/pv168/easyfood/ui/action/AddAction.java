package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class AddAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    public AddAction(TabContainer tabContainer, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        super("Add", Icons.ADD_ICON);
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
        dialog.show(table, title.toString()).ifPresent(model::addRow);
    }
}
