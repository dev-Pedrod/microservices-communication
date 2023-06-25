package com.devpedrod.productapi.modules.shared.service;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.utils.SupportUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Supplier;

public interface IGenericSerice<T extends BaseEntity, DTO extends BaseDTO, ID> {
    T findById(ID id, boolean includeDisabled);
    T findById(ID id);
    Page<DTO> findAll(Pageable pageable);
    List<T> findAll();
    T getReference(ID id);
    T create(T entity);
    T create(Supplier<T> supplier);
    T update(T entity);
    T update(Supplier<T> supplier);
    void delete(ID id);
    void delete(ID id, SupportUtils.NoReturnExpression method);
    T saveAndFlush(T entity);
}
