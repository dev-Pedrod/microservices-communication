package com.devpedrod.productapi.modules.supplier.model;

import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.supplier.DTO.SupplierRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.beans.BeanUtils;

@Data
@Entity(name = "tb_supplier")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "disabled_at is null")
public class Supplier extends BaseEntity {

    @Column(name = "NAME", nullable = false)
    private String name;

    public static Supplier of(SupplierRequest request) {
        var supplier = new Supplier();
        BeanUtils.copyProperties(request, supplier);
        return supplier;
    }
}
