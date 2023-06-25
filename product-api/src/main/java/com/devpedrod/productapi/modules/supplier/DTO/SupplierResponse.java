package com.devpedrod.productapi.modules.supplier.DTO;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierResponse extends BaseDTO {
    private String name;

    public static SupplierResponse of(Supplier supplier) {
        var response = new SupplierResponse();
        BeanUtils.copyProperties(supplier, response);
        return response;
    }
}
