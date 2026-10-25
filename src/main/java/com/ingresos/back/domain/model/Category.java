package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.category.CategoryType;
import lombok.Data;

@Data
public class Category {
    private String id;
    private String name;
    private CategoryType type;
    private String icon;
    private String color;
    private String userId;
}