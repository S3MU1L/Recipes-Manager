package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.I18N;

import java.util.List;

public class IngredientAndAmountTableModel extends ServiceTableModel<IngredientAndAmount> {

    private static final I18N I18N = new I18N(IngredientAndAmountTableModel.class);

    private static final List<Column<?, IngredientAndAmount>> DEBUG_COLUMNS = List.of(
        Column.readOnly("ID", Long.class, IngredientAndAmount::getID)
    );
    private static final List<Column<?, IngredientAndAmount>> COLUMNS = List.of(
        Column.readOnly(I18N.getString("name"), String.class, IngredientAndAmount::getName),
        Column.readOnly(I18N.getString("calories"), String.class, IngredientAndAmount::getFormattedCalories),
        Column.readOnly(I18N.getString("amount"), String.class, IngredientAndAmount::getFormattedAmount)
    );

    public IngredientAndAmountTableModel(Service<IngredientAndAmount> service) {
        super(DEBUG_COLUMNS, COLUMNS, service);
    }
}
