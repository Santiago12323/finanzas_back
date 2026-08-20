package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.mapper.SubscriptionMapper;
import com.ingresos.back.domain.model.Subscription;
import com.ingresos.back.infrastructure.entity.suscription.SubscriptionEntity;
import com.ingresos.back.infrastructure.repository.budget.SpringDataSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SubscriptionRepository {

    @Autowired
    private SpringDataSubscriptionRepository springDataSubscriptionRepository;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    public Subscription createSubscription(Subscription subscription) {
        SubscriptionEntity entity = subscriptionMapper.toEntity(subscription);
        SubscriptionEntity saved = springDataSubscriptionRepository.save(entity);
        return subscriptionMapper.toDomain(saved);
    }

    public Optional<Subscription> findById(String id) {
        return springDataSubscriptionRepository.findById(id).map(subscriptionMapper::toDomain);
    }

    public List<Subscription> findByUserId(String userId) {
        return springDataSubscriptionRepository
                .findByUserId(userId)
                .stream()
                .map(subscriptionMapper::toDomain)
                .toList();
    }

    public List<Subscription> findAll() {
        return springDataSubscriptionRepository.findAll().stream()
                .map(subscriptionMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Subscription updateSubscription(String id, Subscription subscription) {
        SubscriptionEntity entity = subscriptionMapper.toEntity(subscription);
        entity.setId(id);
        SubscriptionEntity updated = springDataSubscriptionRepository.save(entity);
        return subscriptionMapper.toDomain(updated);
    }

    public void deleteById(String id) {
        springDataSubscriptionRepository.deleteById(id);
    }
}