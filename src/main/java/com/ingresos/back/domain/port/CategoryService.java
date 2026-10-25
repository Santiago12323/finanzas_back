package com.ingresos.back.domain.port;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.model.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(Category category);

    Category findById(String id);

    List<Category> findAvailableByUserId(String userId, CategoryType type);

    Category findByIdForUser(String categoryId, String userId, CategoryType usage);

    Category updateCategory(String id, Category category);

    void deleteCategory(String id);

    List<Category> findByUserIdAndType(String userId, CategoryType type);
}