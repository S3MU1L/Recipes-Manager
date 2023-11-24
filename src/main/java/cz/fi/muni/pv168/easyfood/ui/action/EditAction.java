package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class EditAction extends AbstractAction {

    private final TabContainer tabContainer;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    public EditAction(TabContainer tabContainer, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        super("Edit", Icons.EDIT_ICON);
        this.tabContainer = tabContainer;
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
        title.deleteCharAt(title.length() - 1);
        var entity = tabContainer.getSelectedTab().getModel().getEntity(modelRow);
        var dialog = tabContainer.getSelectedTab().getDialog().createNewDialog(entity, ingredients, categories, units);
        var result = dialog.show(table, title.toString());

        result.ifPresent(model::updateRow);
    }
}
