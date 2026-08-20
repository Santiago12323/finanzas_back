package com.ingresos.back.infrastructure.entity.suscription;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "service_description")
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    @Column(name = "next_payment_date", nullable = false)
    private LocalDate nextPaymentDate;

    @Column(name = "is_automated", nullable = false)
    private Boolean automated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
}