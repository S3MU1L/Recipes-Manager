package cz.fi.muni.pv168.easyfood.ui.model.listmodel;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

public class IngredientListModel extends AbstractListModel<Ingredient> {

    private List<Ingredient> ingredients;
    private final CrudService<Ingredient> ingredientCrudService;

    public IngredientListModel(CrudService<Ingredient> ingredientCrudService) {
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
        this.ingredientCrudService = ingredientCrudService;
    }

    @Override
    public int getSize() {
        return ingredients.size();
    }

    @Override
    public Ingredient getElementAt(int index) {
        return ingredients.get(index);
    }

    public void refresh() {
        this.ingredients = new ArrayList<>(ingredientCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
