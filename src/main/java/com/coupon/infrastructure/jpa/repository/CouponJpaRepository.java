package com.coupon.infrastructure.jpa.repository;

import com.coupon.infrastructure.jpa.entity.CouponJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponJpaRepository extends JpaRepository<CouponJpaEntity, String> {

    @Query("SELECT c FROM CouponJpaEntity c WHERE c.code = :code AND c.deletedAt IS NULL")
    Optional<CouponJpaEntity> findByCodeAndNotDeleted(@Param("code") String code);

    @Query("SELECT c FROM CouponJpaEntity c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<CouponJpaEntity> findByIdAndNotDeleted(@Param("id") String id);

    @Query("SELECT c FROM CouponJpaEntity c WHERE c.id = :id")
    Optional<CouponJpaEntity> findByIdIncludingDeleted(@Param("id") String id);
}
