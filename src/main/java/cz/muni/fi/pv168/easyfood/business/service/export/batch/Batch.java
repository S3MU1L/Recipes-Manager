package cz.muni.fi.pv168.easyfood.business.service.export.batch;

import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;

import java.util.Collection;

public record Batch(Collection<Recipe> recipes, Collection<Ingredient> ingredients) {
}
