package com.coupon.domain.value;

import com.coupon.domain.exception.InvalidDiscountValueException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class DiscountValue {

    private static final BigDecimal MINIMUM_DISCOUNT = new BigDecimal("0.5");
    private BigDecimal value;

    private DiscountValue(BigDecimal value) {
        this.value = value;
    }

    public static DiscountValue of(BigDecimal discount) {
        if (discount == null) {
            throw new InvalidDiscountValueException("Discount value cannot be null");
        }

        if (discount.compareTo(MINIMUM_DISCOUNT) < 0) {
            throw new InvalidDiscountValueException("Discount value must be at least " + MINIMUM_DISCOUNT);
        }

        return new DiscountValue(discount);
    }

    public static DiscountValue of(Number discount) {
        if (discount == null) {
            throw new InvalidDiscountValueException("Discount value cannot be null");
        }
        return of(new BigDecimal(discount.toString()));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
