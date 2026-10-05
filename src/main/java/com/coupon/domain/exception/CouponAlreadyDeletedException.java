package com.coupon.domain.exception;

public class CouponAlreadyDeletedException extends CouponException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
