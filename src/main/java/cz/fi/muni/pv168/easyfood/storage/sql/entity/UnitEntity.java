package cz.fi.muni.pv168.easyfood.storage.sql.entity;


import cz.fi.muni.pv168.easyfood.bussiness.model.BaseUnit;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;

import java.util.Objects;

public record UnitEntity(
        Long id,
        String guid,
        BaseUnit baseUnit,
        String name,
        String abbreviation,
        double conversion) {
    public UnitEntity(
            Long id,
            String guid,
            BaseUnit baseUnit,
            String name,
            String abbreviation,
            double conversion) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.baseUnit = baseUnit;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.abbreviation = Objects.requireNonNull(name, "abbreviation must not be null");
        this.conversion = conversion;
    }

    public UnitEntity(
            String guid,
            BaseUnit baseUnit,
            String name,
            String abbreviation,
            double conversion) {
        this(null, guid, baseUnit, name, abbreviation, conversion);
    }
}
