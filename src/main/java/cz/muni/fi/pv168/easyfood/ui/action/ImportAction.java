package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Import;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.dialog.ImportDialog;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class ImportAction extends AbstractAction {
    private final MainWindow mainWindow;
    private final TabContainer importContainer;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;
    private final AbstractTableModel[] tableModels;

    public ImportAction(
            MainWindow mainWindow,
            TabContainer importContainer,
            List<Recipe> recipes,
            List<Ingredient> ingredients,
            List<Category> categories,
            List<Unit> units,
            AbstractTableModel[] tableModels
    ) {
        super("Import", Icons.IMPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Import recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_M);

        this.mainWindow = mainWindow;
        this.importContainer = importContainer;
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
        this.tableModels = tableModels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = (ImportDialog) importContainer.getSelectedTab().getDialog().createNewDialog(new Import(), recipes, ingredients, categories, units);
        dialog.show(importContainer.getComponent(), "Import").orElse(null);
        for (AbstractTableModel tableModel : tableModels) tableModel.fireTableDataChanged();
    }
}
