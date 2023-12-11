package cz.muni.fi.pv168.easyfood.ui.action;


import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class EditAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    public EditAction(TabContainer tabContainer,
                      List<Recipe> recipes,
                      List<Ingredient> ingredients,
                      List<Category> categories,
                      List<Unit> units) {
        super("Edit", Icons.EDIT_ICON);
        this.tabContainer = tabContainer;
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        setEnabled(false);
        putValue(SHORT_DESCRIPTION, "Edits selected row");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        var table = tabContainer.getSelectedTab().getTable();
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (table.isEditing()) {
            table.getCellEditor().cancelCellEditing();
        }

        var model = tabContainer.getSelectedTab().getModel();
        int modelRow = table.convertRowIndexToModel(selectedRows[0]);
        StringBuilder title = new StringBuilder("Edit ").append(tabContainer.getSelectedTab().getTitle());
        var entity = tabContainer.getSelectedTab().getModel().getEntity(modelRow);
        var dialog = tabContainer.getSelectedTab().getDialog().createNewDialog(entity, recipes, ingredients, categories, units);
        var result = dialog.show(table, title.toString());

        result.ifPresentOrElse(entityToEdit -> model.updateRow(entity), model::updateAll);

    }
}
