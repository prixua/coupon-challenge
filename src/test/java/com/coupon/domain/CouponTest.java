package com.coupon.domain;

import com.coupon.domain.exception.CouponAlreadyDeletedException;
import com.coupon.domain.exception.InvalidDiscountValueException;
import com.coupon.domain.exception.InvalidExpirationDateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Coupon Aggregate Root Tests")
class CouponTest {

    @Test
    @DisplayName("Should create a coupon with valid data")
    void shouldCreateCouponWithValidData() {
        String code = "TEST01";
        String description = "Test discount";
        BigDecimal discount = new BigDecimal("10.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        boolean published = false;

        Coupon coupon = Coupon.create(code, description, discount, expirationDate, published);

        assertThat(coupon).isNotNull();
        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.getCode().getValue()).isEqualTo("TEST01");
        assertThat(coupon.getDescription()).isEqualTo(description);
        assertThat(coupon.getDiscountValue().getValue()).isEqualTo(discount);
        assertThat(coupon.getExpirationDate()).isEqualTo(expirationDate);
        assertThat(coupon.isPublished()).isFalse();
        assertThat(coupon.isRedeemed()).isFalse();
        assertThat(coupon.getStatus()).isEqualTo(Coupon.CouponStatus.ACTIVE);
        assertThat(coupon.getDeletedAt()).isNull();
    }

    @Test
    @DisplayName("Should throw exception when expiration date is in the past")
    void shouldThrowExceptionForPastExpirationDate() {
        String code = "TEST01";
        String description = "Test discount";
        BigDecimal discount = new BigDecimal("10.00");
        LocalDateTime expirationDate = LocalDateTime.now().minusDays(1);
        boolean published = false;

        assertThatThrownBy(() -> Coupon.create(code, description, discount, expirationDate, published))
                .isInstanceOf(InvalidExpirationDateException.class)
                .hasMessageContaining("cannot be in the past");
    }

    @Test
    @DisplayName("Should throw exception when discount is below minimum")
    void shouldThrowExceptionForInvalidDiscount() {
        String code = "TEST01";
        String description = "Test discount";
        BigDecimal discount = new BigDecimal("0.25");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);
        boolean published = false;

        assertThatThrownBy(() -> Coupon.create(code, description, discount, expirationDate, published))
                .isInstanceOf(InvalidDiscountValueException.class);
    }

    @Test
    @DisplayName("Should sanitize coupon code on creation")
    void shouldSanitizeCouponCode() {
        String code = "test@#-01";
        String description = "Test discount";
        BigDecimal discount = new BigDecimal("5.00");
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

        Coupon coupon = Coupon.create(code, description, discount, expirationDate, false);

        assertThat(coupon.getCode().getValue()).isEqualTo("TEST01");
    }

    @Test
    @DisplayName("Should delete a coupon successfully")
    void shouldDeleteCouponSuccessfully() {
        Coupon coupon = Coupon.create("TEST01", "Test", new BigDecimal("10.00"), 
                LocalDateTime.now().plusDays(1), false);

        coupon.delete();

        assertThat(coupon.isDeleted()).isTrue();
        assertThat(coupon.getStatus()).isEqualTo(Coupon.CouponStatus.DELETED);
        assertThat(coupon.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should throw exception when deleting an already deleted coupon")
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.create("TEST01", "Test", new BigDecimal("10.00"), 
                LocalDateTime.now().plusDays(1), false);
        coupon.delete();

        assertThatThrownBy(coupon::delete)
                .isInstanceOf(CouponAlreadyDeletedException.class)
                .hasMessageContaining("already deleted");
    }

    @Test
    @DisplayName("Should publish a coupon")
    void shouldPublishCoupon() {
        Coupon coupon = Coupon.create("TEST01", "Test", new BigDecimal("10.00"), 
                LocalDateTime.now().plusDays(1), false);

        coupon.publish();

        assertThat(coupon.isPublished()).isTrue();
    }

    @Test
    @DisplayName("Should check if coupon is expired")
    void shouldCheckIfCouponIsExpired() {
        LocalDateTime pastDate = LocalDateTime.now().minusHours(1);
        
        Coupon expiredCoupon = Coupon.builder()
                .id(com.coupon.domain.value.CouponId.generate())
                .code(com.coupon.domain.value.CouponCode.of("TEST01"))
                .description("Test")
                .discountValue(com.coupon.domain.value.DiscountValue.of(new BigDecimal("10.00")))
                .expirationDate(pastDate)
                .published(false)
                .redeemed(false)
                .status(Coupon.CouponStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();

        assertThat(expiredCoupon.isExpired()).isTrue();
    }

    @Test
    @DisplayName("Should check if coupon is active")
    void shouldCheckIfCouponIsActive() {
        Coupon coupon = Coupon.create("TEST01", "Test", new BigDecimal("10.00"), 
                LocalDateTime.now().plusDays(1), false);

        assertThat(coupon.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should not be active when deleted")
    void shouldNotBeActiveWhenDeleted() {
        Coupon coupon = Coupon.create("TEST01", "Test", new BigDecimal("10.00"), 
                LocalDateTime.now().plusDays(1), false);
        coupon.delete();

        assertThat(coupon.isActive()).isFalse();
    }

    @Test
    @DisplayName("Should create coupon as published")
    void shouldCreateCouponAsPublished() {
        Coupon coupon = Coupon.create("TEST01", "Test", new BigDecimal("10.00"), 
                LocalDateTime.now().plusDays(1), true);

        assertThat(coupon.isPublished()).isTrue();
    }
}
