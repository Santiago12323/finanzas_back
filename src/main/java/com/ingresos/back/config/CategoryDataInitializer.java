package com.ingresos.back.config;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.infrastructure.entity.category.CategoryEntity;
import com.ingresos.back.infrastructure.repository.category.SpringDataCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDataInitializer implements CommandLineRunner {

    @Autowired
    private SpringDataCategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        List.of(
                // Gastos
                defaultCategory("Alimentación", CategoryType.EXPENSE, "utensils", "#F97316"),
                defaultCategory("Transporte", CategoryType.EXPENSE, "car", "#EAB308"),
                defaultCategory("Vivienda", CategoryType.EXPENSE, "home", "#3B82F6"),
                defaultCategory("Entretenimiento", CategoryType.EXPENSE, "film", "#EC4899"),
                defaultCategory("Educación", CategoryType.EXPENSE, "graduation-cap", "#8B5CF6"),
                defaultCategory("Salud", CategoryType.EXPENSE, "heart-pulse", "#EF4444"),
                defaultCategory("Servicios", CategoryType.EXPENSE, "zap", "#14B8A6"),
                defaultCategory("Ropa", CategoryType.EXPENSE, "shirt", "#6366F1"),
                defaultCategory("Suscripciones", CategoryType.EXPENSE, "repeat", "#06B6D4"),
                defaultCategory("Otros", CategoryType.EXPENSE, "ellipsis", "#6B7280"),
                // Ingresos
                defaultCategory("Salario", CategoryType.INCOME, "briefcase", "#22C55E"),
                defaultCategory("Freelance", CategoryType.INCOME, "laptop", "#10B981"),
                defaultCategory("Inversiones", CategoryType.INCOME, "trending-up", "#0EA5E9"),
                defaultCategory("Regalos", CategoryType.INCOME, "gift", "#F43F5E"),
                defaultCategory("Otros ingresos", CategoryType.INCOME, "ellipsis", "#6B7280")
        ).forEach(category -> {
            if (!categoryRepository.existsByNameAndUserIsNull(category.getName())) {
                categoryRepository.save(category);
            }
        });
    }

    private CategoryEntity defaultCategory(String name, CategoryType type, String icon, String color) {
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        category.setType(type);
        category.setIcon(icon);
        category.setColor(color);
        category.setUser(null);
        return category;
    }
}