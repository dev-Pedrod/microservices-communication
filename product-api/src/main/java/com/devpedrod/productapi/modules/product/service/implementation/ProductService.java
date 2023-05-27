package com.devpedrod.productapi.modules.product.service.implementation;

import com.devpedrod.productapi.modules.product.model.Product;
import com.devpedrod.productapi.modules.product.repository.IProductRepository;
import com.devpedrod.productapi.modules.product.service.IProductService;
import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.service.implementation.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class ProductService extends GenericService<Product, BaseDTO, Long, IProductRepository> implements IProductService {

    @Autowired
    public ProductService(WebApplicationContext applicationContext, ModelMapper mapper) {
        super(applicationContext, mapper);
    }
}
