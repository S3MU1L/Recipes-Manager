package cz.fi.muni.pv168.easyfood.data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Dao {

    private final DataSource dataSource;

    public Dao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public boolean tableExists(String schemaName, String tableName) {
        try (var connection = getDataSource().getConnection();
             var rs = connection.getMetaData().getTables(null, schemaName, tableName, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + schemaName + "." + tableName + " exist", ex);
        }
    }
}
