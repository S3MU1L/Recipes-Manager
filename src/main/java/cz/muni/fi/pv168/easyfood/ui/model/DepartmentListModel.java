package cz.muni.fi.pv168.easyfood.ui.model;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.List;

public class DepartmentListModel extends AbstractListModel<Department> {

    private List<Department> departments;
    private final CrudService<Department> departmentCrudService;

    public DepartmentListModel(CrudService<Department> departmentCrudService) {
        this.departments = new ArrayList<>(departmentCrudService.findAll());
        this.departmentCrudService = departmentCrudService;
    }

    @Override
    public int getSize() {
        return departments.size();
    }

    @Override
    public Department getElementAt(int index) {
        return departments.get(index);
    }

    public void refresh() {
        this.departments = new ArrayList<>(departmentCrudService.findAll());
        fireContentsChanged(this, 0, getSize() - 1);
    }
}
