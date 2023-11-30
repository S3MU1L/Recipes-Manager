package cz.muni.fi.pv168.easyfood.ui.model.listmodel;

import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;

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
