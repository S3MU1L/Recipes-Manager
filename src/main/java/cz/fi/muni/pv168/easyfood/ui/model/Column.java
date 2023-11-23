package cz.fi.muni.pv168.easyfood.ui.column;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Column<T, E> {
    private final String name;
    private final Class<T> columnClass;
    private final Function<E, T> valueGetter;

    public Column(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        this.name = Objects.requireNonNull(columnName);
        this.columnClass = Objects.requireNonNull(columnClass);
        this.valueGetter = Objects.requireNonNull(valueGetter);
    }

    public static <T, E> Column<T, E> editable(String columnName, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        return new EditableColumn<>(columnName, columnClass, valueGetter, Objects.requireNonNull(valueSetter));
    }

    public static <T, E> Column<T, E> readOnly(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        return new UneditableColumn<>(columnName, columnClass, valueGetter);
    }

    public String getName() {
        return name;
    }

    public Class<T> getColumnClass() {
        return columnClass;
    }

    public Object getValue(E entity) {
        return valueGetter.apply(entity);
    }

    public abstract void setValue(Object value, E entity);

    public abstract boolean isEditable();

}
