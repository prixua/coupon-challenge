package com.coupon.infrastructure.controller.api;

import com.coupon.application.dto.CreateCouponRequest;
import com.coupon.application.dto.CreateCouponResponse;
import com.coupon.application.dto.DeleteCouponResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Cupom", description = "Endpoints para gerenciamento de cupons")
public interface ControllerApi {

    @PostMapping
    @Operation(
            summary = "Criar um novo cupom",
            description = "Cria um novo cupom com informações de desconto. O código do cupom será sanitizado para 6 caracteres alfanuméricos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cupom criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados do cupom inválidos (data expiração no passado, desconto < 0.5, código vazio)"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Código do cupom já existe"
            )
    })
    ResponseEntity<CreateCouponResponse> createCoupon(
            @RequestBody CreateCouponRequest request
    );

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar um cupom",
            description = "Deleta um cupom por seu ID (soft delete - o cupom é marcado como deletado, mas não é removido do banco)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cupom deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cupom não encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cupom já foi deletado"
            )
    })
    ResponseEntity<DeleteCouponResponse> deleteCoupon(
            @PathVariable String id
    );

    @Schema(description = "Resposta de erro")
    class ErrorResponse {
        @Schema(description = "Mensagem de erro")
        public String message;

        @Schema(description = "Timestamp quando o erro ocorreu")
        public long timestamp;
    }
}
