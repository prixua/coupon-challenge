package com.coupon.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteCouponResponse {

    private String id;

    private boolean success;

    private String message;

    public static DeleteCouponResponse success(String id) {
        return DeleteCouponResponse.builder()
                .id(id)
                .success(true)
                .message("Coupon deleted successfully")
                .build();
    }
}
