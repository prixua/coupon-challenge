package com.coupon.infrastructure.adapter;

import com.coupon.domain.Coupon;
import com.coupon.domain.exception.CouponAlreadyDeletedException;
import com.coupon.domain.repository.CouponRepository;
import com.coupon.domain.value.CouponCode;
import com.coupon.domain.value.CouponId;
import com.coupon.infrastructure.jpa.entity.CouponJpaEntity;
import com.coupon.infrastructure.jpa.repository.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponRepositoryJpaAdapter implements CouponRepository {

    private final CouponJpaRepository jpaRepository;

    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity entity = CouponJpaEntity.fromDomain(coupon);
        CouponJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Coupon> findById(CouponId id) {
        return jpaRepository.findByIdAndNotDeleted(id.getValue())
                .map(CouponJpaEntity::toDomain);
    }

    @Override
    public Optional<Coupon> findByIdIncludingDeleted(CouponId id) {
        return jpaRepository.findByIdIncludingDeleted(id.getValue())
                .map(CouponJpaEntity::toDomain);
    }

    @Override
    public Optional<Coupon> findByCode(CouponCode code) {
        return jpaRepository.findByCodeAndNotDeleted(code.getValue())
                .map(CouponJpaEntity::toDomain);
    }

    @Override
    public void delete(Coupon coupon) {
        jpaRepository.findByIdIncludingDeleted(coupon.getId().getValue())
                .ifPresent(entity -> {
                    if (entity.getDeletedAt() != null) {
                        throw new CouponAlreadyDeletedException("Coupon with id " + coupon.getId() + " is already deleted");
                    }
                });

        CouponJpaEntity updatedEntity = CouponJpaEntity.fromDomain(coupon);
        jpaRepository.save(updatedEntity);
    }
}
