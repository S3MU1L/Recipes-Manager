package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;

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
        add("*Name:", nameField);
        add("*Abbreviation", abbreviationField);
        add("*Base Unit: ", baseUnitField);
        add("*Amount in Base Unit", conversionRatio);
    }

    @Override
    public Unit getEntity() {
        unit.setName(nameField.getText().trim());
        unit.setAbbreviation(abbreviationField.getText().trim());
        unit.setBaseUnit((BaseUnit) baseUnitField.getSelectedItem());
        unit.setConversion((double) conversionRatio.getValue());
        return unit;
    }

    @Override
    public boolean valid(Unit unit) {
        StringBuilder stringBuilder = new StringBuilder();

        if (unit.getName().trim().equals("")) {
            stringBuilder.append("Please enter a valid name\n\n");
        }
        if (!units.stream().filter(unit1 -> !unit1.getGuid().equals(unit.getGuid()) && unit1.getName().equals(unit.getName())).toList()
                  .isEmpty()) {
            stringBuilder.append("Duplicate name: ").append(unit.getName()).append("\n\n");
        }
        if (unit.getAbbreviation().equals("")) {
            stringBuilder.append("Please enter a valid abbreviation\n\n");
        }
        if (unit.getConversion() == 0) {
            stringBuilder.append("Conversion can't be zero\n\n");
        }


        if (stringBuilder.isEmpty()){
            return true;
        }
        JOptionPane.showMessageDialog(null, stringBuilder.toString(), "Unit Dialog Error", ERROR_MESSAGE, null);
        return false;
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

}
