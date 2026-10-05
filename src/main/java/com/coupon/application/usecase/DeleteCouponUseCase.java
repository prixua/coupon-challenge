package com.coupon.application.usecase;

import com.coupon.application.dto.DeleteCouponRequest;
import com.coupon.application.dto.DeleteCouponResponse;
import com.coupon.domain.Coupon;
import com.coupon.domain.exception.CouponNotFoundException;
import com.coupon.domain.repository.CouponRepository;
import com.coupon.domain.value.CouponId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    public DeleteCouponResponse execute(DeleteCouponRequest request) {
        CouponId couponId = CouponId.of(request.getId());

        Coupon coupon = couponRepository.findByIdIncludingDeleted(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon with id '" + couponId + "' not found"));

        coupon.delete();

        couponRepository.delete(coupon);

        return DeleteCouponResponse.success(coupon.getId().getValue());
    }
}
