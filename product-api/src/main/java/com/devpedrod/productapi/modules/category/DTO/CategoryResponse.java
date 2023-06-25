package com.devpedrod.productapi.modules.category.DTO;

import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryResponse extends BaseDTO {
    private String description;

    public static CategoryResponse of(Category category) {
        var response = new CategoryResponse();
        BeanUtils.copyProperties(category,response);
        return response;
    }
}