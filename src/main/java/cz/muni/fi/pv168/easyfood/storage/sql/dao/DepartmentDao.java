package cz.muni.fi.pv168.easyfood.storage.sql.dao;

import cz.muni.fi.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.DepartmentEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link DepartmentEntity} entity.
 */
public final class DepartmentDao implements DataAccessObject<DepartmentEntity> {

    private final Supplier<ConnectionHandler> connections;

    public DepartmentDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public DepartmentEntity create(DepartmentEntity newDepartment) {
        var sql = "INSERT INTO Department (guid, number, name) VALUES (?, ?, ?);";

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newDepartment.guid());
            statement.setString(2, newDepartment.number());
            statement.setString(3, newDepartment.name());
            statement.executeUpdate();

            try (ResultSet keyResultSet = statement.getGeneratedKeys()) {
                long departmentId;

                if (keyResultSet.next()) {
                    departmentId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newDepartment);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newDepartment);
                }

                return findById(departmentId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newDepartment, ex);
        }
    }

    @Override
    public Collection<DepartmentEntity> findAll() {
        var sql = """
                SELECT id,
                       guid,
                       number,
                       name
                FROM Department
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            List<DepartmentEntity> departments = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var department = departmentFromResultSet(resultSet);
                    departments.add(department);
                }
            }

            return departments;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all departments", ex);
        }
    }

    @Override
    public Optional<DepartmentEntity> findById(long id) {
        var sql = """
                SELECT id,
                       guid,
                       number,
                       name
                FROM Department
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(departmentFromResultSet(resultSet));
            } else {
                // department not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load department by id: " + id, ex);
        }
    }

    @Override
    public Optional<DepartmentEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                       guid,
                       number,
                       name
                FROM Department
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(departmentFromResultSet(resultSet));
            } else {
                // department not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load department by guid: " + guid, ex);
        }
    }

    @Override
    public DepartmentEntity update(DepartmentEntity entity) {
        var sql = """
                UPDATE Department
                SET number = ?,
                    name = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.number());
            statement.setString(2, entity.name());
            statement.setLong(3, entity.id());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Department not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 department (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update department: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = """
                DELETE FROM Department
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Department not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 department (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete department, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Department";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all departments", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Department
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if department exists, guid: " + guid, ex);
        }
    }

    private static DepartmentEntity departmentFromResultSet(ResultSet resultSet) throws SQLException {
        return new DepartmentEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("number"),
                resultSet.getString("name")
        );
    }
}
