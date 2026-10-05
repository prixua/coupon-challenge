package com.coupon.domain.value;

import com.coupon.domain.exception.InvalidDiscountValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DiscountValue Value Object Tests")
class DiscountValueTest {

    @Test
    @DisplayName("Should create a valid discount value")
    void shouldCreateValidDiscountValue() {
        BigDecimal value = new BigDecimal("10.50");

        DiscountValue result = DiscountValue.of(value);

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should accept minimum discount value of 0.5")
    void shouldAcceptMinimumDiscountValue() {
        BigDecimal value = new BigDecimal("0.5");

        DiscountValue result = DiscountValue.of(value);

        assertThat(result.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should throw exception for discount below 0.5")
    void shouldThrowExceptionForDiscountBelowMinimum() {
        BigDecimal value = new BigDecimal("0.49");

        assertThatThrownBy(() -> DiscountValue.of(value))
                .isInstanceOf(InvalidDiscountValueException.class)
                .hasMessageContaining("at least");
    }

    @Test
    @DisplayName("Should throw exception for null discount value")
    void shouldThrowExceptionForNullValue() {
        assertThatThrownBy(() -> DiscountValue.of((BigDecimal) null))
                .isInstanceOf(InvalidDiscountValueException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    @DisplayName("Should accept Number type and convert to BigDecimal")
    void shouldAcceptNumberType() {
        Number value = 5.5;

        DiscountValue result = DiscountValue.of(value);

        assertThat(result.getValue()).isEqualTo(new BigDecimal("5.5"));
    }

    @Test
    @DisplayName("Should be equal when values are equal")
    void shouldBeEqualWhenValuesAreEqual() {
        DiscountValue discount1 = DiscountValue.of(new BigDecimal("10.00"));
        DiscountValue discount2 = DiscountValue.of(new BigDecimal("10.00"));

        assertThat(discount1).isEqualTo(discount2);
    }

    @Test
    @DisplayName("Should accept large discount values")
    void shouldAcceptLargeValues() {
        BigDecimal value = new BigDecimal("999999.99");

        DiscountValue result = DiscountValue.of(value);

        assertThat(result.getValue()).isEqualTo(value);
    }
}
