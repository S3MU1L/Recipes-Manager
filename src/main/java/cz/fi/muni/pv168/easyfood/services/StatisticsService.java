package cz.fi.muni.pv168.easyfood.services;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;

import java.util.List;

public class StatisticsService {
    public static Long calculateCategoryStatistics(Category category, List<Recipe> recipes) {
        return recipes.stream()
                .filter(recipe -> recipe.getCategory() == category)
                .count();
    }
}
