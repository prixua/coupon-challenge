package com.coupon.infrastructure.controller;

import com.coupon.application.dto.CreateCouponRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Coupon Controller Integration Tests")
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should create a coupon successfully with status 201")
    void shouldCreateCouponSuccessfully() throws Exception {
        CreateCouponRequest request = CreateCouponRequest.builder()
                .code("TEST01")
                .description("Test coupon")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(false)
                .build();

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.code", equalTo("TEST01")))
                .andExpect(jsonPath("$.description", equalTo("Test coupon")))
                .andExpect(jsonPath("$.discountValue", equalTo(10.00)))
                .andExpect(jsonPath("$.status", equalTo("ACTIVE")))
                .andExpect(jsonPath("$.published", equalTo(false)))
                .andExpect(jsonPath("$.redeemed", equalTo(false)));
    }

    @Test
    @DisplayName("Should return 400 when creating coupon with expiration date in the past")
    void shouldReturnBadRequestForPastExpirationDate() throws Exception {
        CreateCouponRequest request = CreateCouponRequest.builder()
                .code("TEST02")
                .description("Test coupon")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().minusDays(1))
                .published(false)
                .build();

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when creating coupon with discount below minimum")
    void shouldReturnBadRequestForInvalidDiscount() throws Exception {
        CreateCouponRequest request = CreateCouponRequest.builder()
                .code("TEST03")
                .description("Test coupon")
                .discountValue(new BigDecimal("0.25"))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(false)
                .build();

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 409 when creating coupon with duplicate code")
    void shouldReturnConflictForDuplicateCode() throws Exception {
        CreateCouponRequest request = CreateCouponRequest.builder()
                .code("UNIQUE01")
                .description("Test coupon")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(false)
                .build();

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should delete a coupon successfully")
    void shouldDeleteCouponSuccessfully() throws Exception {
        CreateCouponRequest createRequest = CreateCouponRequest.builder()
                .code("DELETE01")
                .description("Test coupon")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(false)
                .build();

        MvcResult createResult = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String couponId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/coupon/" + couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(couponId)))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent coupon")
    void shouldReturnNotFoundWhenDeletingNonExistentCoupon() throws Exception {
        mockMvc.perform(delete("/coupon/non-existent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return 409 when deleting already deleted coupon")
    void shouldReturnConflictWhenDeletingAlreadyDeletedCoupon() throws Exception {
        CreateCouponRequest createRequest = CreateCouponRequest.builder()
                .code("DELETE02")
                .description("Test coupon")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(false)
                .build();

        MvcResult createResult = mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String couponId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(delete("/coupon/" + couponId))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/coupon/" + couponId))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should sanitize coupon code in response")
    void shouldSanitizeCouponCodeInResponse() throws Exception {
        CreateCouponRequest request = CreateCouponRequest.builder()
                .code("T@E#S-T")
                .description("Test coupon")
                .discountValue(new BigDecimal("10.00"))
                .expirationDate(LocalDateTime.now().plusDays(1))
                .published(false)
                .build();

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code", not(containsString("@"))))
                .andExpect(jsonPath("$.code", not(containsString("#"))))
                .andExpect(jsonPath("$.code", not(containsString("-"))));
    }

    @Test
    @DisplayName("Should create coupon with published flag")
    void shouldCreateCouponWithPublishedFlag() throws Exception {
        CreateCouponRequest request = CreateCouponRequest.builder()
                .code("PUBLISH01")
                .description("Published coupon")
                .discountValue(new BigDecimal("5.50"))
                .expirationDate(LocalDateTime.now().plusDays(7))
                .published(true)
                .build();

        mockMvc.perform(post("/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.published", equalTo(true)));
    }
}
