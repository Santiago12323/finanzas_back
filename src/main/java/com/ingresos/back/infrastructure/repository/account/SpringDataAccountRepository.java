package com.ingresos.back.infrastructure.repository.account;

import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByUserId(String userId);
}