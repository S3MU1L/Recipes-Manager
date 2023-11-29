package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Export;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.ui.MainWindow;
import cz.muni.fi.pv168.easyfood.ui.dialog.ExportDialog;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;
import cz.muni.fi.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public final class ExportAction extends AbstractAction {
    private final MainWindow mainWindow;
    private final TabContainer exportContainer;
    private final List<Recipe> recipes;
    private final List<Ingredient> ingredients;
    private final List<Category> categories;
    private final List<Unit> units;

    public ExportAction(
            MainWindow mainWindow,
            TabContainer exportContainer,
            List<Recipe> recipes,
            List<Ingredient> ingredients,
            List<Category> categories,
            List<Unit> units
    ) {
        super("Export", Icons.EXPORT_ICON);
        putValue(SHORT_DESCRIPTION, "Exports recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        this.mainWindow = mainWindow;
        this.exportContainer = exportContainer;

        this.recipes = recipes;
        this.ingredients = ingredients;
        this.categories = categories;
        this.units = units;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = (ExportDialog) exportContainer.getSelectedTab().getDialog().createNewDialog(new Export(), recipes, ingredients, categories, units);
        dialog.show(exportContainer.getComponent(), "Export").orElse(null);
    }
}
