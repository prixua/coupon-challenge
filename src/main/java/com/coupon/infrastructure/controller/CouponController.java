package com.coupon.infrastructure.controller;

import com.coupon.application.dto.CreateCouponRequest;
import com.coupon.application.dto.CreateCouponResponse;
import com.coupon.application.dto.DeleteCouponRequest;
import com.coupon.application.dto.DeleteCouponResponse;
import com.coupon.application.usecase.CreateCouponUseCase;
import com.coupon.application.usecase.DeleteCouponUseCase;
import com.coupon.domain.exception.*;
import com.coupon.infrastructure.controller.api.ControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController implements ControllerApi {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;

    @Override
    public ResponseEntity<CreateCouponResponse> createCoupon(CreateCouponRequest request) {
        try {
            CreateCouponResponse response = createCouponUseCase.execute(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateCouponCodeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (CouponException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<DeleteCouponResponse> deleteCoupon(String id) {
        try {
            DeleteCouponRequest request = DeleteCouponRequest.builder().id(id).build();
            DeleteCouponResponse response = deleteCouponUseCase.execute(request);
            return ResponseEntity.ok(response);
        } catch (CouponNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (CouponAlreadyDeletedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (CouponException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ControllerApi.ErrorResponse> handleCouponException(CouponException e) {
        ControllerApi.ErrorResponse error = new ControllerApi.ErrorResponse();
        error.message = e.getMessage();
        error.timestamp = System.currentTimeMillis();

        if (e instanceof CouponNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else if (e instanceof DuplicateCouponCodeException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } else if (e instanceof CouponAlreadyDeletedException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
