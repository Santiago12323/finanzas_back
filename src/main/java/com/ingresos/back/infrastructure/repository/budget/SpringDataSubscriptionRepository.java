package com.ingresos.back.infrastructure.repository.budget;

import com.ingresos.back.infrastructure.entity.suscription.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSubscriptionRepository extends JpaRepository<SubscriptionEntity, String> {
    List<SubscriptionEntity> findByUserId(String userId);
}