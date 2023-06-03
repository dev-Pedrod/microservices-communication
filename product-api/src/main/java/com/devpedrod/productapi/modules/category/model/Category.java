package com.devpedrod.productapi.modules.category.model;

import com.devpedrod.productapi.modules.category.DTO.CategoryRequest;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.beans.BeanUtils;

@Data
@Entity(name = "tb_category")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "disabled_at is null")
public class Category extends BaseEntity {

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public static Category of(CategoryRequest request) {
        var category = new Category();
        BeanUtils.copyProperties(request, category);
        return category;
    }
}