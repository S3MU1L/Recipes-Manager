package cz.muni.fi.pv168.easyfood.storage.sql.entity;

import java.awt.Color;
import java.util.Objects;

public record CategoryEntity(
        Long id,
        String guid,
        String name,
        Color color
) {
    public CategoryEntity(Long id, String guid, String name, Color color) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.color = Objects.requireNonNull(color, "rgb must not be null");
    }
}
