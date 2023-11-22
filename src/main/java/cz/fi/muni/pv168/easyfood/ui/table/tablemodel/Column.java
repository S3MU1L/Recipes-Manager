package cz.fi.muni.pv168.easyfood.ui.table.tablemodel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Column<T, E> {
    private final String columnName;
    private final Class<T> columnClass;
    private final Function<E, T> valueGetter;

    public Column(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        this.columnName = Objects.requireNonNull(columnName);
        this.columnClass = Objects.requireNonNull(columnClass);
        this.valueGetter = Objects.requireNonNull(valueGetter);
    }

    static <T, E> Column<T, E> editable(String columnName, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        return new EditableColumn<>(columnName, columnClass, valueGetter, Objects.requireNonNull(valueSetter));
    }

    static <T, E> Column<T, E> readOnly(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        return new ReadOnlyColumn<>(columnName, columnClass, valueGetter);
    }

    String getColumnName() {
        return columnName;
    }

    Class<T> getColumnClass() {
        return columnClass;
    }

    Object getValue(E entity) {
        return valueGetter.apply(entity);
    }

    abstract void setValue(Object value, E entity);

    abstract boolean isEditable();

}
