package com.devpedrod.productapi.modules.shared.service;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.utils.SupportUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Supplier;

public interface IGenericSerice<T extends BaseEntity, DTO extends BaseDTO, ID> {
    T findById(ID id, boolean includeDisabled) throws IllegalAccessException, InstantiationException;
    T findById(ID id);
    Page<DTO> findAll(Pageable pageable);
    List<DTO> findAll();
    T getReference(ID id);
    T save(T entity);
    T save(Supplier<T> supplier);
    T update(T entity);
    T update(Supplier<T> supplier);
    void delete(ID id);
    void delete(ID id, SupportUtils.NoReturnExpression method);
    void reactivateEntity(ID id);
    T saveAndFlush(T entity);
}
