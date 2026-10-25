package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.Category;
import com.ingresos.back.infrastructure.entity.category.CategoryEntity;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "user.id", target = "userId")
    Category toDomain(CategoryEntity entity);

    @Mapping(source = "userId", target = "user")
    CategoryEntity toEntity(Category category);

    default UserEntity toUserRef(String id) {
        if (id == null) return null;
        UserEntity user = new UserEntity();
        user.setId(id);
        return user;
    }
}