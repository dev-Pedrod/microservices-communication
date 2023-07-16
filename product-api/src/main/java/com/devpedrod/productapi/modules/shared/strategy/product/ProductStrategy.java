package com.devpedrod.productapi.modules.shared.strategy.product;

import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.category.service.ICategoryService;
import com.devpedrod.productapi.modules.product.DTO.ProductRequest;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.strategy.IStrategy;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import com.devpedrod.productapi.modules.supplier.service.ISupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductStrategy implements IStrategy {
    private final ICategoryService categoryService;
    private final ISupplierService supplierService;
    private final IProductService service;
    private final ModelMapper mapper;

    @Autowired
    public ProductStrategy(ICategoryService categoryService, ISupplierService supplierService, @Lazy IProductService service, ModelMapper mapper) {
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public BaseEntity applyBusinessRule(BaseDTO baseDTO) {
        if (baseDTO instanceof ProductRequest productRequest) {
            // if it's update
            if (!Objects.isNull(productRequest.getId())) {
                var exist = service.findById(productRequest.getId());
                mapper.map(exist, productRequest);
            }
            Category category = this.categoryService.getReference(productRequest.getCategoryId());
            Supplier supplier = this.supplierService.getReference(productRequest.getSupplierId());
            return Product.of(productRequest, supplier, category);
        }
        return null;
    }

}
