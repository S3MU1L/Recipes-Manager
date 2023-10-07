package cz.fi.muni.pv168.easyfood.ui.column;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EditableColumn<T, E> extends Column<T, E> {
    private final BiConsumer<E, T> valueSetter;

    public EditableColumn(String columnName, Class<T> columnClass, Function<E, T> valueGetter, BiConsumer<E, T> valueSetter) {
        super(columnName, columnClass, valueGetter);
        this.valueSetter = valueSetter;
    }

    @Override
    public void setValue(Object value, E entity) {
        valueSetter.accept(entity, getColumnClass().cast(value));
    }

    @Override
    public boolean isEditable() {
        return true;
    }
}
