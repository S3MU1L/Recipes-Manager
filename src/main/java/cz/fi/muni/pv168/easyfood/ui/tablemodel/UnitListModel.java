package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.*;
import java.util.List;

/**
 * @author Samuel Sabo
 */
public class UnitListModel extends AbstractListModel<Unit> {

    private final List<Unit> units;

    public UnitListModel(List<Unit> units) {
        this.units = units;
    }

    @Override
    public int getSize() {
        return units.size();
    }

    @Override
    public Unit getElementAt(int index) {
        return units.get(index);
    }
}
