package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.infrastructure.controller.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserDto toDto(User user);

    User toDomain(UserDto userDto);
}
