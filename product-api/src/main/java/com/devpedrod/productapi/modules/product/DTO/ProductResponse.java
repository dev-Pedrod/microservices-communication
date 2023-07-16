package com.devpedrod.productapi.modules.product.DTO;

import com.devpedrod.productapi.modules.category.DTO.CategoryResponse;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.supplier.DTO.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductResponse extends BaseDTO {
    private Long id;
    private String name;
    @JsonProperty("quantity_available")
    private Integer quantityAvailable;
    private SupplierResponse supplier;
    private CategoryResponse category;

    public static ProductResponse of(Product product) {
        return ProductResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .quantityAvailable(product.getQuantityAvailable())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .supplier(SupplierResponse.of(product.getSupplier()))
                .category(CategoryResponse.of(product.getCategory()))
                .build();
    }
}
