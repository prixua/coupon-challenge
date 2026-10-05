package com.coupon.domain.value;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CouponId {

    private String value;

    private CouponId(String value) {
        this.value = value;
    }

    public static CouponId generate() {
        return new CouponId(UUID.randomUUID().toString());
    }

    public static CouponId of(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("CouponId cannot be null or empty");
        }
        return new CouponId(id);
    }

    @Override
    public String toString() {
        return value;
    }
}
