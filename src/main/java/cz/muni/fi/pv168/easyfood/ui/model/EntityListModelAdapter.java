package cz.muni.fi.pv168.easyfood.ui.model;

import javax.swing.AbstractListModel;
import javax.swing.event.TableModelEvent;

/**
 * The {@link EntityListModelAdapter} is an adaptation for table, so it can be used as a list.
 *
 * @param <E> The type of entity stored in the list in the table.
 * */
public class EntityListModelAdapter<E> extends AbstractListModel<E> {

    private final EntityTableModel<E> entityTableModel;

    public EntityListModelAdapter(EntityTableModel<E> entityTableModel) {
        this.entityTableModel = entityTableModel;
        entityTableModel.addTableModelListener(this::handleTableModelEvent);
    }

    @Override
    public int getSize() {
        return entityTableModel.getRowCount();
    }

    @Override
    public E getElementAt(int index) {
        return entityTableModel.getEntity(index);
    }

    private void handleTableModelEvent(TableModelEvent event) {
        switch (event.getType()) {
            case TableModelEvent.INSERT -> this.fireIntervalAdded(this, event.getFirstRow(), event.getLastRow());
            case TableModelEvent.UPDATE -> this.fireContentsChanged(this, event.getFirstRow(), event.getLastRow());
            case TableModelEvent.DELETE -> this.fireIntervalRemoved(this, event.getFirstRow(), event.getLastRow());
            default -> {
                // there should be no other events
            }
        }
    }
}
