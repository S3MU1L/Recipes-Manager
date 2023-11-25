package cz.fi.muni.pv168.easyfood.ui;


import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.model.UuidGuidProvider;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CategoryCrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.IngredientCrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.RecipeCrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.UnitCrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.CategoryValidator;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.IngredientValidator;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.RecipeValidator;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.UnitValidator;
import cz.fi.muni.pv168.easyfood.data.TestDataGenerator;
import cz.fi.muni.pv168.easyfood.storage.memory.InMemoryRepository;
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
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.CategoryTableModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.IngredientTableModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.RecipeTableModel;
import cz.fi.muni.pv168.easyfood.ui.model.tablemodel.UnitTableModel;
import cz.fi.muni.pv168.easyfood.ui.renderers.CustomTableCellRenderer;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

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
    private final RecipeTableModel recipeTableModel;
    private final IngredientTableModel ingredientTableModel;
    private final CategoryTableModel categoryTableModel;
    private final UnitTableModel unitTableModel;
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Category> categoryCrudService;
    private final CrudService<Unit> unitCrudService;

    private JLabel recipeCountLabel = new JLabel();

    public MainWindow() {
        frame = createFrame();

        var testDataGenerator = new TestDataGenerator();

        List<Unit> units = testDataGenerator.createTestUnits(5);
        List<Category> categories = testDataGenerator.createTestCategories(5);
        List<Ingredient> ingredients = testDataGenerator.createTestIngredients(5, units);
        List<Recipe> recipes = testDataGenerator.createTestRecipes(10, ingredients, categories);

        var recipeRepository = new InMemoryRepository<>(recipes);
        var ingredientRepository = new InMemoryRepository<>(ingredients);
        var categoryRepository = new InMemoryRepository<>(categories);
        var unitRepository = new InMemoryRepository<>(units);

        var recipeValidator = new RecipeValidator();
        var ingredientValidator = new IngredientValidator();
        var categoryValidator = new CategoryValidator();
        var unitValidator = new UnitValidator();

        var guidProvider = new UuidGuidProvider();
        recipeCrudService = new RecipeCrudService(recipeRepository, recipeValidator, guidProvider);
        ingredientCrudService = new IngredientCrudService(ingredientRepository, ingredientValidator, guidProvider);
        categoryCrudService = new CategoryCrudService(categoryRepository, categoryValidator, guidProvider);
        unitCrudService = new UnitCrudService(unitRepository, unitValidator, guidProvider);

        recipeTableModel = new RecipeTableModel(recipeCrudService);
        ingredientTableModel = new IngredientTableModel(ingredientCrudService, recipeCrudService);
        categoryTableModel = new CategoryTableModel(categoryCrudService, recipeCrudService);
        unitTableModel = new UnitTableModel(unitCrudService, ingredientCrudService);

        unitTable = createUnitTable(unitTableModel);
        ingredientTable = createIngredientTable(ingredientTableModel);
        categoryTable = createCategoryTable(categoryTableModel);
        recipeTable = createRecipeTable(recipeTableModel);

        tabContainer = new TabContainer();
        tabContainer.addTab(recipeTab);
        tabContainer.addTab(ingredientTab);
        tabContainer.addTab(categoryTab);
        tabContainer.addTab(unitTab);
        tabContainer.addChangeListener(this::tabChangeListener);

        TabContainer filterContainer = new TabContainer();
        var model = new RecipeTableModel(recipeCrudService);
        var table = new JTable(model);
        Tab filterTab = new Tab("Filter", table, model, new FilterDialog(categories, ingredients));
        filterContainer.addTab(filterTab);

        addAction = new AddAction(this, tabContainer, recipes, ingredients, categories, units);
        deleteAction = new DeleteAction(this, tabContainer);
        editAction = new EditAction(tabContainer, recipes, ingredients, categories, units);
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

    private JTable createRecipeTable(RecipeTableModel recipeTableModel) {
        var table = new JTable(recipeTableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(recipeTableModel));
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        recipeTab =
                new Tab("recipes", table, recipeTableModel,
                        new RecipeDialog(Recipe.createEmptyRecipe(),
                                recipeCrudService.findAll(),
                                ingredientCrudService.findAll(),
                                categoryCrudService.findAll()));
        return table;
    }

    private JTable createIngredientTable(IngredientTableModel ingredientTableModel) {
        var table = new JTable(ingredientTableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(ingredientTableModel));
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        ingredientTab = new Tab("ingredients", table, ingredientTableModel, new IngredientDialog(ingredientCrudService.findAll(), unitCrudService.findAll()));
        return table;
    }

    private JTable createCategoryTable(CategoryTableModel categoryTableModel) {
        var table = new JTable(categoryTableModel);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(categoryTableModel));
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        categoryTab = new Tab("categories", table, categoryTableModel, new CategoryDialog(categoryCrudService.findAll()));
        return table;
    }

    private JTable createUnitTable(UnitTableModel unitTableModel) {
        var unitTable = new JTable(unitTableModel);
        unitTable.setAutoCreateRowSorter(true);
        unitTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer<>(unitTableModel));
        unitTable.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);
        unitTab = new Tab("units", unitTable, unitTableModel, new UnitDialog(unitCrudService.findAll()));
        Box tables = Box.createVerticalBox();
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
