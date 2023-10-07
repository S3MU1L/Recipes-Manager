package cz.fi.muni.pv168.easyfood.ui.column;

import java.util.function.Function;

public class UneditableColumn<T, E> extends Column<T, E> {

    public UneditableColumn(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        super(columnName, columnClass, valueGetter);
    }

    @Override
    public void setValue(Object value, E entity) {
        throw new UnsupportedOperationException("Column " + getName() + " is not editable!");
    }

    @Override
    public boolean isEditable() {
        return false;
    }
}
