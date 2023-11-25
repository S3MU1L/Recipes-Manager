package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.awt.Component;

/**
 * The {@link EntityTableModel} interface provides an ability to get the actual entity at a certain index.
 *
 * @param <E> The entity type which the table keeps in a list.
 */
public interface EntityTableModel<E> extends TableModel {

    /**
     * Gets the entity at a certain index.
     *
     * @param rowIndex The index of the requested entity
     * @throws IndexOutOfBoundsException in case the rowIndex is less than zero or greater or equal
     *                                   than number of items in the table
     */
    E getEntity(int rowIndex);

    default void customizeTableCell(Component cell, Object value, int row, JTable table) {
    }

    default void customizeTable(JTable table) {

    }

    void addRow(E entity);

    void updateRow(E entity);

    void deleteRow(int modelRow);
}
