package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JTable;
import java.awt.Component;
import java.util.List;

public class BaseUnitModel extends EntityTableModel<BaseUnit> {
    private final List<BaseUnit> baseUnits;

    public BaseUnitModel(List<BaseUnit> baseUnits) {
        super(List.of(
                Column.readOnly("Name", String.class, BaseUnit::toString),
                Column.readOnly("Abbreviation", String.class, BaseUnit::getAbbreviation)
        ));
        this.baseUnits = baseUnits;
    }

    @Override
    public BaseUnit getEntity(int rowIndex) {
        return baseUnits.get(rowIndex);
    }

    @Override
    protected void updateEntity(BaseUnit baseUnit) {

    }

    @Override
    public void addRow(BaseUnit baseUnit) {
        int newRowIndex = baseUnits.size();
        baseUnits.add(baseUnit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(BaseUnit baseUnit) {
        int rowIndex = baseUnits.indexOf(baseUnit);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, int row) {

    }

    @Override
    public void customizeTable(JTable table) {

    }

    @Override
    public int getRowCount() {
        return baseUnits.size();
    }

    @Override
    public void deleteRow(int rowIndex) {
        baseUnits.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}