package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import com.ingresos.back.domain.mapper.UserMapper;
import com.ingresos.back.infrastructure.repository.user.SpringDataUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    @Autowired
    private UserMapper userMapper;

    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = springDataUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    public Optional<User> findById(String id) {
        return springDataUserRepository.findById(id)
                .map(userMapper::toDomain);
    }

    public List<User> findAll() {
        return springDataUserRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    public User updateUser(String id, User user) {
        UserEntity entity = userMapper.toEntity(user);
        entity.setId(id);
        UserEntity updatedEntity = springDataUserRepository.save(entity);
        return userMapper.toDomain(updatedEntity);
    }

    public void deleteById(String id) {
        springDataUserRepository.deleteById(id);
    }
    public Optional<User> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email).map(userMapper::toDomain);
    }
}