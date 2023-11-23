package cz.fi.muni.pv168.easyfood.ui.model.tablemodel;

import cz.fi.muni.pv168.easyfood.ui.column.Column;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class EntityTableModel<E> extends AbstractTableModel {
    private final List<Column<?, E>> columns;

    public EntityTableModel(List<Column<?, E>> columns) {
        List<Column<?, E>> cols = new ArrayList<>(columns);
        this.columns = Collections.unmodifiableList(cols);
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getColumn(columnIndex).getValue(getEntity(rowIndex));
    }

    public abstract E getEntity(int rowIndex);

    protected abstract void updateEntity(E entity);

    public void deleteRow(int rowIndex) {
        columns.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public abstract void addRow(E entity);

    public abstract void updateRow(E oldEntity, E newEntity);

    public abstract void customizeTableCell(Component cell, Object value, int row, JTable table);

    public abstract void customizeTable(JTable table);

    @Override
    public String getColumnName(int columnIndex) {
        return getColumn(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getColumn(columnIndex).getColumnClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return getColumn(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        E entity = getEntity(rowIndex);
        getColumn(columnIndex).setValue(value, entity);
        updateEntity(entity);
    }

    private Column<?, E> getColumn(int columnIndex) {
        return columns.get(columnIndex);
    }
}
