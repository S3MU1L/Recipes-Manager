package cz.fi.muni.pv168.easyfood.bussiness.service.export.batch;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;

import java.util.Collection;

public record Batch(Collection<Recipe> recipes, Collection<Ingredient> ingredients) {
}
