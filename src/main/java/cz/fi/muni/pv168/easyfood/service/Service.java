package cz.fi.muni.pv168.easyfood.service;

import java.util.List;

public interface Service<E> {

    void add(E entity);
    void update(E entity);
    void delete(E entity);
    List<E> getEntityList();

    void openAddWindow();
    void openUpdateWindow(E entity);
    void openShowWindow(E entity);
}
