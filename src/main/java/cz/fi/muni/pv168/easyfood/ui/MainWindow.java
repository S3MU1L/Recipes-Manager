package cz.fi.muni.pv168.easyfood.ui;


import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.action.AddIngredientAction;
import cz.fi.muni.pv168.easyfood.ui.action.AddRecipeAction;
import cz.fi.muni.pv168.easyfood.ui.action.DeleteAction;
import cz.fi.muni.pv168.easyfood.ui.action.EditAction;
import cz.fi.muni.pv168.easyfood.ui.action.QuitAction;
import cz.fi.muni.pv168.easyfood.ui.action.ShowAction;
import cz.fi.muni.pv168.easyfood.ui.model.RecipeTableModel;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MainWindow {

    private final JFrame frame;

    private final Action quitAction = new QuitAction();
    private final Action addRecipeAction;
    private final Action addIngredientAction;
    private final Action showAction;
    private final Action deleteAction;
    private final Action editAction;

    public MainWindow() {
        frame = createFrame();
        var testDataGenerator = new TestDataGenerator();
        var recipeTable = createRecipeTable(testDataGenerator.createTestRecipes(10));
        addRecipeAction = new AddRecipeAction(recipeTable, testDataGenerator);
        addIngredientAction = new AddIngredientAction();
        deleteAction = new DeleteAction(recipeTable);
        editAction = new EditAction(recipeTable);
        showAction = new ShowAction(recipeTable);
        recipeTable.setComponentPopupMenu(createRecipeTablePopupMenu());
        frame.add(new JScrollPane(recipeTable), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Easy food");
        frame.setMinimumSize(new Dimension(400, 500));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) (screenSize.getWidth() - frame.getWidth()) / 2;
        int centerY = (int) (screenSize.getHeight() - frame.getHeight()) / 2;
        frame.setLocation(centerX, centerY);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JTable createRecipeTable(List<Recipe> recipes) {
        var model = new RecipeTableModel(recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        return table;
    }

    private JPopupMenu createRecipeTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.add(deleteAction);
        menu.add(editAction);
        menu.add(addRecipeAction);
        menu.add(showAction);
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('o');
        optionsMenu.add(addRecipeAction);
        optionsMenu.add(addIngredientAction);
        optionsMenu.addSeparator();
        optionsMenu.add(quitAction);
        menuBar.add(optionsMenu);
        return menuBar;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.add(addRecipeAction);
        toolbar.addSeparator();
        toolbar.add(addIngredientAction);
        toolbar.addSeparator();
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.add(showAction);
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() >= 1);
    }

}
