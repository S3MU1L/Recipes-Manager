package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.bussiness.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.util.List;

public class UnitDialog extends EntityDialog<Unit> {
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
        baseUnitField.setSelectedItem(unit.getBaseUnit());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Abbreviation", abbreviationField);
        add("Base Unit: ", baseUnitField);
        add("Amount in Base Unit", conversionRatio);
    }

    @Override
    public Unit getEntity() {
        String name = nameField.getText();
        String abbreviation = abbreviationField.getText();
        BaseUnit baseUnit = (BaseUnit) baseUnitField.getSelectedItem();
        double conversion = Double.parseDouble(conversionRatio.getText());
        return new Unit(name, abbreviation, baseUnit, conversion);
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
