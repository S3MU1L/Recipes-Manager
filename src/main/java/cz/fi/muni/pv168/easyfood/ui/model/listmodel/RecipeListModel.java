package cz.fi.muni.pv168.easyfood.ui.model.listmodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

public class RecipeListModel extends AbstractListModel<Recipe> {

    private List<Recipe> recipes;
    private final CrudService<Recipe> recipeCrudService;

    public RecipeListModel(CrudService<Recipe> recipeCrudService) {
        this.recipes = new ArrayList<>(recipeCrudService.findAll());
        this.recipeCrudService = recipeCrudService;
    }

    @Override
    public int getSize() {
        return recipes.size();
    }

    @Override
    public Recipe getElementAt(int index) {
        return recipes.get(index);
    }

    public void refresh() {
        this.recipes = new ArrayList<>(recipeCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
