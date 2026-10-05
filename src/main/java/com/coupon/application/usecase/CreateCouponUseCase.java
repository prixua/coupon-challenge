package com.coupon.application.usecase;

import com.coupon.application.dto.CreateCouponRequest;
import com.coupon.application.dto.CreateCouponResponse;
import com.coupon.domain.Coupon;
import com.coupon.domain.exception.DuplicateCouponCodeException;
import com.coupon.domain.repository.CouponRepository;
import com.coupon.domain.value.CouponCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCouponUseCase {

    private final CouponRepository couponRepository;

    public CreateCouponResponse execute(CreateCouponRequest request) {
        Coupon coupon = Coupon.create(
                request.getCode(),
                request.getDescription(),
                request.getDiscountValue(),
                request.getExpirationDate(),
                request.isPublished()
        );

        CouponCode sanitizedCode = coupon.getCode();
        couponRepository.findByCode(sanitizedCode).ifPresent(existing -> {
            throw new DuplicateCouponCodeException("Coupon with code '" + sanitizedCode + "' already exists");
        });

        Coupon savedCoupon = couponRepository.save(coupon);

        return CreateCouponResponse.fromDomain(savedCoupon);
    }
}
