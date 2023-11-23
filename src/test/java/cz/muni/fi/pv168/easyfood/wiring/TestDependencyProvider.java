package cz.muni.fi.pv168.easyfood.wiring;

import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;

public final class TestDependencyProvider extends CommonDependencyProvider {

    public TestDependencyProvider(DatabaseManager databaseManager) {
        super(databaseManager);
    }
}
