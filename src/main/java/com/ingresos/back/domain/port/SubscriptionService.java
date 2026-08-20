package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.Subscription;
import java.util.List;

public interface SubscriptionService {
    Subscription createSubscription(Subscription subscription, String userId);
    Subscription findById(String id);
    List<Subscription> findByUserId(String userId);
    List<Subscription> findAllSubscriptions();
    Subscription updateSubscription(String id, Subscription subscription);
    void deleteSubscription(String id);
}