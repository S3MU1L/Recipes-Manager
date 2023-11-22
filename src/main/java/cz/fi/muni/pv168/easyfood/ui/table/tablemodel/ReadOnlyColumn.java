package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import java.util.function.Function;

public class ReadOnlyColumn<T, E> extends Column<T, E> {

    public ReadOnlyColumn(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        super(columnName, columnClass, valueGetter);
    }

    @Override
    void setValue(Object value, E entity) {
        throw new UnsupportedOperationException("Column " + getColumnName() + " is not editable!");
    }

    @Override
    boolean isEditable() {
        return false;
    }
}
