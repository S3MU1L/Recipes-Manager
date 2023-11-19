package cz.fi.muni.pv168.easyfood.ui.tablemodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class UnitTableModel extends EntityTableModel<Unit> {
    private final List<Unit> units;
    private final List<Ingredient> ingredients;

    public UnitTableModel(List<Unit> units, List<Ingredient> ingredients) {
        super(List.of(
                Column.readOnly("Name", String.class, Unit::getName),
                Column.readOnly("Abbreviation", String.class, Unit::getAbbreviation),
                Column.readOnly("In Base Unit", String.class, Unit::getFormattedBaseUnit)
        ));
        this.units = units;
        this.ingredients = ingredients;
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
        if (!units.stream().filter(unit1 -> unit1.getName().equals(unit.getName())).toList().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Duplicate name: " + unit.getName(), "Error", ERROR_MESSAGE, null);
            return;
        }

        int newRowIndex = units.size();
        units.add(unit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(Unit oldUnit, Unit newUnit) {
        if (!units.stream().filter(unit -> unit != oldUnit &&
                unit.getName().equals(newUnit.getName())).toList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + newUnit.getName(),
                    "Error", ERROR_MESSAGE, null);
            return;
        }
        units.set(units.indexOf(oldUnit), newUnit);
        int rowIndex = units.indexOf(oldUnit);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {

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
        Unit removedUnit = units.get(rowIndex);

        for (BaseUnit baseUnit : BaseUnit.values()) {
            if (removedUnit.getName().equals(baseUnit.toString())) {
                JOptionPane.showMessageDialog(null,
                        "Cannot delete Base Unit " + baseUnit, "Error", ERROR_MESSAGE, null);
                return;
            }
        }

        List<Ingredient> usedIn = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getUnit().equals(removedUnit)) {
                usedIn.add(ingredient);
            }
        }

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Unit : ").append(removedUnit.getName()).append("\nIt is used in Ingredients:");
            for (Ingredient ingredient : usedIn) {
                stringBuilder.append(" ").append(ingredient.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }

        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
