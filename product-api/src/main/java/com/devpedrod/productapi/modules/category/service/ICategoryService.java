package com.devpedrod.productapi.modules.category.service;

import com.devpedrod.productapi.modules.category.DTO.CategoryResponse;
import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.shared.service.IGenericSerice;

public interface ICategoryService extends IGenericSerice<Category, CategoryResponse, Long> {
}
