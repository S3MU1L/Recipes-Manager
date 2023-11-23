package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

final class DatabaseManagerTest {
    private DatabaseManager manager;

    @BeforeEach
    void setUp() {
        this.manager = DatabaseManager.createTestInstance();
    }

    @AfterEach
    void tearDown() {
        manager.destroySchema();
    }

    @Test
    void schemaInitializationShouldBeIdempotent() {
        assertThatCode(() -> manager.initSchema()).doesNotThrowAnyException();
    }

    @Test
    void destroySchemaShouldByValid() {
        assertThatCode(() -> manager.destroySchema()).doesNotThrowAnyException();
    }
}
