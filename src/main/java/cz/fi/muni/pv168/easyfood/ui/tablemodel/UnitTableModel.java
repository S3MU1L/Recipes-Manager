package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.List;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

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
        if (units.stream().filter(unit1 -> unit1.getName().equals(unit.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null,
                    "Unable to add Row -> Name <" + unit.getName() + "> duplicity", "Error", INFORMATION_MESSAGE, null);
            return;
        }

        int newRowIndex = units.size();
        units.add(unit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(Unit oldUnit, Unit newUnit) {
        if (units.stream().filter(unit -> unit != oldUnit &&
                unit.getName().equals(newUnit.getName())).toList().size() != 0) {
            JOptionPane.showMessageDialog(null, "Unable to edit Row -> Name <" + newUnit.getName() +
                    "> duplicity", "Error", INFORMATION_MESSAGE, null);
            return;
        }
        units.set(units.indexOf(oldUnit), newUnit);
        int rowIndex = units.indexOf(oldUnit);
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
