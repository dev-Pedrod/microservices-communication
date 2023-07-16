package com.devpedrod.productapi.modules.category.service.implementation;

import com.devpedrod.productapi.modules.category.DTO.CategoryResponse;
import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.category.repository.ICategoryRepository;
import com.devpedrod.productapi.modules.category.service.ICategoryService;
import com.devpedrod.productapi.modules.shared.service.implementation.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
public class CategoryService extends GenericService<Category, CategoryResponse, Long, ICategoryRepository> implements ICategoryService {

    @Autowired
    protected CategoryService(WebApplicationContext applicationContext, ModelMapper mapper) {
        super(applicationContext, mapper);
    }
}
