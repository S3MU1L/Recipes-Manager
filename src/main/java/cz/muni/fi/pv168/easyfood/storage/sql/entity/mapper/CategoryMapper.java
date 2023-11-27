package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.CategoryEntity;

public class CategoryMapper implements EntityMapper<CategoryEntity, Category> {

    @Override
    public Category mapToBusiness(CategoryEntity entity) {
        return new Category(
                entity.guid(),
                entity.name(),
                entity.color()
        );
    }

    @Override
    public CategoryEntity mapNewEntityToDatabase(Category entity) {
        return getCategoryEntity(entity, null);
    }

    @Override
    public CategoryEntity mapExistingEntityToDatabase(Category entity, Long dbId) {
        return getCategoryEntity(entity, dbId);
    }

    private static CategoryEntity getCategoryEntity(Category entity, Long dbId) {
        return new CategoryEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getColor()
        );
    }
}
