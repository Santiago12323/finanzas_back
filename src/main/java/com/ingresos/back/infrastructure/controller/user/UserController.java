package com.ingresos.back.infrastructure.controller.user;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.infrastructure.controller.dto.UserDto;
import com.ingresos.back.infrastructure.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UseService useService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto
    ) {
        User user = userDtoMapper.toDomain(userDto);

        User createdUser = useService.createUser(user);

        return new ResponseEntity<>(
                userDtoMapper.toDto(createdUser),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(
            @PathVariable String id
    ) {

        User user = useService.findById(id);

        return ResponseEntity.ok(
                userDtoMapper.toDto(user)
        );
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {

        List<UserDto> users = useService.findAllUsers()
                .stream()
                .map(userDtoMapper::toDto)
                .toList();

        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UserDto userDto
    ) {

        User user = userDtoMapper.toDomain(userDto);

        User updatedUser = useService.updateUser(id, user);

        return ResponseEntity.ok(
                userDtoMapper.toDto(updatedUser)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id
    ) {

        useService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
