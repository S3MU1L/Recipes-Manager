package cz.fi.muni.pv168.easyfood.ui;


import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.model.Category;
import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.ui.action.AddCategoryAction;
import cz.fi.muni.pv168.easyfood.ui.action.AddIngredientAction;
import cz.fi.muni.pv168.easyfood.ui.action.AddRecipeAction;
import cz.fi.muni.pv168.easyfood.ui.action.DeleteAction;
import cz.fi.muni.pv168.easyfood.ui.action.EditAction;
import cz.fi.muni.pv168.easyfood.ui.action.ExportAction;
import cz.fi.muni.pv168.easyfood.ui.action.FilterAction;
import cz.fi.muni.pv168.easyfood.ui.action.ImportAction;
import cz.fi.muni.pv168.easyfood.ui.action.QuitAction;
import cz.fi.muni.pv168.easyfood.ui.action.ShowAction;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.CategoryTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.IngredientTableModel;
import cz.fi.muni.pv168.easyfood.ui.tablemodel.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MainWindow {
    int currentTabIndex = 0;
    private final JFrame frame;
    private final QuitAction quitAction = new QuitAction();
    private final AddRecipeAction addRecipeAction;
    private final AddIngredientAction addIngredientAction;
    private final AddCategoryAction addCategoryAction;
    private final ShowAction showAction;
    private final DeleteAction deleteRecipeAction;
    private final EditAction editAction;
    private final FilterAction filterAction;
    private final ExportAction exportAction;
    private final ImportAction importAction;
    private Tab ingredientTab;
    private Tab recipeTab;
    private Tab categoryTab;
    private final JTable ingredientTable;
    private final JTable recipeTable;
    private final JTable categoryTable;

    public MainWindow() {
        frame = createFrame();
        var testDataGenerator = new TestDataGenerator();
        List<Recipe> recipes = testDataGenerator.createTestRecipes(10);
        List<Ingredient> ingredients = testDataGenerator.createTestIngredients(10);
        List<Category> categories = testDataGenerator.createTestCategories(10);
        recipeTable = createRecipeTable(recipes);
        ingredientTable = createIngredientTable(ingredients);
        categoryTable = createCategoryTable(categories);

        TabContainer tabContainer = new TabContainer();
        tabContainer.addTab(recipeTab);
        tabContainer.addTab(ingredientTab);
        tabContainer.addTab(categoryTab);
        tabContainer.addChangeListener(this::tabChangeListener);

        addRecipeAction = new AddRecipeAction(recipeTable, testDataGenerator);

        addIngredientAction = new AddIngredientAction(tabContainer.getSelectedTab().getTable());
        addCategoryAction = new AddCategoryAction(categoryTable);
        deleteRecipeAction = new DeleteAction(tabContainer);
        editAction = new EditAction(tabContainer.getSelectedTab().getTable());
        showAction = new ShowAction(tabContainer.getSelectedTab().getTable());
        filterAction = new FilterAction();
        importAction = new ImportAction();
        exportAction = new ExportAction();

        recipeTable.setComponentPopupMenu(createRecipeTablePopupMenu());
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

    private JTable createRecipeTable(List<Recipe> recipes) {
        var model = new RecipeTableModel(recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        recipeTab = new Tab("recipes", table, model);
        return table;
    }

    private JTable createIngredientTable(List<Ingredient> ingredients) {
        var model = new IngredientTableModel(ingredients);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        ingredientTab = new Tab("ingredients", table, model);
        return table;
    }
    private JTable createCategoryTable(List<Category> categories) {
        var model = new CategoryTableModel(categories);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        categoryTab = new Tab("categories", table, model);
        return table;
    }

    private JPopupMenu createRecipeTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.add(deleteRecipeAction);
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
        optionsMenu.add(addCategoryAction);
        optionsMenu.addSeparator();
        optionsMenu.add(deleteRecipeAction);
        optionsMenu.add(editAction);
        optionsMenu.add(filterAction);
        optionsMenu.add(importAction);
        optionsMenu.add(exportAction);
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
        toolbar.add(addCategoryAction);
        toolbar.addSeparator();
        toolbar.add(editAction);
        toolbar.add(deleteRecipeAction);
        toolbar.add(showAction);
        toolbar.add(filterAction);
        toolbar.add(importAction);
        toolbar.add(exportAction);
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
        deleteRecipeAction.setEnabled(selectionModel.getSelectedItemsCount() >= 1);
    }

}
