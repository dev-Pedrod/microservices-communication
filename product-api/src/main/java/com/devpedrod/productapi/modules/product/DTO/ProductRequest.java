package com.devpedrod.productapi.modules.product.DTO;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductRequest extends BaseDTO {
    private String name;
    @JsonProperty("quantity_available")
    private Integer quantityAvailable;
    private Integer supplierId;
    private Integer categoryId;
}
