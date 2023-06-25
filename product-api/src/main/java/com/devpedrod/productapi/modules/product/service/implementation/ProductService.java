package com.devpedrod.productapi.modules.product.service.implementation;

import com.devpedrod.productapi.modules.product.DTO.ProductResponse;
import com.devpedrod.productapi.modules.product.DTO.ProductSalesResponse;
import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.product.repository.IProductRepository;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.devpedrod.productapi.modules.shared.exceptions.ValidationException;
import com.devpedrod.productapi.modules.shared.service.implementation.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService extends GenericService<Product, ProductResponse, Long, IProductRepository> implements IProductService {

    @Autowired
    public ProductService(WebApplicationContext applicationContext, ModelMapper mapper) {
        super(applicationContext, mapper);
    }

    @Override
    @Transactional
    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) throw new ValidationException("The product name must be informed.");
        return repository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProductResponse> findBySupplierId(Long supplierId) {
        if (isEmpty(supplierId)) throw new ValidationException("The product' supplier ID name must be informed.");
        return repository.findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ProductResponse> findByCategoryId(Long categoryId) {
        if (isEmpty(categoryId)) throw new ValidationException("The product' category ID name must be informed.");
        return repository.findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductSalesResponse findProductSales(Long id) {
        var product = super.findById(id);
        var sales = new ArrayList<String>();
        return ProductSalesResponse.of(product, sales);
    }

    @Override
    @Transactional
    public ProductResponse findByIdResponse(Long id) {
        return ProductResponse.of(super.findById(id));
    }
}
