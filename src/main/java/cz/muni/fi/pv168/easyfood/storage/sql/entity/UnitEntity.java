package cz.muni.fi.pv168.easyfood.storage.sql.entity;


import cz.muni.fi.pv168.easyfood.business.model.BaseUnit;

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
        this.abbreviation = Objects.requireNonNull(abbreviation, "abbreviation must not be null");
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
