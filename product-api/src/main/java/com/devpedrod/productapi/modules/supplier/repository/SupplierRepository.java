package com.devpedrod.productapi.modules.supplier.repository;

import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends IGenericRepository<Supplier, Long> {
}