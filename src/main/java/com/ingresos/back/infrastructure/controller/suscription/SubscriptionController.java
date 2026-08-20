package com.ingresos.back.infrastructure.controller.suscription;

import com.ingresos.back.domain.model.Subscription;
import com.ingresos.back.domain.port.SubscriptionService;
import com.ingresos.back.infrastructure.controller.dto.suscription.SubscriptionDto;
import com.ingresos.back.infrastructure.mapper.SubscriptionDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionDtoMapper subscriptionDtoMapper;

    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody SubscriptionDto dto) {
        Subscription subscription = subscriptionDtoMapper.toDomain(dto);
        Subscription created = subscriptionService.createSubscription(subscription, dto.getUserId());
        return new ResponseEntity<>(
                subscriptionDtoMapper.toDto(created),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionDtoMapper.toDto(subscriptionService.findById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDto>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(
                subscriptionService.findByUserId(userId)
                        .stream()
                        .map(subscriptionDtoMapper::toDto)
                        .toList()
        );
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> findAllSubscriptions() {
        return ResponseEntity.ok(
                subscriptionService.findAllSubscriptions()
                        .stream()
                        .map(subscriptionDtoMapper::toDto)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable String id, @RequestBody SubscriptionDto dto) {
        Subscription subscription = subscriptionDtoMapper.toDomain(dto);
        Subscription updated = subscriptionService.updateSubscription(id, subscription);
        return ResponseEntity.ok(subscriptionDtoMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}