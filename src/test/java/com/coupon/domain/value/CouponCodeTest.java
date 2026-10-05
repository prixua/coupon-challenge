package com.coupon.domain.value;

import com.coupon.domain.exception.InvalidCouponCodeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CouponCode Value Object Tests")
class CouponCodeTest {

    @Test
    @DisplayName("Should create a valid coupon code")
    void shouldCreateValidCouponCode() {
        String code = "ABC123";

        CouponCode result = CouponCode.of(code);

        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo("ABC123");
    }

    @Test
    @DisplayName("Should sanitize and uppercase coupon code")
    void shouldSanitizeAndUppercaseCode() {
        String code = "abc-123";

        CouponCode result = CouponCode.of(code);

        assertThat(result.getValue()).isEqualTo("ABC123");
    }

    @Test
    @DisplayName("Should pad code to 6 characters with zeros")
    void shouldPadCodeToSixCharacters() {
        String code = "AB";

        CouponCode result = CouponCode.of(code);

        assertThat(result.getValue()).hasSize(6);
        assertThat(result.getValue()).startsWith("AB");
    }

    @Test
    @DisplayName("Should truncate code longer than 6 characters")
    void shouldTruncateCodeLongerThanSix() {
        String code = "ABCDEFGHIJ";

        CouponCode result = CouponCode.of(code);

        assertThat(result.getValue()).isEqualTo("ABCDEF");
        assertThat(result.getValue()).hasSize(6);
    }

    @Test
    @DisplayName("Should remove special characters from code")
    void shouldRemoveSpecialCharacters() {
        String code = "A@B#C$1%2^3&";

        CouponCode result = CouponCode.of(code);

        assertThat(result.getValue()).contains("ABC123");
        assertThat(result.getValue()).doesNotContain("@", "#", "$", "%", "^", "&");
    }

    @Test
    @DisplayName("Should throw exception for null code")
    void shouldThrowExceptionForNullCode() {
        assertThatThrownBy(() -> CouponCode.of(null))
                .isInstanceOf(InvalidCouponCodeException.class)
                .hasMessageContaining("cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception for empty code")
    void shouldThrowExceptionForEmptyCode() {
        assertThatThrownBy(() -> CouponCode.of(""))
                .isInstanceOf(InvalidCouponCodeException.class)
                .hasMessageContaining("cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when code has only special characters")
    void shouldThrowExceptionForOnlySpecialCharacters() {
        assertThatThrownBy(() -> CouponCode.of("@#$%^&"))
                .isInstanceOf(InvalidCouponCodeException.class)
                .hasMessageContaining("must contain at least one alphanumeric character");
    }

    @Test
    @DisplayName("Should be equal when values are equal")
    void shouldBeEqualWhenValuesAreEqual() {
        CouponCode code1 = CouponCode.of("ABC123");
        CouponCode code2 = CouponCode.of("abc-123");

        assertThat(code1).isEqualTo(code2);
    }
}
