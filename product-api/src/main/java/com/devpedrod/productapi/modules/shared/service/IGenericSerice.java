package com.devpedrod.productapi.modules.shared.service;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGenericSerice<T extends BaseEntity, DTO extends BaseDTO, ID> {
    T findById(ID id, boolean includeDisabled);
    T findById(ID id);
    Page<DTO> findAll(Pageable pageable);
    List<T> findAll();
    T getReference(ID id);
    T create(T dto);
    T update(T dto);
    void delete(ID id);
    T saveAndFlush(T entity);
}
