package cz.muni.fi.pv168.easyfood.storage.sql.dao;

import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;
import cz.muni.fi.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.UnitEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class UnitDao implements DataAccessObject<UnitEntity> {

    private final Supplier<ConnectionHandler> connections;

    public UnitDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public UnitEntity create(UnitEntity entity) {
        var sql = """
                INSERT INTO Unit(guid, baseUnitOrdinal, name, abbreviation, conversion)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setInt(2, entity.baseUnit().ordinal()); // Use ordinal here
            statement.setString(3, entity.name());
            statement.setString(4, entity.abbreviation());
            statement.setDouble(5, entity.conversion());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long unitId;

                if (keyResultSet.next()) {
                    unitId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(unitId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<UnitEntity> findAll() {
        var sql = "SELECT id, guid, baseUnitOrdinal, name, abbreviation, conversion FROM Unit";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {

            List<UnitEntity> units = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var unit = unitFromResultSet(resultSet);
                    units.add(unit);
                }
            }

            return units;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all units", ex);
        }
    }

    private static UnitEntity unitFromResultSet(ResultSet resultSet) throws SQLException {
        return new UnitEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                BaseUnit.values()[resultSet.getInt("baseUnitOrdinal")],
                resultSet.getString("name"),
                resultSet.getString("abbreviation"),
                resultSet.getDouble("conversion")
        );
    }

    @Override
    public Optional<UnitEntity> findById(long id) {
        var sql = "SELECT id, guid, baseUnitOrdinal, name, abbreviation, conversion FROM Unit WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by id", ex);
        }
    }

    @Override
    public Optional<UnitEntity> findByGuid(String guid) {
        var sql = "SELECT id, guid, baseUnitOrdinal, name, abbreviation, conversion FROM Unit WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by guid", ex);
        }
    }

    @Override
    public UnitEntity update(UnitEntity entity) {
        var sql = "UPDATE Unit SET baseUnitOrdinal = ?, name = ?, abbreviation = ?, conversion = ? WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setInt(1, entity.baseUnit().ordinal()); // Use ordinal here
            statement.setString(2, entity.name());
            statement.setString(3, entity.abbreviation());
            statement.setDouble(4, entity.conversion());
            statement.setLong(5, entity.id());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update unit: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Unit WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete unit by guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Unit";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all units", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = "SELECT id FROM Unit WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if unit exists by guid: " + guid, ex);
        }
    }


}
