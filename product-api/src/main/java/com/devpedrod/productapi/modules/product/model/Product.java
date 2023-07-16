package com.devpedrod.productapi.modules.product.model;

import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.product.DTO.ProductRequest;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.strategy.product.ProductStrategy;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;

@Data
@Entity(name = "tb_product")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "disabled_at is null")
public class Product extends BaseEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;

    public static Product of(ProductRequest request, Supplier supplier, Category category) {
        return Product
                .builder()
                .id(request.getId())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getCreatedAt())
                .disabledAt(request.getDisabledAt())
                .name(request.getName())
                .quantityAvailable(request.getQuantityAvailable())
                .supplier(supplier)
                .category(category)
                .build();
    }

    public void updateStock(Integer quantity) {
        this.quantityAvailable = quantityAvailable - quantity;
    }
}
