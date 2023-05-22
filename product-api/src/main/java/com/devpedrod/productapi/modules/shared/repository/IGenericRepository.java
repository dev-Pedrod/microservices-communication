package com.devpedrod.productapi.modules.shared.repository;

import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IGenericRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    @Override
    Page<T> findAll(Specification<T> specification, Pageable pageable);

    @Override
    List<T> findAll(Specification<T> specification);
}
