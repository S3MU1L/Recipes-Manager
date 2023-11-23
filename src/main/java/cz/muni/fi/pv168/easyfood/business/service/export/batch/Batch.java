package cz.muni.fi.pv168.easyfood.business.service.export.batch;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;

import java.util.Collection;

public record Batch(Collection<Department> departments, Collection<Employee> employees) {
}
