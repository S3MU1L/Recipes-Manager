package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.I18N;

import java.util.List;

public class RecipeTableModel extends ServiceTableModel<Recipe> {

    private static final I18N I18N = new I18N(RecipeTableModel.class);

    private static final List<Column<?, Recipe>> DEBUG_COLUMNS = List.of(
        Column.readOnly("ID", Long.class, Recipe::getID)
    );
    private static final List<Column<?, Recipe>> COLUMNS = List.of(
        Column.readOnly(I18N.getString("name"), String.class, Recipe::getName),
        Column.readOnly(I18N.getString("calories"), String.class, Recipe::getFormattedCalories),
        Column.readOnly(I18N.getString("preparationTime"), String.class, Recipe::getFormattedTime)
    );

    public RecipeTableModel(Service<Recipe> service) {
        super(DEBUG_COLUMNS, COLUMNS, service);
    }
}
