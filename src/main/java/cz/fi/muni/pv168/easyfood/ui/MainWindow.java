package cz.fi.muni.pv168.easyfood.ui;


import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.bussiness.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
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
import cz.fi.muni.pv168.easyfood.ui.renderers.CustomTableCellRenderer;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.BaseUnitModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.CategoryTableModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.IngredientTableModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.UnitTableModel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
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
    private final TabContainer tabContainer;
    private Tab recipeTab;
    private Tab ingredientTab;
    private Tab categoryTab;
    private Tab unitTab;
    private final JTable ingredientTable;
    private final JTable recipeTable;
    private final JTable categoryTable;
    private final JTable unitTable;
    private JLabel recipeCountLabel = new JLabel();

    public MainWindow() {
        frame = createFrame();

        var testDataGenerator = new TestDataGenerator();
        List<Recipe> recipes = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Unit> units = new ArrayList<>();

        unitTable = createUnitTable(units, ingredients);
        ingredientTable = createIngredientTable(recipes, ingredients, units);
        categoryTable = createCategoryTable(categories, recipes);
        recipeTable = createRecipeTable(recipes, ingredients, categories);

        UnitTableModel unitModel = (UnitTableModel) unitTable.getModel();
        testDataGenerator.createTestUnits(3).forEach(unitModel::addRow);

        IngredientTableModel ingredientModel = (IngredientTableModel) ingredientTable.getModel();
        testDataGenerator.createTestIngredients(5, units).forEach(ingredientModel::addRow);

        CategoryTableModel categoryModel = (CategoryTableModel) categoryTable.getModel();
        testDataGenerator.createTestCategories(10).forEach(categoryModel::addRow);

        RecipeTableModel recipeModel = (RecipeTableModel) recipeTable.getModel();
        testDataGenerator.createTestRecipes(5, ingredients, categories).forEach(recipeModel::addRow);

        tabContainer = new TabContainer();
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

        addAction = new AddAction(this, tabContainer, ingredients, categories, units);
        deleteAction = new DeleteAction(this, tabContainer);
        editAction = new EditAction(tabContainer, ingredients, categories, units);
        showAction = new ShowAction(tabContainer);
        filterAction = new FilterAction(this, tabContainer, filterContainer, recipes, ingredients, categories, units);
        importAction = new ImportAction();
        exportAction = new ExportAction();

        recipeTable.setComponentPopupMenu(createExtendedTablePopupMenu());
        ingredientTable.setComponentPopupMenu(createBasicTablePopupMenu());
        categoryTable.setComponentPopupMenu(createBasicTablePopupMenu());
        unitTable.setComponentPopupMenu(createBasicTablePopupMenu());

        frame.add(tabContainer.getComponent(), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.add(createFooter(), BorderLayout.AFTER_LAST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.setSize(900, 600);
    }

    private void tabChangeListener(ChangeEvent changeEvent) {
        var tabbedPane = (JTabbedPane) changeEvent.getSource();
        currentTabIndex = tabbedPane.getSelectedIndex();
        ingredientTable.clearSelection();
        recipeTable.clearSelection();
        categoryTable.clearSelection();
        unitTable.clearSelection();
        filterAction.setEnabled(tabContainer.getSelectedTab().getModel().getClass().equals(RecipeTableModel.class));
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

    private JPanel createFooter() {
        var footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        recipeCountLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        updateRecipeCountLabel();
        recipeCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(recipeCountLabel, BorderLayout.CENTER);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return footerPanel;
    }

    private JTable createRecipeTable(List<Recipe> recipes, List<Ingredient> ingredients, List<Category> categories) {
        var model = new RecipeTableModel(recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(model));
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        recipeTab =
                new Tab("recipes", table, model, new RecipeDialog(Recipe.createEmptyRecipe(), ingredients, categories));
        return table;
    }

    private JTable createIngredientTable(List<Recipe> recipes, List<Ingredient> ingredients, List<Unit> units) {
        var model = new IngredientTableModel(ingredients, recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(model));
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        ingredientTab = new Tab("ingredients", table, model, new IngredientDialog(units));
        return table;
    }

    private JTable createCategoryTable(List<Category> categories, List<Recipe> recipes) {
        var model = new CategoryTableModel(categories, recipes);
        var table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(model));
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        categoryTab = new Tab("categories", table, model, new CategoryDialog());
        return table;
    }

    private JTable createUnitTable(List<Unit> units, List<Ingredient> ingredients) {
        var unitModel = new UnitTableModel(units, ingredients);
        var unitTable = new JTable(unitModel);
        unitTable.setAutoCreateRowSorter(true);
        unitTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(unitModel));
        unitTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        unitTab = new Tab("units", unitTable, unitModel, new UnitDialog());

        var baseUnitModel = new BaseUnitModel(List.of(BaseUnit.GRAM, BaseUnit.MILLILITER, BaseUnit.PIECE));
        var baseUnitTable = new JTable(baseUnitModel);
        baseUnitTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(baseUnitModel));
        baseUnitTable.setCellSelectionEnabled(false);

        Box tables = Box.createVerticalBox();
        tables.add(baseUnitTable.getTableHeader());
        tables.add(baseUnitTable);
        tables.add(new JLabel(" "));
        tables.add((unitTable.getTableHeader()));
        tables.add(unitTable);

        unitTab.setComponent(new JScrollPane(tables));
        return unitTable;
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
        optionsMenu.add(editAction);
        optionsMenu.add(deleteAction);
        optionsMenu.add(showAction);
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
        showAction.setEnabled(selectionModel.getSelectedItemsCount() == 1);
    }

    public void updateRecipeCountLabel() {
        int recipeCount = recipeTable.getModel().getRowCount();
        recipeCountLabel.setText("Amount of recipes: " + recipeCount);

    }
}
