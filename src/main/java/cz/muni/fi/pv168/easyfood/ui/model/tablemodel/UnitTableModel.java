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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class UnitTableModel extends AbstractTableModel implements EntityTableModel<Unit> {
    private List<Unit> units;
    private final IngredientTableModel ingredientTableModel;
    private final CrudService<Unit> unitCrudService;

    private final List<Column<Unit, ?>> columns = List.of(
            Column.readonly("Name", String.class, Unit::getName),
            Column.readonly("Abbreviation", String.class, Unit::getAbbreviation),
            Column.readonly("In Base Unit", String.class, Unit::getFormattedBaseUnit)
    );

    public UnitTableModel(CrudService<Unit> unitCrudService, IngredientTableModel ingredientTableModel,
                          List<Unit> units) {
        this.unitCrudService = unitCrudService;
        this.ingredientTableModel = ingredientTableModel;
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
    public List<Unit> getEntity() {
        return units;
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
        units = new ArrayList<>(unitCrudService.findAll());
    }


    public void deleteRows(int[] rowIndexes) {
        List<Unit> toDelete = Arrays.stream(rowIndexes).sequential().mapToObj(rowIndex -> units.get(rowIndex)).toList();
        StringBuilder stringBuilder = new StringBuilder();

        for (Unit unit : toDelete) {
            for (BaseUnit baseUnit : BaseUnit.values()) {
                if (unit.getName().equals(baseUnit.toString())) {
                    stringBuilder.append("Cannot delete Base Unit ").append(baseUnit).append("\n\n");
                }
            }
        }

        Map<Unit, List<Ingredient>> usedIn = new HashMap<>();
        for (Ingredient ingredient : ingredientTableModel.getEntity()) {
            Unit unit = ingredient.getUnit();

            if (toDelete.contains(unit)) {
                usedIn.computeIfAbsent(unit, k -> new ArrayList<>());
                usedIn.get(unit).add(ingredient);
            }
        }

        if (!usedIn.isEmpty()) {
            for (var entry : usedIn.entrySet()) {
                stringBuilder.append("Unable to delete Unit : ").append(entry.getKey().getName())
                             .append("\nIt is used in Ingredients:");
                for (Ingredient ingredient : entry.getValue()) {
                    stringBuilder.append(" ").append(ingredient.getName()).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                stringBuilder.append("\n\n");
            }
        }

        if (!stringBuilder.isEmpty()) {
            JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Error", ERROR_MESSAGE, null);
            return;
        }

        for (Unit unit : toDelete) {
            unitCrudService.deleteByGuid(unit.getGuid());
        }
        units.removeAll(toDelete);
        fireTableRowsDeleted(rowIndexes[0], rowIndexes[rowIndexes.length - 1]);
    }

}
