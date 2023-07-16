package com.devpedrod.productapi.modules.shared.repository;

import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@NoRepositoryBean
public interface IGenericRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

    @Transactional(readOnly = true)
    @Query(value = "select * from #{#entityName} entity where entity.id = ?1 and (entity.disabled_at is null or entity.disabled_at is not null)", nativeQuery = true)
    Optional<T> findByIdIncludeDisabled(ID id);

    @Modifying
    @Transactional(propagation = REQUIRED)
    @Query(value = "update #{#entityName} set disabled_at = null where id = ?1", nativeQuery = true)
    void reactivateEntity(Long id);
}