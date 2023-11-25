package cz.muni.fi.pv168.easyfood.storage.sql.entity;

import java.util.Objects;

public record IngredientWithAmountEntity(
        Long id,
        String guid,
        Long ingredientId,
        double amount) {

    public IngredientWithAmountEntity(
            Long id,
            String guid,
            Long ingredientId,
            double amount
    ) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.ingredientId = ingredientId;
        this.amount = amount;
    }

    public IngredientWithAmountEntity(
            String guid,
            Long ingredientId,
            double amount
    ) {
        this(null, guid, ingredientId, amount);
    }
}
