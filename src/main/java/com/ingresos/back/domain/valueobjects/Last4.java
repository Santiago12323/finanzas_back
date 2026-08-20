package com.ingresos.back.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public final class Last4 {

    private final String value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Last4(String value) {
        if (value == null || !value.matches("\\d{4}")) {
            throw new IllegalArgumentException("Last4 debe contener exactamente 4 dígitos.");
        }
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Last4 last4)) return false;
        return Objects.equals(value, last4.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}