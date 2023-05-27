package com.devpedrod.productapi.modules.shared.repository;

import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

    @Transactional(readOnly = true)
    @Query(value = "select * from #{#entityName} entity where entity.id = ?1 and (entity.disabled_at is null or entity.disabled_at is not null)", nativeQuery = true)
    Optional<T> findByIdIncludeDisabled(ID id);
}