package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.ui.model.Column;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class UnitTableModel extends AbstractTableModel implements EntityTableModel<Unit> {
    private List<Unit> units;
    private final List<Ingredient> ingredients;
    private final CrudService<Unit> unitCrudService;

    private final List<Column<Unit, ?>> columns = List.of(
            Column.readonly("Name", String.class, Unit::getName),
            Column.readonly("Abbreviation", String.class, Unit::getAbbreviation),
            Column.readonly("In Base Unit", String.class, Unit::getFormattedBaseUnit)
    );

    public UnitTableModel(CrudService<Unit> unitCrudService, List<Ingredient> ingredients, List<Unit> units) {
        this.unitCrudService = unitCrudService;
        this.ingredients = ingredients;
        this.units = units;
    }

    public int getSize() {
        return units.size();
    }

    @Override
    public Unit getEntity(int rowIndex) {
        return units.get(rowIndex);
    }

    @Override
    public void customizeTableCell(Component cell, Object value, int row, JTable table) {
        Object fstCol = table.getValueAt(row, 0);
        List<String> baseUnitsNames = Arrays.stream(BaseUnit.values()).map(BaseUnit::toString).toList();
        if (fstCol instanceof String) {
            Unit unit = findUnitByName((String) fstCol);
            if (baseUnitsNames.contains(unit.getName())) {
                cell.setBackground(Color.CYAN);
                cell.setForeground(Color.BLACK);
            }
        }
    }

    private Unit findUnitByName(String unitName) {
        return units.stream()
                .filter(category -> category.getName().equals(unitName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void customizeTable(JTable table) {
    }

    @Override
    public int getRowCount() {
        return units.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var unit = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(unit);
    }


    @Override
    public void addRow(Unit unit) {
        unitCrudService.create(unit)
                .intoException();
        int newRowIndex = units.size();
        units.add(unit);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    @Override
    public void updateRow(Unit unit) {
        unitCrudService.update(unit)
                .intoException();
        int rowIndex = units.indexOf(unit);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    @Override
    public void updateAll() {
        units = unitCrudService.findAll();
    }


    public void deleteRow(int rowIndex) {
        var toDelete = units.get(rowIndex);

        for (BaseUnit baseUnit : BaseUnit.values()) {
            if (toDelete.getName().equals(baseUnit.toString())) {
                JOptionPane.showMessageDialog(null,
                        "Cannot delete Base Unit " + baseUnit, "Error", ERROR_MESSAGE, null);
                return;
            }
        }

        List<Ingredient> usedIn = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getUnit().equals(toDelete)) {
                usedIn.add(ingredient);
            }
        }

        if (usedIn.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to delete Unit : ").append(toDelete.getName()).append("\nIt is used in Ingredients:");
            for (Ingredient ingredient : usedIn) {
                stringBuilder.append(" ").append(ingredient.getName()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }

        unitCrudService.deleteByGuid(toDelete.getGuid());
        units.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

}
