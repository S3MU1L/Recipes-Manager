package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.ui.I18N;

import java.util.List;

public class ShoppingListTableModel extends AbstractEntityTableModel{

    private static final cz.fi.muni.pv168.easyfood.ui.I18N I18N = new I18N(IngredientAndAmountTableModel.class);

    private static final List<Column<?, IngredientAndAmount>> DEBUG_COLUMNS = List.of(
            Column.readOnly("ID", Long.class, IngredientAndAmount::getID)
    );
    private static final List<Column<?, IngredientAndAmount>> COLUMNS = List.of(
            Column.readOnly(I18N.getString("name"), String.class, IngredientAndAmount::getName),
            Column.readOnly(I18N.getString("amount"), String.class, IngredientAndAmount::getFormattedAmount)
    );

    private final List<IngredientAndAmount> ingredientAndAmountList;

    public ShoppingListTableModel(List<IngredientAndAmount> ingredientAndAmountList) {
        super(DEBUG_COLUMNS, COLUMNS);
        this.ingredientAndAmountList = ingredientAndAmountList;
    }

    @Override
    protected Object getEntity(int rowIndex) {
        return ingredientAndAmountList.get(rowIndex);
    }

    @Override
    protected void updateEntity(Object entity) {
        int index = ingredientAndAmountList.indexOf((IngredientAndAmount) entity);
        if (index != -1) {
            ingredientAndAmountList.set(index, (IngredientAndAmount) entity);
        }
    }

    @Override
    public int getRowCount() {
        return ingredientAndAmountList.size();
    }
}
