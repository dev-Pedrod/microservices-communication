package com.devpedrod.productapi.modules.supplier.service.implementation;

import com.devpedrod.productapi.modules.shared.exceptions.ValidationException;
import com.devpedrod.productapi.modules.shared.service.implementation.GenericService;
import com.devpedrod.productapi.modules.supplier.DTO.SupplierResponse;
import com.devpedrod.productapi.modules.supplier.model.Supplier;
import com.devpedrod.productapi.modules.supplier.repository.ISupplierRepository;
import com.devpedrod.productapi.modules.supplier.service.ISupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService extends GenericService<Supplier, SupplierResponse, Long, ISupplierRepository> implements ISupplierService {

    @Autowired
    protected SupplierService(WebApplicationContext applicationContext, ModelMapper mapper) {
        super(applicationContext, mapper);
    }


    @Override
    @Transactional
    public List<SupplierResponse> findByName(String name) {
        if (isEmpty(name)) throw new ValidationException("The supplier name must be informed.");
        return repository.findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SupplierResponse findByIdResponse(Long id) {
        if (isEmpty(id)) throw new ValidationException("The supplier id must be informed.");
        return SupplierResponse.of(super.findById(id));
    }
}
