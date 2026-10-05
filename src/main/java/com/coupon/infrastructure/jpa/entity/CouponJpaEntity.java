package com.coupon.infrastructure.jpa.entity;

import com.coupon.domain.Coupon;
import com.coupon.domain.value.CouponCode;
import com.coupon.domain.value.CouponId;
import com.coupon.domain.value.DiscountValue;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponJpaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String code;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Coupon.CouponStatus status;

    @Column
    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Coupon toDomain() {
        Coupon coupon = Coupon.builder()
                .id(CouponId.of(this.id))
                .code(CouponCode.of(this.code))
                .description(this.description)
                .discountValue(DiscountValue.of(this.discountValue))
                .expirationDate(this.expirationDate)
                .published(this.published)
                .redeemed(this.redeemed)
                .status(this.status)
                .deletedAt(this.deletedAt)
                .createdAt(this.createdAt)
                .build();
        return coupon;
    }

    public static CouponJpaEntity fromDomain(Coupon coupon) {
        return CouponJpaEntity.builder()
                .id(coupon.getId().getValue())
                .code(coupon.getCode().getValue())
                .description(coupon.getDescription())
                .discountValue(coupon.getDiscountValue().getValue())
                .expirationDate(coupon.getExpirationDate())
                .published(coupon.isPublished())
                .redeemed(coupon.isRedeemed())
                .status(coupon.getStatus())
                .deletedAt(coupon.getDeletedAt())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}
