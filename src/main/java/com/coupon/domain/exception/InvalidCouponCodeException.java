package com.coupon.domain.exception;

public class InvalidCouponCodeException extends CouponException {

    public InvalidCouponCodeException(String message) {
        super(message);
    }
}
