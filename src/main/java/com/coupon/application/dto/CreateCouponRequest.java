package com.coupon.application.dto;

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
@Schema(description = "Request to create a new coupon")
public class CreateCouponRequest {

    @Schema(description = "Coupon code (alphanumeric, will be sanitized to 6 characters)", example = "ABC-123")
    private String code;

    @Schema(description = "Coupon description", example = "10% discount on electronics")
    private String description;

    @Schema(description = "Discount value (minimum 0.5)", example = "10.50")
    private BigDecimal discountValue;

    @Schema(description = "Coupon expiration date (must be in the future)", example = "2025-12-31T23:59:59")
    private LocalDateTime expirationDate;

    @Schema(description = "Whether coupon is published", example = "false")
    private boolean published;
}

