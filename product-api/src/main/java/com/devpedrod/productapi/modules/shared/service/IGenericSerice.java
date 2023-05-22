package com.devpedrod.productapi.modules.shared.service;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGenericSerice<T extends BaseEntity, DTO extends BaseDTO> {
    T findById(Long id);
    T findById(Long id, boolean includeDisabled);
    Page<DTO> findAll(Pageable pageable);
    List<T> findAll();
    T getReference(Long id);
    T create(DTO dto);
    T update(DTO dto);
    void delete(Long id);
    T saveAndFlush(T entity);
}
