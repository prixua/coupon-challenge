package com.coupon.domain.exception;

public class InvalidExpirationDateException extends CouponException {

    public InvalidExpirationDateException(String message) {
        super(message);
    }
}
