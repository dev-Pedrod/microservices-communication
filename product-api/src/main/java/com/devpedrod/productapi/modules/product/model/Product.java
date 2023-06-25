package com.devpedrod.productapi.modules.product.model;

import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.Where;

@Data
@Entity(name = "tb_product")
@Builder
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
}
