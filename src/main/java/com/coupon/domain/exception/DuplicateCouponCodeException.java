package com.coupon.domain.exception;

public class DuplicateCouponCodeException extends CouponException {

    public DuplicateCouponCodeException(String message) {
        super(message);
    }
}
