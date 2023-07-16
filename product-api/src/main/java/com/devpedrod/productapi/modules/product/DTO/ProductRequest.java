package com.devpedrod.productapi.modules.product.DTO;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductRequest extends BaseDTO {
    @NotEmpty(message = "The product's name was not informed.")
    @NotNull(message = "The product's name was not informed.")
    private String name;

    @JsonProperty("quantity_available")
    @NotNull(message = "The product's quantity was not informed.")
    @PositiveOrZero(message = "The quantity should not be less or equal to zero.")
    private Integer quantityAvailable;

    @Positive
    @NotNull(message = "The supplier ID was not informed.")
    private Long supplierId;

    @Positive
    @NotNull(message = "The category ID was not informed.")
    private Long categoryId;
}