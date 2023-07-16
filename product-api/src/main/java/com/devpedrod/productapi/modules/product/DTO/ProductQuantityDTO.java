package com.devpedrod.productapi.modules.product.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityDTO {
    private Long productId;
    private Integer quantity;
}
