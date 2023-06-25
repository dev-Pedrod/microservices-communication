package com.devpedrod.productapi.modules.shared.service.implementation;

import com.devpedrod.productapi.modules.shared.DTO.BaseDTO;
import com.devpedrod.productapi.modules.shared.entity.BaseEntity;
import com.devpedrod.productapi.modules.shared.exceptions.ObjectNotFoundException;
import com.devpedrod.productapi.modules.shared.repository.IGenericRepository;
import com.devpedrod.productapi.modules.shared.service.IGenericSerice;
import com.devpedrod.productapi.modules.shared.utils.SupportUtils;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public abstract class GenericService<T extends BaseEntity, DTO extends BaseDTO, ID, REPOSITORY extends IGenericRepository<T, ID>> implements IGenericSerice<T, DTO, ID> {

    private static final String OF = "of";
    private static final String UNCHECKED = "unchecked";
    private static final String NOT_FOUND_BY_ID = "Entity type: %s not found with id: %s";

    @Getter(AccessLevel.PROTECTED)
    protected final REPOSITORY repository;
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
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_BY_ID, getDomainClass().getName(), id)));
    }

    @Override
    @Transactional(readOnly = true)
    public T findById(ID id, boolean includeDisabled) {
        if (includeDisabled) return repository.findByIdIncludeDisabled(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(NOT_FOUND_BY_ID, getDomainClass().getName(), id)));
        return this.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(e -> mapper.map(e, getDTOClass()));
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings(UNCHECKED)
    public List<DTO> findAll() {
        return repository.findAll()
                .stream()
                .map(entity -> {
                    try {
                        return (DTO) getDTOClass().getMethod(OF, getDomainClass()).invoke(getDTOClass(), entity);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
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
    public T create(Supplier<T> supplier) {
        return this.create(supplier.get());
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
    public T update(Supplier<T> supplier) {
        return this.update(supplier.get());
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
    public void delete(ID id, SupportUtils.NoReturnExpression method) {
        method.run();
        this.delete(id);
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

    @SuppressWarnings(UNCHECKED)
    private Class<DTO> buildDTOClass(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (Class<DTO>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[1];
        } else if (genericSuperclass instanceof Class) {
            return buildDTOClass((Class<?>) genericSuperclass);
        }
        throw new RuntimeException();
    }

    @SuppressWarnings(UNCHECKED)
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
