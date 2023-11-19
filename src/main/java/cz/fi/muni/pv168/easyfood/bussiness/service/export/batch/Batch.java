package cz.muni.fi.pv168.employees.business.service.export.batch;

import cz.muni.fi.pv168.employees.business.model.Department;
import cz.muni.fi.pv168.employees.business.model.Employee;

import java.util.Collection;

public record Batch(Collection<Department> departments, Collection<Employee> employees) {
}
