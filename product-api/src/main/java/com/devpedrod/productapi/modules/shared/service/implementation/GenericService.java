package com.devpedrod.productapi.modules.shared.service.implementation;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.exceptions.ObjectNotFoundException;
import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import com.devpedrod.productapi.modules.shared.service.IGenericSerice;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public abstract class GenericService<T extends BaseEntity, DTO extends BaseDTO, ID, REPOSITORY extends IGenericRepository<T, ID>> implements IGenericSerice<T, DTO, ID> {

    @Getter(AccessLevel.PROTECTED)
    private final REPOSITORY repository;
    private final Repositories repositories;
    @Getter(AccessLevel.PROTECTED)
    private final Class<T> domainClass;
    @Getter(AccessLevel.PROTECTED)
    private final Class<DTO> DTOClass;
    @Getter(AccessLevel.PROTECTED)
    private final ModelMapper mapper;

    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Entity type: %s not found with id: %s", getDomainClass().getName(), id)));
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(ID id, boolean includeDisabled) {
        if (includeDisabled) return repository.findByIdIncludeDisabled(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Entity type: %s not found with id: %s", getDomainClass().getName(), id)));
        return this.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(e -> mapper.map(e, getDTOClass()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T getReference(ID id) {
        return repository.getReferenceById(id);
    }

    @Override
    @Transactional()
    public T create(T entity) {
        log.info("new entity created. type: {}", getDomainClass().getName());
        return repository.save(entity);
    }

    @Override
    @Transactional()
    public T update(T entity) {
        entity.setUpdatedAt(LocalDateTime.now());
        log.info("entity updated. id: {} type: {}", entity.getId(), getDomainClass().getName());
        return repository.save(entity);
    }

    @Override
    @Transactional()
    public void delete(ID id) {
        T entity = this.getReference(id);
        entity.setDisabledAt(LocalDateTime.now());
        this.saveAndFlush(entity);
        log.info("entity disabled. id: {} type: {}", id, domainClass.getName());
    }

    @Override
    @Transactional()
    public T saveAndFlush(T entity) {
        log.info("entity saved. id: {} type: {}", entity.getId(), domainClass.getName());
        return this.repository.saveAndFlush(entity);
    }

    @SuppressWarnings(value = "unchecked")
    private Class<T> buildDomainClass(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        } else if (genericSuperclass instanceof Class) {
            return buildDomainClass((Class<?>) genericSuperclass);
        }
        throw new RuntimeException();
    }

    @SuppressWarnings(value = "unchecked")
    private Class<DTO> buildDTOClass(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<DTO>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
        } else if (genericSuperclass instanceof Class) {
            return buildDTOClass((Class<?>) genericSuperclass);
        }
        throw new RuntimeException();
    }

    @SuppressWarnings(value = "unchecked")
    private REPOSITORY buildRepository() {
        return (REPOSITORY) repositories.getRepositoryFor(this.getDomainClass())
                .orElseThrow(() -> new IllegalStateException("Can't find repository for entity of type " + this.getDomainClass()));
    }

    @Autowired
    protected GenericService(WebApplicationContext applicationContext, ModelMapper mapper) {
        this.mapper = mapper;
        this.domainClass = buildDomainClass(getClass());
        this.DTOClass = buildDTOClass(getClass());
        this.repositories = new Repositories(applicationContext);
        this.repository = buildRepository();
    }
}
