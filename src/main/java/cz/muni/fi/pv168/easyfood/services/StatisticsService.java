package cz.muni.fi.pv168.easyfood.services;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;

import java.util.List;

public class StatisticsService {
    public static Long calculateCategoryStatistics(Category category, List<Recipe> recipes) {
        return recipes.stream()
                .filter(recipe -> recipe.getCategory().getName().equals(category.getName()))
                .count();
    }
}
