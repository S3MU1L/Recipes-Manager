package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.service.IngredientService;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.utility.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddIngredientWindow extends AbstractWindow {

    private static final I18N I18N = new I18N(AddIngredientWindow.class);

    private final IngredientService ingredientService;

    private final JLabel nameLabel = new JLabel(I18N.getString("name") +":");
    private final JLabel caloriesLabel = new JLabel(I18N.getString("calories") + ":");
    private final JLabel unitLabel = new JLabel(I18N.getString("unit") +":");
    private final JTextField nameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JComboBox unitComboBox = new JComboBox(Unit.values());

    public AddIngredientWindow(IngredientService ingredientService, JFrame parentFrame){
        super(I18N.getString("title"), parentFrame);
        this.ingredientService = ingredientService;

        frame.setMinimumSize(new Dimension(500, 150));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);

        JButton button = new JButton(I18N.getString("addIngredientButton"));
        button.addActionListener(this::addListener);
        mainPanel.add(button, BorderLayout.SOUTH);

        Util.setAcceptNumbersOnly(caloriesField);

        frame.add(mainPanel);
        frame.pack();
    }

    private JPanel createGridPanel(){
        JPanel gridPanel = new JPanel(new GridLayout(3,2));
        gridPanel.add(nameLabel);
        gridPanel.add(nameField);
        gridPanel.add(caloriesLabel);
        gridPanel.add(caloriesField);
        gridPanel.add(unitLabel);
        gridPanel.add(unitComboBox);
        return gridPanel;
    }

    private void addListener(ActionEvent event) {
        String name = nameField.getText();
        if (name.isEmpty()) {
            WindowsManager.openWindow(new InfoWindow(I18N.getString("invalidName") , parentFrame));
            return;
        }
        double calories;
        try {
            calories = Double.parseDouble(caloriesField.getText());
        } catch (NumberFormatException e) {
            WindowsManager.openWindow(
                    new InfoWindow(I18N.getString("inValidCaloriesFormat"), parentFrame));
            return;
        }
        Unit unit = (Unit) unitComboBox.getItemAt(unitComboBox.getSelectedIndex());
        ingredientService.add(new Ingredient(name, calories, unit));
        frame.dispose();
    }
}
