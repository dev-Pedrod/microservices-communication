package com.devpedrod.productapi.modules.product.repository;

import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends IGenericRepository<Product, Long> {
}
