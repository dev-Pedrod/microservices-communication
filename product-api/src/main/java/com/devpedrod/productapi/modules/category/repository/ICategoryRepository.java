package com.devpedrod.productapi.modules.category.repository;

import com.devpedrod.productapi.modules.category.model.Category;
import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends IGenericRepository<Category, Long> {
}
