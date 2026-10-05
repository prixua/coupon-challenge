package com.coupon.domain;

import com.coupon.domain.exception.CouponAlreadyDeletedException;
import com.coupon.domain.exception.InvalidExpirationDateException;
import com.coupon.domain.value.CouponCode;
import com.coupon.domain.value.CouponId;
import com.coupon.domain.value.DiscountValue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Coupon {

    private CouponId id;
    private CouponCode code;
    private String description;
    private DiscountValue discountValue;
    private LocalDateTime expirationDate;
    private boolean published;
    private boolean redeemed;
    private Coupon.CouponStatus status;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;

    public static Coupon create(String codeValue, String description, BigDecimal discountValue,
                                LocalDateTime expirationDate, boolean published) {
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new InvalidExpirationDateException("Expiration date cannot be in the past");
        }

        CouponCode code = CouponCode.of(codeValue);
        DiscountValue discount = DiscountValue.of(discountValue);
        CouponId id = CouponId.generate();

        return Coupon.builder()
                .id(id)
                .code(code)
                .description(description)
                .discountValue(discount)
                .expirationDate(expirationDate)
                .published(published)
                .redeemed(false)
                .status(CouponStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
    }

    public void publish() {
        this.published = true;
    }

    public void delete() {
        if (isDeleted()) {
            throw new CouponAlreadyDeletedException("Coupon with id " + id + " is already deleted");
        }
        this.status = CouponStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.status == CouponStatus.DELETED;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public boolean isActive() {
        return this.status == CouponStatus.ACTIVE && !isExpired();
    }

    public enum CouponStatus {
        ACTIVE,
        INACTIVE,
        DELETED
    }
}
