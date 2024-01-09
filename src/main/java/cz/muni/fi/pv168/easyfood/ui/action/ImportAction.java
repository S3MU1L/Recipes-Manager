package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.model.Filter;
import cz.muni.fi.pv168.easyfood.business.model.Import;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.dialog.ImportDialog;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.CategoryTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.IngredientTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.UnitTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ImportAction extends AbstractAction {
    private final MainWindow mainWindow;
    private final TabContainer importContainer;
    private final RecipeTableModel recipeTableModel;
    private final IngredientTableModel ingredientTableModel;
    private final CategoryTableModel categoryTableModel;
    private final UnitTableModel unitTableModel;
    private final AbstractTableModel[] tableModels;

    public ImportAction(
            MainWindow mainWindow,
            TabContainer importContainer,
            RecipeTableModel recipeTableModel,
            CategoryTableModel categoryTableModel,
            IngredientTableModel ingredientTableModel,
            UnitTableModel unitTableModel
    ) {
        super("Import", Icons.IMPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Import recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_M);

        this.mainWindow = mainWindow;
        this.importContainer = importContainer;
        this.recipeTableModel = recipeTableModel;
        this.ingredientTableModel = ingredientTableModel;
        this.categoryTableModel = categoryTableModel;
        this.unitTableModel = unitTableModel;

        tableModels =
                new AbstractTableModel[]{recipeTableModel, ingredientTableModel, categoryTableModel, unitTableModel};
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Filter filter = recipeTableModel.getActiveFiter();
        recipeTableModel.reset();
        var dialog = (ImportDialog) importContainer.getSelectedTab().getDialog()
                                                   .createNewDialog(new Import(), recipeTableModel.getEntity(),
                                                                    ingredientTableModel.getEntity(),
                                                                    categoryTableModel.getEntity(),
                                                                    unitTableModel.getEntity());
        dialog.show(importContainer.getComponent(), "Import").orElse(null);
        for (AbstractTableModel tableModel : tableModels) {
            tableModel.fireTableDataChanged();
        }
        recipeTableModel.updateWithFilter(filter);
    }
}
