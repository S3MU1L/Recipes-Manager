package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.bussiness.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.util.List;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class UnitDialog extends EntityDialog<Unit> {
    private final JTextField nameField = new JTextField();
    private final JTextField abbreviationField = new JTextField();
    private final JSpinner conversionRatio = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Integer.MAX_VALUE, 0.5));
    private final JComboBox<BaseUnit> baseUnitField = new JComboBox<>();
    private final Unit unit;
    private final List<Unit> units;

    public UnitDialog(Unit unit, List<Unit> units) {
        this.unit = unit;
        this.units = units;
        setValues();
        addFields();
    }

    public UnitDialog(List<Unit> units) {
        this(Unit.createEmptyUnit(), units);
    }

    private void setValues() {
        nameField.setText(unit.getName());
        abbreviationField.setText(unit.getAbbreviation());
        conversionRatio.setValue(unit.getConversion());

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
        String name = nameField.getText().trim();
        String abbreviation = abbreviationField.getText().trim();
        BaseUnit baseUnit = (BaseUnit) baseUnitField.getSelectedItem();
        double conversion = (double) conversionRatio.getValue();
        return new Unit(name, abbreviation, baseUnit, conversion);
    }

    @Override
    public boolean valid(Unit unit) {
        if (unit.getName().equals("")) {
            JOptionPane.showMessageDialog(null, "Empty name", "Error", ERROR_MESSAGE, null);
            return false;
        }
        if (unit.getAbbreviation().equals("")) {
            JOptionPane.showMessageDialog(null, "Empty abbreviation", "Error", ERROR_MESSAGE, null);
            return false;
        }
        if (unit.getConversion() == 0) {
            JOptionPane.showMessageDialog(null, "Conversion can't be zero", "Error", ERROR_MESSAGE, null);
            return false;
        }
        if (!unit.getName().equals(this.unit.getName()) &&
                !units.stream().filter(unit1 -> unit1.getName().equals(unit.getName())).toList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Duplicate name: " + unit.getName(), "Error", ERROR_MESSAGE, null);
            return false;
        }
        return true;
    }

    @Override
    public EntityDialog<?> createNewDialog(List<Recipe> recipes, List<Ingredient> ingredients,
                                           List<Category> categories, List<Unit> units) {
        return new UnitDialog(units);
    }

    @Override
    public EntityDialog<Unit> createNewDialog(Unit unit, List<Recipe> recipes, List<Ingredient> ingredients,
                                              List<Category> categories, List<Unit> units) {
        return new UnitDialog(unit, units);
    }

    /*@Override
    public void changeEntityInDialog(Unit entity) {

    }*/
}
