package com.devpedrod.productapi.modules.product.model;

import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;
}
