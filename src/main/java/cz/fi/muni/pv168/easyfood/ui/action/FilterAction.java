package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Filter;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class FilterAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    public FilterAction(TabContainer tabContainer, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        super("Filter", Icons.FILTER_ICON);
        this.tabContainer = tabContainer;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        putValue(SHORT_DESCRIPTION, "Filter recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = tabContainer.getSelectedTab().getDialog().createNewDialog(Filter.createEmptyFilter(), ingredients, categories, units);
        //var model = tabContainer.getSelectedTab().getModel();
        dialog.show(tabContainer.getComponent(), "Filter");
    }
}
