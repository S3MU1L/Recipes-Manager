package cz.fi.muni.pv168.easyfood.ui.table;

import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.ServiceTableModel;

import javax.swing.*;
import java.util.List;

public class IngredientAndAmountTable extends Table<IngredientAndAmount> {

    public IngredientAndAmountTable(ServiceTableModel<IngredientAndAmount> tableModel, List<Action> editActions) {
        super(tableModel, editActions);
    }

    public IngredientAndAmountTable(ServiceTableModel<IngredientAndAmount> tableModel) {
        super(tableModel);
    }
}
