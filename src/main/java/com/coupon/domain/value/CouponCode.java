package com.coupon.domain.value;

import com.coupon.domain.exception.InvalidCouponCodeException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CouponCode {

    private static final int CODE_LENGTH = 6;
    private String value;

    private CouponCode(String value) {
        this.value = value;
    }

    public static CouponCode of(String code) {
        if (code == null || code.isBlank()) {
            throw new InvalidCouponCodeException("Coupon code cannot be null or empty");
        }

        String sanitized = code.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (sanitized.isEmpty()) {
            throw new InvalidCouponCodeException("Coupon code must contain at least one alphanumeric character");
        }

        if (sanitized.length() < CODE_LENGTH) {
            sanitized = String.format("%-6s", sanitized).replace(" ", "0");
        } else if (sanitized.length() > CODE_LENGTH) {
            sanitized = sanitized.substring(0, CODE_LENGTH);
        }

        return new CouponCode(sanitized);
    }

    @Override
    public String toString() {
        return value;
    }
}
