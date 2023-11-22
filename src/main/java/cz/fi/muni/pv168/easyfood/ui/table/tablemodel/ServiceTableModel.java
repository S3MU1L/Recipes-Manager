package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import cz.fi.muni.pv168.easyfood.service.Service;

import java.util.Collections;
import java.util.List;

public class ServiceTableModel<E> extends AbstractEntityTableModel<E> {

    private final Service<E> service;

    public ServiceTableModel(List<Column<?, E>> debugColumns, List<Column<?, E>> columns, Service<E> service) {
        super(debugColumns, columns);
        this.service = service;
    }

    @Override
    public E getEntity(int rowIndex) {
        return service.getEntityList().get(rowIndex);
    }

    @Override
    protected void updateEntity(E entity) {
        service.update(entity);
    }

    @Override
    public int getRowCount() {
        return service.getEntityList().size();
    }

    public void addRow() {
        fireTableRowsInserted(service.getEntityList().size() - 1, service.getEntityList().size() - 1);
    }

    public void deleteRow(int rowIndex) {
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void updateRow(int rowIndex) {
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public int getRowIndex(E entity) {
        return service.getEntityList().indexOf(entity);
    }

    public void updateAllRows() {
        fireTableDataChanged();
    }

    public List<E> getEntities() {
        return Collections.unmodifiableList(service.getEntityList());
    }
}
