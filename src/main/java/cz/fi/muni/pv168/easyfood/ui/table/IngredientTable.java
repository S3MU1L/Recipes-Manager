package cz.fi.muni.pv168.easyfood.ui.table;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.ServiceTableModel;

import javax.swing.*;
import java.util.List;

public class IngredientTable extends Table<Ingredient> {

    public IngredientTable(ServiceTableModel<Ingredient> tableModel, List<Action> editActions) {
        super(tableModel, editActions);
    }

    public IngredientTable(ServiceTableModel<Ingredient> tableModel) {
        super(tableModel);
    }
}
