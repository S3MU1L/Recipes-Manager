package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.model.Export;
import cz.muni.fi.pv168.easyfood.business.model.Filter;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.dialog.ExportDialog;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.CategoryTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.IngredientTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.UnitTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class ExportAction extends AbstractAction {
    private final MainWindow mainWindow;
    private final TabContainer exportContainer;
    private final RecipeTableModel recipeTableModel;
    private final IngredientTableModel ingredientTableModel;
    private final CategoryTableModel categoryTableModel;
    private final UnitTableModel unitTableModel;

    public ExportAction(
            MainWindow mainWindow,
            TabContainer exportContainer,
            RecipeTableModel recipeTableModel,
            CategoryTableModel categoryTableModel,
            IngredientTableModel ingredientTableModel,
            UnitTableModel unitTableModel
    ) {
        super("Export", Icons.EXPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Exports recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        this.mainWindow = mainWindow;
        this.exportContainer = exportContainer;

        this.recipeTableModel = recipeTableModel;
        this.ingredientTableModel = ingredientTableModel;
        this.categoryTableModel = categoryTableModel;
        this.unitTableModel = unitTableModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Filter filter = recipeTableModel.getActiveFiter();
        recipeTableModel.reset();
        var dialog = (ExportDialog) exportContainer.getSelectedTab().getDialog()
                                                   .createNewDialog(new Export(), recipeTableModel.getEntity(),
                                                                    ingredientTableModel.getEntity(),
                                                                    categoryTableModel.getEntity(),
                                                                    unitTableModel.getEntity());
        dialog.show(exportContainer.getComponent(), "Export").orElse(null);
        recipeTableModel.updateWithFilter(filter);
    }
}
