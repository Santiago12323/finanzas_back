package com.ingresos.back.domain.mapper;


import com.ingresos.back.domain.model.User;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserEntity toEntity(User domain);

    User toDomain(UserEntity entity);
}