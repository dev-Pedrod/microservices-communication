package com.devpedrod.productapi.modules.product.DTO;

import com.devpedrod.productapi.modules.category.DTO.CategoryResponse;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.supplier.DTO.SupplierResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductSalesResponse extends BaseDTO {
    private String name;
    private Integer quantityAvailable;
    private SupplierResponse supplier;
    private CategoryResponse category;
    private List<String> sales;

    public static ProductSalesResponse of(Product product, List<String> sales) {
        return ProductSalesResponse
                .builder()
                .id(product.getId())
                .name(product.getName())
                .quantityAvailable(product.getQuantityAvailable())
                .createdAt(product.getCreatedAt())
                .supplier(SupplierResponse.of(product.getSupplier()))
                .category(CategoryResponse.of(product.getCategory()))
                .sales(sales)
                .build();
    }
}
