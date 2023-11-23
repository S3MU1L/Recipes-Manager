package cz.muni.fi.pv168.easyfood.storage.sql.dao;

import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.EmployeeEntity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DAO for {@link EmployeeEntity} entity.
 */
public final class EmployeeDao implements DataAccessObject<EmployeeEntity> {

    private final Supplier<ConnectionHandler> connections;

    public EmployeeDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public EmployeeEntity create(EmployeeEntity newEmployee) {
        var sql = """
                INSERT INTO Employee(
                    guid,
                    firstName,
                    lastName,
                    birthDate,
                    gender,
                    departmentId
                )
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newEmployee.guid());
            statement.setString(2, newEmployee.firstName());
            statement.setString(3, newEmployee.lastName());
            statement.setDate(4, Date.valueOf(newEmployee.birthDate()));
            statement.setString(5, newEmployee.gender().toString());
            statement.setLong(6, newEmployee.departmentId());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long employeeId;

                if (keyResultSet.next()) {
                    employeeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newEmployee);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newEmployee);
                }

                return findById(employeeId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newEmployee, ex);
        }
    }

    @Override
    public Collection<EmployeeEntity> findAll() {
        var sql = """
                SELECT id,
                       guid,
                       departmentId,
                       firstName,
                       lastName,
                       gender,
                       birthDate
                FROM Employee
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<EmployeeEntity> employees = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var employee = employeeFromResultSet(resultSet);
                    employees.add(employee);
                }
            }

            return employees;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all employees", ex);
        }
    }

    @Override
    public Optional<EmployeeEntity> findById(long id) {
        var sql = """
                SELECT id,
                       guid,
                       departmentId,
                       firstName,
                       lastName,
                       gender,
                       birthDate
                FROM Employee
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(employeeFromResultSet(resultSet));
            } else {
                // employee not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load employee by id", ex);
        }
    }

    @Override
    public Optional<EmployeeEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                       guid,
                       departmentId,
                       firstName,
                       lastName,
                       gender,
                       birthDate
                FROM Employee
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(employeeFromResultSet(resultSet));
            } else {
                // employee not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load employee by id", ex);
        }
    }

    @Override
    public EmployeeEntity update(EmployeeEntity entity) {
        var sql = """
                UPDATE Employee
                SET firstName = ?,
                    lastName = ?,
                    birthDate = ?,
                    gender = ?,
                    departmentId = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.firstName());
            statement.setString(2, entity.lastName());
            statement.setDate(3, Date.valueOf(entity.birthDate()));
            statement.setString(4, entity.gender().toString());
            statement.setLong(5, entity.departmentId());
            statement.setLong(6, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Employee not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 employee (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update employee: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Employee WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Employee not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 employee (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete employee, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Employee";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all employees", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Employee
                WHERE guid = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if employee exists: " + guid, ex);
        }
    }

    private static EmployeeEntity employeeFromResultSet(ResultSet resultSet) throws SQLException {
        return new EmployeeEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getLong("departmentId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                Gender.valueOf(resultSet.getString("gender")),
                resultSet.getTimestamp("birthDate").toLocalDateTime().toLocalDate()
        );
    }
}
