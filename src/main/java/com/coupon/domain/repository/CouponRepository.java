package com.coupon.domain.repository;

import com.coupon.domain.Coupon;
import com.coupon.domain.value.CouponCode;
import com.coupon.domain.value.CouponId;

import java.util.Optional;

public interface CouponRepository {

    /**
     * Save or update a coupon
     * @param coupon the coupon to save
     * @return the saved coupon with id populated
     */
    Coupon save(Coupon coupon);

    /**
     * Find a coupon by id (only non-deleted)
     * @param id the coupon id
     * @return Optional containing the coupon if found and not deleted
     */
    Optional<Coupon> findById(CouponId id);

    /**
     * Find a coupon by id (including deleted)
     * @param id the coupon id
     * @return Optional containing the coupon if found (even if deleted)
     */
    Optional<Coupon> findByIdIncludingDeleted(CouponId id);

    /**
     * Find a coupon by code (only non-deleted)
     * @param code the coupon code
     * @return Optional containing the coupon if found and not deleted
     */
    Optional<Coupon> findByCode(CouponCode code);

    /**
     * Delete a coupon (soft delete)
     * @param coupon the coupon to delete
     */
    void delete(Coupon coupon);
}
