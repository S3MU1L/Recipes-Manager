package cz.muni.fi.pv168.easyfood.ui.model.listmodel;

import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

public class UnitListModel extends AbstractListModel<Unit> {

    private List<Unit> units;
    private final CrudService<Unit> unitCrudService;

    public UnitListModel(CrudService<Unit> unitCrudService) {
        this.units = new ArrayList<>(unitCrudService.findAll());
        this.unitCrudService = unitCrudService;
    }

    @Override
    public int getSize() {
        return units.size();
    }

    @Override
    public Unit getElementAt(int index) {
        return units.get(index);
    }

    public void refresh() {
        this.units = new ArrayList<>(unitCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
