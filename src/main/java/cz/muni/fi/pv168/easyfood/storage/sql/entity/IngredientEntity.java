package cz.muni.fi.pv168.easyfood.storage.sql.entity;


import java.util.Objects;

public record IngredientEntity(
        Long id,
        String guid,
        Long unitId,
        String name,
        double calories) {
    public IngredientEntity(
            Long id,
            String guid,
            Long unitId,
            String name,
            double calories) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.unitId = unitId;
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.calories = calories;
    }

    public IngredientEntity(
            String guid,
            Long unitId,
            String name,
            double calories) {
        this(null, guid, unitId, name, calories);
    }
}
