package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.util.List;

public class UnitDialog  extends EntityDialog<Unit>{
    private final JTextField nameField = new JTextField();
    private final JComboBox<BaseUnit> baseUnitField = new JComboBox<>();
    private final Unit unit;

    public UnitDialog(Unit unit) {
        this.unit = unit;
        setValues();
        addFields();
    }

    public UnitDialog() {
        this(Unit.createEmptyUnit());
    }

    private void setValues() {
        nameField.setText(unit.getName());

        baseUnitField.removeAllItems();
        for (BaseUnit baseUnit : BaseUnit.values()) {
            baseUnitField.addItem(baseUnit);
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Base Unit: ", baseUnitField);
    }
    @Override
    public Unit getEntity() {
        return null;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new UnitDialog();
    }

    @Override
    public EntityDialog<Unit> createNewDialog(Unit unit, List<Ingredient> ingredients, List<Category> categories, List<Unit> units) {
        return new UnitDialog(unit);
    }
}
