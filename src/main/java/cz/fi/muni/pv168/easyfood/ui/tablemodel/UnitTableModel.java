package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UnitTableModel extends EntityTableModel<Unit> {
    private final List<Unit> units;

    public UnitTableModel(List<Unit> units) {
        super(List.of(
                Column.readOnly("Name", String.class, Unit::getName),
                Column.readOnly("Abbreviation", String.class, Unit::getAbbreviation),
                Column.readOnly("In Base Unit", String.class, Unit::getFormattedBaseUnit)
        ));
        this.units = units;
    }

    @Override
    public Unit getEntity(int rowIndex) {
        return units.get(rowIndex);
    }

    @Override
    protected void updateEntity(Unit entity) {

    }

    @Override
    public void addRow(Unit unit) {
        int newRowIndex = units.size();
        units.add(unit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(Unit unit) {
        int rowIndex = units.indexOf(unit);
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
        return units.size();
    }

    @Override
    public void deleteRow(int rowIndex) {
        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
