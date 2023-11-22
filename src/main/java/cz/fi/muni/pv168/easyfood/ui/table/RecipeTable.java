package cz.fi.muni.pv168.easyfood.ui.table;

import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.ServiceTableModel;

import javax.swing.*;
import java.util.List;

public class RecipeTable extends Table<Recipe> {

    public RecipeTable(ServiceTableModel<Recipe> tableModel, List<Action> editActions) {
        super(tableModel, editActions);
    }

    public RecipeTable(ServiceTableModel<Recipe> tableModel) {
        super(tableModel);
    }
}
