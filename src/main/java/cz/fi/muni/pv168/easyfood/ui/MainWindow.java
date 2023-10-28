package cz.fi.muni.pv168.easyfood.ui;


import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;
import cz.fi.muni.pv168.easyfood.ui.action.AddAction;
import cz.fi.muni.pv168.easyfood.ui.action.DeleteAction;
import cz.fi.muni.pv168.easyfood.ui.action.EditAction;
import cz.fi.muni.pv168.easyfood.ui.action.ExportAction;
import cz.fi.muni.pv168.easyfood.ui.action.FilterAction;
import cz.fi.muni.pv168.easyfood.ui.action.ImportAction;
import cz.fi.muni.pv168.easyfood.ui.action.QuitAction;
import cz.fi.muni.pv168.easyfood.ui.action.ShowAction;
import cz.fi.muni.pv168.easyfood.ui.dialog.CategoryDialog;
import cz.fi.muni.pv168.easyfood.ui.dialog.FilterDialog;
import cz.fi.muni.pv168.easyfood.ui.dialog.IngredientDialog;
import cz.fi.muni.pv168.easyfood.ui.dialog.RecipeDialog;
import cz.fi.muni.pv168.easyfood.ui.dialog.UnitDialog;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.CategoryTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.IngredientTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.RecipeTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.UnitTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MainWindow {
    int currentTabIndex = 0;
    private final JFrame frame;
    private final QuitAction quitAction = new QuitAction();
    private final AddAction addAction;
    private final ShowAction showAction;
    private final DeleteAction deleteAction;
    private final EditAction editAction;
    private final FilterAction filterAction;
    private final ExportAction exportAction;
    private final ImportAction importAction;
    private Tab ingredientTab;
    private Tab recipeTab;
    private Tab categoryTab;
    private Tab unitTab;
    private final JTable ingredientTable;
    private final JTable recipeTable;
    private final JTable categoryTable;
    private final JTable unitTable;

    public MainWindow() {
        frame = createFrame();
        var testDataGenerator = new TestDataGenerator();
        List<Recipe> recipes = testDataGenerator.createTestRecipes(10);
        List<Ingredient> ingredients = testDataGenerator.createTestIngredients(10);
        List<Category> categories = testDataGenerator.createTestCategories(10);
        List<Unit> units = testDataGenerator.createTestUnits(10);
        recipeTable = createRecipeTable(recipes, ingredients, categories);
        ingredientTable = createIngredientTable(ingredients, units);
        categoryTable = createCategoryTable(categories, recipes);
        unitTable = createUnitTable(units);

        TabContainer tabContainer = new TabContainer();
        tabContainer.addTab(recipeTab);
        tabContainer.addTab(ingredientTab);
        tabContainer.addTab(categoryTab);
        tabContainer.addTab(unitTab);
        tabContainer.addChangeListener(this::tabChangeListener);

        TabContainer filterContainer = new TabContainer();
        var model = new RecipeTableModel(recipes);
        var table = new JTable(model);
        Tab filterTab = new Tab("Filter", table, model, new FilterDialog(categories, ingredients));
        filterContainer.addTab(filterTab);

        addAction = new AddAction(tabContainer, ingredients, categories, units);
        deleteAction = new DeleteAction(tabContainer);
        editAction = new EditAction(tabContainer, ingredients, categories, units);
        showAction = new ShowAction(tabContainer);
        filterAction = new FilterAction(filterContainer, ingredients, categories, units);
        importAction = new ImportAction();
        exportAction = new ExportAction();

        recipeTable.setComponentPopupMenu(createExtendedTablePopupMenu());
        ingredientTable.setComponentPopupMenu(createBasicTablePopupMenu());
        categoryTable.setComponentPopupMenu(createBasicTablePopupMenu());
        unitTable.setComponentPopupMenu(createBasicTablePopupMenu());

        frame.add(tabContainer.getComponent(), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
    }

    private void tabChangeListener(ChangeEvent changeEvent) {
        var tabbedPane = (JTabbedPane) changeEvent.getSource();
        currentTabIndex = tabbedPane.getSelectedIndex();
        ingredientTable.clearSelection();
        recipeTable.clearSelection();
        categoryTable.clearSelection();
        unitTable.clearSelection();
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Easy food");
        frame.setMinimumSize(new Dimension(450, 500));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) (screenSize.getWidth() - frame.getWidth()) / 2;
        int centerY = (int) (screenSize.getHeight() - frame.getHeight()) / 2;
        frame.setLocation(centerX, centerY);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JTable createRecipeTable(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories) {
        var model = new RecipeTableModel(recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        recipeTab = new Tab("recipes", table, model, new RecipeDialog(Recipe.createEmptyRecipe(), ingredients, categories));
        return table;
    }

    private JTable createIngredientTable(List<Ingredient> ingredients, List<Unit> units) {
        var model = new IngredientTableModel(ingredients);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        ingredientTab = new Tab("ingredients", table, model, new IngredientDialog(units));
        return table;
    }

    private JTable createCategoryTable(List<Category> categories, List<Recipe> recipes) {
        var model = new CategoryTableModel(categories, recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        categoryTab = new Tab("categories", table, model, new CategoryDialog());
        return table;
    }

    private JTable createUnitTable(List<Unit> units) {
        var model = new UnitTableModel(units);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        unitTab = new Tab("units", table, model, new UnitDialog());
        return table;
    }

    private JPopupMenu createExtendedTablePopupMenu() {
        var menu = createBasicTablePopupMenu();
        menu.add(showAction);
        menu.add(filterAction);
        return menu;
    }

    private JPopupMenu createBasicTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.add(addAction);
        menu.add(deleteAction);
        menu.add(editAction);
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('o');
        optionsMenu.add(addAction);
        optionsMenu.add(deleteAction);
        optionsMenu.add(editAction);
        optionsMenu.add(filterAction);
        optionsMenu.add(quitAction);
        var fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');
        fileMenu.add(importAction);
        fileMenu.add(exportAction);
        menuBar.add(optionsMenu);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private JToolBar createToolbar() {
        var toolbar = new JToolBar();
        toolbar.add(addAction);
        toolbar.addSeparator();
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.add(showAction);
        toolbar.add(filterAction);
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() >= 1);
    }

}
