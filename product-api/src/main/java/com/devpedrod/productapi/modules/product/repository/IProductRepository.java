package com.devpedrod.productapi.modules.product.repository;

import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends IGenericRepository<Product, Long> {
    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findByCategoryId(Long category_id);
    List<Product> findBySupplierId(Long supplier_id);
    Boolean existsByCategoryId(Long category_id);
    Boolean existsBySupplierId(Long supplier_id);
}
