package com.devpedrod.productapi.modules.supplier.service;

import com.devpedrod.productapi.modules.shared.service.IGenericSerice;
import com.devpedrod.productapi.modules.supplier.DTO.SupplierResponse;
import com.devpedrod.productapi.modules.supplier.model.Supplier;

import java.util.List;

public interface ISupplierService extends IGenericSerice<Supplier, SupplierResponse, Long> {
    List<SupplierResponse> findByName(String name);
    SupplierResponse findByIdResponse(Long id);
}
