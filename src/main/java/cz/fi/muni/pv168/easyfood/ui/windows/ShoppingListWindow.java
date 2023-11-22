package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.ShoppingListTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ShoppingListWindow extends AbstractWindow {

    private static final I18N I18N = new I18N(ShoppingListWindow.class);

    private final JTable table;

    public ShoppingListWindow(List<IngredientAndAmount> ingredientAndAmountList, JFrame parentFrame) {
        super(I18N.getString("title"), parentFrame);
        this.table = new JTable(new ShoppingListTableModel(ingredientAndAmountList));

        frame.setMinimumSize(new Dimension(300, 400));
        frame.add(new JScrollPane(table));
        frame.pack();
    }
}
