package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.exception.UserException;
import com.ingresos.back.domain.model.Subscription;
import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.SubscriptionService;
import com.ingresos.back.domain.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Subscription createSubscription(Subscription subscription, String userId) {
        User user = userService.findById(userId);
        subscription.setUser(user);
        return subscriptionRepository.createSubscription(subscription);
    }

    @Override
    public Subscription findById(String id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new UserException("Suscripción no encontrada con id: " + id));
    }

    @Override
    public List<Subscription> findByUserId(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription updateSubscription(String id, Subscription subscription) {
        findById(id);
        return subscriptionRepository.updateSubscription(id, subscription);
    }

    @Override
    public void deleteSubscription(String id) {
        findById(id);
        subscriptionRepository.deleteById(id);
    }
}