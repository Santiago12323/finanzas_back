package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.exception.CategoryException;
import com.ingresos.back.domain.model.Category;
import com.ingresos.back.domain.port.CategoryService;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.BudgetRepository;
import com.ingresos.back.domain.repository.CategoryRepository;
import com.ingresos.back.domain.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UseService useService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Category createCategory(Category category) {
        category.setId(null);
        validate(category);
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public List<Category> findAvailableByUserId(String userId, CategoryType type) {
        useService.findById(userId);
        return categoryRepository.findAvailableByUserId(userId).stream()
                .filter(c -> type == null || c.getType() == CategoryType.BOTH || c.getType() == type)
                .toList();
    }

    @Override
    public Category findByIdForUser(String categoryId, String userId, CategoryType usage) {
        Category category = findById(categoryId);
        if (category.getUserId() != null && !category.getUserId().equals(userId)) {
            throw new CategoryException("La categoría no pertenece al usuario");
        }
        if (category.getType() != CategoryType.BOTH && category.getType() != usage) {
            throw new CategoryException("La categoría '" + category.getName() + "' no se puede usar para este tipo de movimiento");
        }
        return category;
    }

    @Override
    public Category updateCategory(String id, Category category) {
        Category existing = findById(id);
        if (existing.getUserId() == null) {
            throw new CategoryException("Las categorías por defecto no se pueden modificar");
        }
        category.setId(id);
        category.setUserId(existing.getUserId());   // el dueño no se puede cambiar
        validate(category);
        if (category.getType() == CategoryType.INCOME && isInUse(id)) {
            throw new CategoryException("No se puede cambiar a tipo INCOME una categoría que ya tiene gastos o presupuestos");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        Category existing = findById(id);
        if (existing.getUserId() == null) {
            throw new CategoryException("Las categorías por defecto no se pueden eliminar");
        }
        if (isInUse(id)) {
            throw new CategoryException("No se puede eliminar una categoría que tiene gastos o presupuestos asociados");
        }
        categoryRepository.deleteById(id);
    }

    private boolean isInUse(String categoryId) {
        return expenseRepository.existsByCategoryId(categoryId) || budgetRepository.existsByCategoryId(categoryId);
    }

    @Override
    public List<Category> findByUserIdAndType(String userId, CategoryType type) {
        if (type == null) {
            throw new CategoryException("El tipo de categoría es obligatorio");
        }
        useService.findById(userId);
        List<CategoryType> types = type == CategoryType.BOTH
                ? List.of(CategoryType.BOTH)
                : List.of(type, CategoryType.BOTH);
        return categoryRepository.findAvailableByUserIdAndTypes(userId, types);
    }

    private void validate(Category category) {
        if (category.getName() == null || category.getName().isBlank()) {
            throw new CategoryException("El nombre de la categoría es obligatorio");
        }
        if (category.getType() == null) {
            throw new CategoryException("El tipo de la categoría es obligatorio");
        }
        if (category.getUserId() == null) {
            throw new CategoryException("El usuario es obligatorio para crear una categoría personalizada");
        }
        useService.findById(category.getUserId());

        String name = category.getName().trim();
        category.setName(name);
        boolean duplicated = categoryRepository.findAvailableByUserId(category.getUserId()).stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name) && !c.getId().equals(category.getId()));
        if (duplicated) {
            throw new CategoryException("Ya existe una categoría con el nombre: " + name);
        }
    }
}