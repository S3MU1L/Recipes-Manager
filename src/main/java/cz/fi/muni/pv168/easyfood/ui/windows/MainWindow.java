package cz.fi.muni.pv168.easyfood.ui.windows;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.actions.*;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;
import cz.fi.muni.pv168.easyfood.ui.table.IngredientTable;
import cz.fi.muni.pv168.easyfood.ui.table.RecipeTable;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.IngredientTableModel;
import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.RecipeTableModel;
import cz.fi.muni.pv168.easyfood.ui.utility.Util;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class MainWindow extends AbstractWindow {

    private static final I18N I18N = new I18N(MainWindow.class);

    private final Action quitAction = new QuitAction();
    private final Action addRecipeAction;
    private final Action addIngredientAction;
    private final Action deleteAction;
    private final EditAction editAction;
    private final Action showAction;
    private final Action filterAction = null;
    private final Action importAction = null;
    private final Action exportAction = null;
    private final IngredientTable ingredientTable;
    private final RecipeTable recipeTable;
    private int currentTabIndex = 0;

    private final TabContainer tabContainer = new TabContainer();

    public MainWindow(Service<Recipe> recipeService, Service<Ingredient> ingredientService) {
        super(I18N.getString("title"), null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ingredientTable = new IngredientTable(new IngredientTableModel(ingredientService));
        recipeTable = new RecipeTable(new RecipeTableModel(recipeService));

        Tab<Ingredient> ingredientTab = new Tab<>(ingredientService, I18N.getString("ingredients.tab.title"), ingredientTable);
        Tab<Recipe> recipeTab = new Tab<>(recipeService, I18N.getString("recipes.tab.title"), recipeTable);

        tabContainer.addTab(recipeTab);
        tabContainer.addTab(ingredientTab);
        tabContainer.addChangeListener(this::tabbSwitchListener);

        addRecipeAction = new AddRecipeAction(recipeService);
        addIngredientAction = new AddIngredientAction(ingredientService);

        deleteAction = new DeleteAction(tabContainer::getSelectedTab);
        editAction = new EditAction(tabContainer::getSelectedTab);
        showAction = new ShowAction(tabContainer::getSelectedTab);

        editAction.setEnabled(false);
        deleteAction.setEnabled(false);
        showAction.setEnabled(false);

        ingredientTable.addActions(List.of(addIngredientAction, editAction, deleteAction, showAction));
        recipeTable.addActions(List.of(addRecipeAction, editAction, deleteAction, showAction));

        setRecipeTableListeners();
        setIngredientTableListeners();

        frame.setMinimumSize(new Dimension(450, 450));

        frame.add(tabContainer.getComponent(), BorderLayout.CENTER);
        frame.add(createToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
    }

    private void setRecipeTableListeners() {
        recipeTable.addListSelectionListener(this::rowSelectionChanged);
        recipeTable.addMouseListener(Util.createDoubleClickListener(showAction));
        recipeTable.addKeyListener(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                new ShowAction(tabContainer::getSelectedTab));
    }

    private void setIngredientTableListeners() {
        ingredientTable.addListSelectionListener(this::rowSelectionChanged);
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('o');
        optionsMenu.add(addRecipeAction);
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
        toolbar.add(addRecipeAction);
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.add(showAction);
        toolbar.add(filterAction);
        return toolbar;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        if (selectionModel.getSelectedItemsCount() == 1 & currentTabIndex == 0) {
            editAction.setEnabled(true);
        }
        deleteAction.setEnabled(selectionModel.getSelectedItemsCount() > 0);
        showAction.setEnabled(selectionModel.getSelectedItemsCount() > 0 && currentTabIndex == 0);
        editAction.setEnabled(selectionModel.getSelectedItemsCount() == 1 && currentTabIndex == 0);

    }

    private void tabbSwitchListener(ChangeEvent changeEvent) {
        var tabbedPane = (JTabbedPane) changeEvent.getSource();
        currentTabIndex = tabbedPane.getSelectedIndex();
        showAction.setEnabled(currentTabIndex == 0 && recipeTable.getSelectedCount() > 0);

        ingredientTable.clearSelection();
        recipeTable.clearSelection();
    }

    @Override
    public void onAddedIngredient() {
        ingredientTable.add();
    }

    @Override
    public void onAddedRecipe() {
        recipeTable.add();
    }

    @Override
    public void onDeletedIngredient(Ingredient ingredient, int index) {
        ingredientTable.delete(index);
    }

    @Override
    public void onDeletedRecipe(Recipe recipe, int index) {
        recipeTable.delete(index);
    }

    @Override
    public void onUpdatedRecipe(Recipe recipe, int index) {
        recipeTable.update(index);
    }

    @Override
    public void onUpdateAll() {
        recipeTable.updateAllRows();
    }
}
