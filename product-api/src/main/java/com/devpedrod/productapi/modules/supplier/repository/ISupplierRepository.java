package com.devpedrod.productapi.modules.supplier.repository;

import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISupplierRepository extends IGenericRepository<Supplier, Long> {
    List<Supplier> findByNameIgnoreCaseContaining(String name);
}