package com.devpedrod.productapi.modules.shared.service.implementation;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import com.devpedrod.productapi.modules.shared.service.IGenericSerice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@Service
public class GenericService<T extends BaseEntity, DTO extends BaseDTO> implements IGenericSerice<T, DTO> {

    @Getter(AccessLevel.PRIVATE)
    private final IGenericRepository<T, DTO> repository;

    @Getter(AccessLevel.PROTECTED)
    private final Class<T> domainClass;


    @Autowired
    public GenericService(IGenericRepository<T, DTO> repository) {
        this.repository = repository;
        this.domainClass = getDomainClass(getClass());

    }

    @Override
    public T findById(Long id) {
        return null;
    }

    @Override
    public T findById(Long id, boolean includeDisabled) {
        return null;
    }

    @Override
    public Page<DTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public T getReference(Long id) {
        return null;
    }

    @Override
    public T create(DTO dto) {
        log.info("new entity created. type: {}", domainClass.getName());
        return null;
    }

    @Override
    public T update(DTO dto) {
        log.info("entity updated. id: {} type: {}", dto.getId(), domainClass.getName());
        return null;
    }

    @Override
    public void delete(Long id) {
        log.info("entity disabled. id: {} type: {}", id, domainClass.getName());
    }

    @Override
    public T saveAndFlush(T entity) {
        log.info("entity saved. id: {} type: {}", entity.getId(), domainClass.getName());
        return null;
    }

    @SuppressWarnings(value = "unchecked")
    private Class<T> getDomainClass(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else if (genericSuperclass instanceof Class) {
            return getDomainClass((Class<?>) genericSuperclass);
        }
        throw new RuntimeException();
    }
}
