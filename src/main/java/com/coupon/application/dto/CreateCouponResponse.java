package com.coupon.application.dto;

import com.coupon.domain.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response after creating a coupon")
public class CreateCouponResponse {

    @Schema(description = "Unique coupon identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Sanitized coupon code (6 characters)", example = "ABC123")
    private String code;

    @Schema(description = "Coupon description", example = "10% discount on electronics")
    private String description;

    @Schema(description = "Discount value", example = "10.50")
    private BigDecimal discountValue;

    @Schema(description = "Coupon expiration date", example = "2025-12-31T23:59:59")
    private LocalDateTime expirationDate;

    @Schema(description = "Coupon status (ACTIVE, INACTIVE, DELETED)", example = "ACTIVE")
    private String status;

    @Schema(description = "Whether coupon is published", example = "false")
    private boolean published;

    @Schema(description = "Whether coupon has been redeemed", example = "false")
    private boolean redeemed;

    public static CreateCouponResponse fromDomain(Coupon coupon) {
        return CreateCouponResponse.builder()
                .id(coupon.getId().getValue())
                .code(coupon.getCode().getValue())
                .description(coupon.getDescription())
                .discountValue(coupon.getDiscountValue().getValue())
                .expirationDate(coupon.getExpirationDate())
                .status(coupon.getStatus().name())
                .published(coupon.isPublished())
                .redeemed(coupon.isRedeemed())
                .build();
    }
}
