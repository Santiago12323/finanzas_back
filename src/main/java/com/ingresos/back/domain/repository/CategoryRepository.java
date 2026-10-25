package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.mapper.CategoryMapper;
import com.ingresos.back.domain.model.Category;
import com.ingresos.back.infrastructure.repository.category.SpringDataCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @Autowired
    private SpringDataCategoryRepository jpaRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public Category save(Category category) {
        return categoryMapper.toDomain(jpaRepository.save(categoryMapper.toEntity(category)));
    }

    public Optional<Category> findById(String id) {
        return jpaRepository.findById(id).map(categoryMapper::toDomain);
    }

    public List<Category> findAvailableByUserId(String userId) {
        return jpaRepository.findAvailableByUserId(userId).stream().map(categoryMapper::toDomain).toList();
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public List<Category> findAvailableByUserIdAndTypes(String userId, List<CategoryType> types) {
        return jpaRepository.findAvailableByUserIdAndTypes(userId, types).stream()
                .map(categoryMapper::toDomain)
                .toList();
    }
}