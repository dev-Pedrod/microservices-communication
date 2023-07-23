package com.devpedrod.productapi.modules.product.service;

import com.devpedrod.productapi.modules.product.DTO.*;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.shared.DTO.SuccessResponse;
import com.devpedrod.productapi.modules.shared.service.IGenericSerice;

import java.util.List;

public interface IProductService extends IGenericSerice<Product, ProductResponse, Long> {
    List<ProductResponse> findByName(String name);

    ProductResponse create(ProductRequest request);

    ProductResponse update(ProductRequest request);

    List<ProductResponse> findBySupplierId(Long supplierId);

    List<ProductResponse> findByCategoryId(Long categoryId);

    ProductSalesResponse findProductSales(Long id);

    ProductResponse findByIdResponse(Long id);

    void updateProductStock(ProductStockDTO product);

    SuccessResponse checkProductsStock(ProductCheckStockRequest request);

    Boolean existsByCategoryId(Long categoryId);

    Boolean existsBySupplierId(Long supplierId);
}
