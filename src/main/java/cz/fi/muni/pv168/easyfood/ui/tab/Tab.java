package cz.fi.muni.pv168.easyfood.ui.tab;

import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.service.RecipeService;
import cz.fi.muni.pv168.easyfood.service.Service;
import cz.fi.muni.pv168.easyfood.ui.table.Table;

import javax.swing.JComponent;
import java.util.List;

public class Tab<E> {

     private Service<E> service;

     private JComponent component;

     private String title;

     private Table<E> table;

    public Tab(Service<E> service, String title, Table<E> table) {
        this.title = title;
        this.service = service;
        this.table =  table;
        this.component =  table.wrapIntoScrollPane();
    }

    public Service<E> getService() {
        return service;
    }

    public void setService(Service<E> service) {
        this.service = service;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Table<E> getTable() {
        return table;
    }

    public void setTable(Table<E> table) {
        this.table = table;
    }

    public void deleteSelected() {
        List<E> selected = table.getSelectedEntities();
        var iterator = selected.listIterator(selected.size());
        while (iterator.hasPrevious()) {
           service.delete(iterator.previous());
        }
    }

    public void showSelected() {
        table.getSelectedEntities().forEach(service::openShowWindow);
    }

    public void updateSelected() {
        table.getSelectedEntities().forEach(service::openUpdateWindow);
    }

    public void generateShoppingList() {
        ((RecipeService) service).generateShoppingList((List<Recipe>) table.getSelectedEntities());
    }
}
