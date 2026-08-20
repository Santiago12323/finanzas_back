package com.ingresos.back.infrastructure.repository.user;

import com.ingresos.back.infrastructure.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}