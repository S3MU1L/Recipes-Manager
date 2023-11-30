package cz.muni.fi.pv168.easyfood.wiring;


import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;

/**
 * Dependency provider for production environment.
 */
public class ProductionDependencyProvider extends CommonDependencyProvider {

    public ProductionDependencyProvider() {
        super(createDatabaseManager());
    }

    private static DatabaseManager createDatabaseManager() {
        DatabaseManager databaseManager = DatabaseManager.createProductionInstance();
        databaseManager.initSchema();

        return databaseManager;
    }
}
