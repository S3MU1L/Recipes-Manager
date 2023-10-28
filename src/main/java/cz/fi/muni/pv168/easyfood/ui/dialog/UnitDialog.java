package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.util.List;
import java.util.Objects;

public class UnitDialog  extends EntityDialog<Unit>{
    private final JTextField nameField = new JTextField();
    private final JTextField abbreviationField = new JTextField();
    private final JTextField conversionRatio = new JTextField();
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
        abbreviationField.setText(unit.getAbbreviation());
        conversionRatio.setText(unit.getFormattedConversionRatio());

        baseUnitField.removeAllItems();
        for (BaseUnit baseUnit : BaseUnit.values()) {
            baseUnitField.addItem(baseUnit);
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Abbreviation", abbreviationField);
        add("Base Unit: ", baseUnitField);
        add("Amount in Base Unit", conversionRatio);
    }
    @Override
    public Unit getEntity() {
        unit.setName(nameField.getText());
        unit.setAbbreviation(abbreviationField.getText());
        unit.setBaseUnit(BaseUnit.getBaseUnitFormSymbol(Objects.requireNonNull(baseUnitField.getSelectedItem()).toString()));
        unit.setConversion(Float.parseFloat(conversionRatio.getText()));
        return unit;
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
