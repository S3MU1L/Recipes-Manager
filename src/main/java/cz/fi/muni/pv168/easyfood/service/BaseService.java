package cz.fi.muni.pv168.easyfood.service;

import cz.fi.muni.pv168.easyfood.data.DataAccessObject;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class BaseService<E> implements Service<E> {

    protected final DataAccessObject<E> dataAccessObject;
    protected List<E> entityList = new ArrayList<>();

    public BaseService(DataAccessObject<E> dataAccessObject) {
        this.dataAccessObject = dataAccessObject;
        new LoadEntitiesWorker().execute();
    }

    public abstract void add(E entity);

    public abstract void update(E entity);

    public abstract void delete(E entity);

    public abstract List<E> getEntityList();

    public abstract void openAddWindow();

    public abstract void openUpdateWindow(E entity);

    public abstract void openShowWindow(E entity);

    public void updateAll() {
    }

    private class LoadEntitiesWorker extends SwingWorker<List<E>, Void> {

        @Override
        protected List<E> doInBackground() {
            return dataAccessObject.findAll();
        }

        @Override
        protected void done() {
            try {
                entityList.addAll(get());
                updateAll();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), "Error when loading entities",
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

}