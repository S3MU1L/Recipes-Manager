package cz.muni.fi.pv168.easyfood.ui.model;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Column<E, T> {

    private final String name;
    private final Function<E, T> valueGetter;
    private final Class<T> columnType;

    private Column(String name, Class<T> columnClass, Function<E, T> valueGetter) {
        // see Item 49: Check parameters for validity
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.columnType = Objects.requireNonNull(columnClass, "column class cannot be null");
        this.valueGetter = Objects.requireNonNull(valueGetter, "value getter cannot be null");
    }

    // see Item 1: Consider static factory methods instead of constructors
    public static <E, T> Column<E, T> editable(String name, Class<T> columnClass, Function<E, T> valueGetter,
                                               BiConsumer<E, T> valueSetter) {
        return new Editable<>(name, columnClass, valueGetter, valueSetter);
    }

    // see Item 1: Consider static factory methods instead of constructors
    public static <E, T> Column<E, T> readonly(String name, Class<T> columnClass, Function<E, T> valueGetter) {
        return new ReadOnly<>(name, columnClass, valueGetter);
    }

    public abstract boolean isEditable();

    public abstract void setValue(Object value, E entity);

    public T getValue(E entity) {
        return valueGetter.apply(entity);
    }

    public String getName() {
        return name;
    }

    public Class<T> getColumnType() {
        return columnType;
    }

    private static class ReadOnly<E, T> extends Column<E, T> {

        private ReadOnly(String name, Class<T> columnClass, Function<E, T> valueGetter) {
            super(name, columnClass, valueGetter);
        }

        @Override
        public boolean isEditable() {
            return false;
        }

        @Override
        public void setValue(Object value, E entity) {
            throw new UnsupportedOperationException("Column '" + getName() + "' is not editable");
        }
    }

    private static class Editable<E, T> extends Column<E, T> {

        private final BiConsumer<E, T> valueSetter;

        private Editable(String name, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
            super(name, columnClass, valueGetter);
            this.valueSetter = Objects.requireNonNull(valueSetter, "value setter cannot be null");
        }

        @Override
        public boolean isEditable() {
            return true;
        }

        @Override
        public void setValue(Object value, E entity) {
            valueSetter.accept(entity, getColumnType().cast(value)); // see Item 33: Consider type-safe heterogeneous containers
        }
    }
}
