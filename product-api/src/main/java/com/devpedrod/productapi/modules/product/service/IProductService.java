package com.devpedrod.productapi.modules.product.service;

import com.devpedrod.productapi.modules.product.DTO.ProductResponse;
import com.devpedrod.productapi.modules.product.DTO.ProductSalesResponse;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.shared.service.IGenericSerice;

import java.util.List;

public interface IProductService extends IGenericSerice<Product, ProductResponse, Long> {
    List<ProductResponse> findByName(String name);
    List<ProductResponse> findBySupplierId(Long supplierId);
    List<ProductResponse> findByCategoryId(Long categoryId);
    ProductSalesResponse findProductSales(Long id);
    ProductResponse findByIdResponse(Long id);
}
