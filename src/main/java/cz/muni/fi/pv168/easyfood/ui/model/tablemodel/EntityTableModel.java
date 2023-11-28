package cz.muni.fi.pv168.easyfood.ui.model.tablemodel;

import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.util.List;

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
    List<E> getEntity();

    default void customizeTableCell(Component cell, Object value, int row, JTable table) {
    }

    default void customizeTable(JTable table) {

    }

    void addRow(E entity);

    void updateRow(E entity);

    void updateAll();

    void deleteRow(int modelRow);

    default void addRow(IngredientWithAmount ingredient, Recipe recipe) {
    }

    default void deleteRow(int modelRow, Recipe recipe) {
    }
}
