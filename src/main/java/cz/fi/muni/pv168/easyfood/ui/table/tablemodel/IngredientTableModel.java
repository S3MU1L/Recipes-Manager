package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;


import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import java.util.List;

public class IngredientTableModel extends ServiceTableModel<Ingredient> {

    private static final I18N I18N = new I18N(IngredientTableModel.class);

    private static final List<Column<?, Ingredient>> DEBUG_COLUMNS = List.of(
        Column.readOnly("ID", Long.class, Ingredient::getID)
    );
    private static final List<Column<?, Ingredient>> COLUMNS = List.of(
        Column.readOnly(I18N.getString("name"), String.class, Ingredient::getName),
        Column.readOnly(I18N.getString("calories"), String.class, Ingredient::getFormattedCalories)
    );

    public IngredientTableModel(Service<Ingredient> service) {
        super(DEBUG_COLUMNS, COLUMNS, service);
    }
}
