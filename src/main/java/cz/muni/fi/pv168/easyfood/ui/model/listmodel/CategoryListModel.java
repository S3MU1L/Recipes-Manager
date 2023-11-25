package cz.muni.fi.pv168.easyfood.ui.model.listmodel;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

public class CategoryListModel extends AbstractListModel<Category> {

    private List<Category> categories;
    private final CrudService<Category> categoryCrudService;

    public CategoryListModel(CrudService<Category> categoryCrudService) {
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        this.categoryCrudService = categoryCrudService;
    }

    @Override
    public int getSize() {
        return categories.size();
    }

    @Override
    public Category getElementAt(int index) {
        return categories.get(index);
    }

    public void refresh() {
        this.categories = new ArrayList<>(categoryCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
